package images;

import images.requests.AlbumCreation;
import images.requests.ImageUpload;
import images.responses.*;
import images.responses.WallpaperThread;
import Utility.FileHelper;
import Utility.RESTHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static constants.ImgurValues.*;


/**
 * Represents all interactions to and from the Imgur API.
 */
@RequiredArgsConstructor
class ImgurContentManager implements Runnable
{
    private final static String GET_REQUEST = "GET";
    private final static String PUT_REQUEST = "PUT";
    private final static String POST_REQUEST = "POST";
    private final static String DELETE_REQUEST = "DELETE";

    private final static String SLASHW_THREAD_ENDPOINT = "http://boards.4channel.org/w/thread/";
    private final static String SLASHW_RESPONSE_URL = "http://i.4cdn.org/w/";

    private final Gson gson;
    private final RESTHelper restHelper;
    private ImgurSecrets secrets;
    private ImgurRateLimiter rateLimiter;

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
     * @param pageNumber
     * @return
     */
    List<URL> getWallpapers(final String pageNumber)
    {
        if (pageNumber == null || pageNumber.isEmpty()) return null;

        try
        {
            final Path authFile = new File(".").toPath().resolve("imgur.json");
            if (!getAuthenticationInfo(authFile, new FileHelper()))
            {
                return null;
            }
            this.rateLimiter = new ImgurRateLimiter(restHelper, gson, secrets); // change this later


            final List<URL> albums = new ArrayList<>();
            final List<String> threadIDs = getSlashwThreads(8);
            for (int i = 0; i < 2; i++)
            {
                try
                {
                    final List<String> imageIDs = new ArrayList<>();
                    final List<Post> posts = getPostsFromThread(threadIDs.get(i));

                    if (!rateLimiter.canUploadEntireAlbum(posts.size() + 1, 0))
                    {
                        long sleepTime = (rateLimiter.getRateLimitResetTime() * 1000L) - System.currentTimeMillis();
                        System.err.println("Rate Limit was reached. Sleeping until reset. Nighty night...");
                        Thread.sleep(sleepTime);
                    }

                    posts.forEach(post ->
                    {
                        try
                        {
                            imageIDs.add(uploadImageToImgur(post));
                        }
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    });
                    albums.add(createImgurAlbum(posts.get(0), imageIDs));
                }
                catch (IOException | InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
            return albums;
        }
        catch (IOException | NullPointerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param pageNumber
     * @return
     * @throws IOException
     */
    private List<String> getSlashwThreads(final int pageNumber) throws IOException
    {
        if (pageNumber <= 0)
        {
            System.err.println("Page number must be 1 or higher");
        }
        final Reader stream = restHelper.sendRESTRequest(GET_REQUEST, new URL("https://a.4cdn.org/w/threads.json"), null, null);
        final Type pageCollection = new TypeToken<List<Page>>(){}.getType();
        final List<Page> pages = gson.fromJson(stream, pageCollection);

        List<String> threadIDs = new ArrayList<>();
        pages.get(pageNumber - 1).getThreads().forEach(x -> threadIDs.add(x.getNo()));
        stream.close();

        return threadIDs;
    }

    /**
     *
     * @param threadID
     * @return
     * @throws IOException
     */
    private List<Post> getPostsFromThread(final String threadID) throws IOException
    {
        final String slashwURL = SLASHW_THREAD_ENDPOINT + threadID + AS_JSON;
        final Reader threadStream = restHelper.sendRESTRequest(GET_REQUEST, new URL(slashwURL), null, null);
        final WallpaperThread wallpaperThread = gson.fromJson(threadStream, WallpaperThread.class);

        return wallpaperThread.getPosts().stream().filter(this::hasImage).collect(Collectors.toList());
    }

    /**
     *
     * @param post
     * @return
     * @throws IOException
     */
    private String uploadImageToImgur(final Post post) throws IOException
    {
        final URL imageURL = new URL(SLASHW_RESPONSE_URL + post.getTim() + post.getExt());
        final ImageUpload image = ImageUpload.builder()
                .type("URL")
                .image(imageURL.toString())
                .build();

        final Reader imageStream = restHelper.sendRESTRequest(POST_REQUEST, new URL("https://api.imgur.com/3/upload"), gson.toJson(image), secrets);
        final ImageResponse response = gson.fromJson(imageStream, ImageResponse.class);
        rateLimiter.updateCreditAmount(POST_REQUEST);

        if (!response.isSuccess()) throw new IOException("Uploading image to Imgur returned " + response.getStatus());
        return response.getData().getId();
    }

    /**
     *
     * @param opPost
     * @param imageIDs
     * @return
     * @throws IOException
     */
    private URL createImgurAlbum(final Post opPost, List<String> imageIDs) throws IOException
    {
        final AlbumCreation wallpaperAlbum = AlbumCreation.builder()
                .title((opPost.getSub() == null || opPost.getSub().isEmpty()) ? opPost.getCom() : opPost.getSub())
                .description(opPost.getCom())
                .ids(imageIDs)
                .deletehashes(imageIDs)
                .privacy("hidden")
                .build();

        final Reader stream = restHelper.sendRESTRequest(POST_REQUEST, new URL("https://api.imgur.com/3/album"), gson.toJson(wallpaperAlbum), secrets);
        final AlbumResponse response = gson.fromJson(stream, AlbumResponse.class);
        rateLimiter.updateCreditAmount(POST_REQUEST);

        if (!response.isSuccess()) throw new IOException("Creating album on Imgur returned " + response.getStatus());
        return new URL("https://imgur.com/a/" + response.getData().getId());
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

    /**
     *
     * @param post
     * @return
     */
    private boolean hasImage(Post post)
    {
        return post.getTim() != 0L;
    }

    @Override
    public void run()
    {

    }
}
