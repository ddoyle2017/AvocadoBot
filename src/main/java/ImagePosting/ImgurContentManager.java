package ImagePosting;

import ImagePosting.requests.AlbumCreation;
import ImagePosting.requests.ImageUpload;
import ImagePosting.responses.Album;
import ImagePosting.responses.Gallery;
import ImagePosting.responses.Post;
import Utility.FileHelper;
import Utility.RESTHelper;
import com.google.gson.Gson;

import java.io.*;
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
            final Reader imgurStream = restHelper.getRESTContent(GET_REQUEST, new URL(imgurQueryURL), secrets);
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
    List<URL> getWallpapers(final String opID)
    {
        if (opID == null || opID.isEmpty()) return null;

        try
        {
            final String slashwURL = SLASHW_THREAD_ENDPOINT + opID + AS_JSON;
            final Reader stream = restHelper.getRESTContent(GET_REQUEST, new URL(slashwURL), null);
            final Album.Thread thread = gson.fromJson(stream, Album.Thread.class);

            final List<URL> wallpapers = new ArrayList<>();
            final List<ImageUpload> uploads = new ArrayList<>();

            thread.posts.forEach(post ->
            {
                if (post.getTim() != 0L)
                {
                    try
                    {
                        final URL imageURL = new URL(SLASHW_RESPONSE_URL + post.getTim() + post.getExt());

                        final ImageUpload image = ImageUpload.builder()
                                .name(String.valueOf(post.getTim()))
                                .title(String.valueOf(post.getTime()))
                                .type("URL")
                                .image(imageURL.toString())
                                .description("Slashw wallpaper")
                                .build();

                        wallpapers.add(imageURL);
                        uploads.add(image);
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
            stream.close();


            return wallpapers;
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
                    .title(opPost.getSub())
                    .description(opPost.getCom())
                    .privacy("hidden")
                    .build();

            // Create album
            Reader response = restHelper.getRESTContent("POST", new URL("endpoint"), null);

            // Upload images
            uploads.forEach(u -> {

            });

            // return the URL to the link of the new album
            return new URL("");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
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
