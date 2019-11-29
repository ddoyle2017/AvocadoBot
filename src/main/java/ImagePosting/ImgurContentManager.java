package ImagePosting;

import ImagePosting.requests.AlbumCreation;
import ImagePosting.requests.ImageUpload;
import ImagePosting.responses.*;
import ImagePosting.responses.Thread;
import Utility.FileHelper;
import Utility.RESTHelper;
import com.google.gson.Gson;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static Resources.ImgurValues.*;


/**
 * Represents all interactions to and from the Imgur API.
 */
class ImgurContentManager
{
    private final static String GET_REQUEST = "GET";
    private final static String PUT_REQUEST = "PUT";
    private final static String POST_REQUEST = "POST";
    private final static String DELETE_REQUEST = "DELETE";

    private final static String SLASHW_THREAD_ENDPOINT = "http://boards.4channel.org/w/thread/";
    private final static String SLASHW_RESPONSE_URL = "http://i.4cdn.org/w/";

    private Gson gson;
    private RESTHelper restHelper;
    private ImgurSecrets secrets;


    ImgurContentManager(final Gson gson, final RESTHelper restHelper)
    {
        this.gson = gson;
        this.restHelper = restHelper;
    }

    /**
     * Connects and sends a request to the Imgur API. Returns a Gallery object with the result.
     * @param imgurQuery An item to search Imgur for.
     * @return An object containing an Imgur post matching the search query.
     */
    Gallery getImgurGallery(final String imgurQuery)
    {
        if (imgurQuery == null || imgurQuery.isEmpty()) return null;

        try
        {
            final Path authFile = new File(".").toPath().resolve("imgur.json");
            if (!getAuthenticationInfo(authFile, new FileHelper()))
            {
                return null;
            }

            final String imgurQueryURL = IMGUR_API_URL + SEARCH_IMGUR + "'" + imgurQuery + "'";
            final Reader imgurStream = restHelper.sendRESTRequest(GET_REQUEST, new URL(imgurQueryURL), null, secrets);
            final Gallery result = gson.fromJson(imgurStream, Gallery.class);

            imgurStream.close();
            return result;
        }
        catch (IOException | NullPointerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param opID
     * @return
     */
    URL getWallpapers(final String opID)
    {
        if (opID == null || opID.isEmpty()) return null;

        try
        {
            final Path authFile = new File(".").toPath().resolve("imgur.json");
            if (!getAuthenticationInfo(authFile, new FileHelper()))
            {
                return null;
            }

            final String slashwURL = SLASHW_THREAD_ENDPOINT + opID + AS_JSON;
            final Reader stream = restHelper.sendRESTRequest(GET_REQUEST, new URL(slashwURL), null, secrets);
            final Thread thread = gson.fromJson(stream, Thread.class);

            final List<ImageUpload> uploads = new ArrayList<>();

            thread.getPosts().forEach(post ->
            {
                if (post.getTim() != 0L)
                {
                    try
                    {
                        final URL imageURL = new URL(SLASHW_RESPONSE_URL + post.getTim() + post.getExt());

                        final ImageUpload image = ImageUpload.builder()
                                .name(String.valueOf(post.getTim()))
                                .type("URL")
                                .image(imageURL.toString())
                                .build();

                        uploads.add(image);
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
            stream.close();


            return createWallpaperAlbum(thread.getPosts().get(0), uploads);
        }
        catch (IOException | NullPointerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param opPost
     * @param uploads
     * @return
     */
    URL createWallpaperAlbum(Post opPost, List<ImageUpload> uploads)
    {
        if (uploads == null || uploads.isEmpty()) return null;

        try
        {
            AlbumCreation wallpaperAlbum = AlbumCreation.builder()
                    .title((opPost.getSub() == null || opPost.getSub().isEmpty()) ? opPost.getCom() : opPost.getSub())
                    .description(opPost.getCom())
                    .privacy("hidden")
                    .build();

            Reader stream = restHelper.sendRESTRequest(POST_REQUEST, new URL("https://api.imgur.com/3/album"), gson.toJson(wallpaperAlbum), secrets);
            AlbumResponse album = gson.fromJson(stream, AlbumResponse.class);

            if (album == null || !album.isSuccess())
            {
                System.err.println("Could not create Imgur album");
                return null;
            }
            if (!uploadImages(new URL("https://api.imgur.com/3/image"), uploads, album.getData().getDeletehash()))
            {
                System.err.println("Failed to upload an image. Rolling back Imgur album creation");
                //
                // TO-DO: Delete album if an upload fails and retry later.
                //
                return null;
            }
            return new URL("https://imgur.com/a/" + album.getData().getId());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    boolean uploadImages(final URL imgurUploadURL, final List<ImageUpload> uploads, final String albumID) throws IOException
    {
        for (ImageUpload u : uploads)
        {
            u.setAlbum(albumID);
            Reader stream = restHelper.sendRESTRequest(POST_REQUEST, imgurUploadURL, gson.toJson(u), secrets);
            ImageResponse response = gson.fromJson(stream, ImageResponse.class);

            if (!response.isSuccess()) return false;
        }
        return true;
    }


    /**
     * Retrieves AvocadoBot's Imgur API secrets and saves them.
     * @param authFile The file path to where the API secrets are being stored.
     * @param fileHelper A helper class for reading the file.
     * @return True, if the contents were found and saved. False, if the operation failed.
     */
    boolean getAuthenticationInfo(final Path authFile, final FileHelper fileHelper)
    {
        if (fileHelper == null) return false;

        try
        {
            final Reader fileStream = fileHelper.getFileContent(authFile);
            this.secrets = gson.fromJson(fileStream, ImgurSecrets.class);
            secrets.setRequestKey("Authorization");
            secrets.setRequestKeyValue("Client-ID " + secrets.getClientID());

            fileStream.close();
            return true;
        }
        catch (IOException | NullPointerException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
