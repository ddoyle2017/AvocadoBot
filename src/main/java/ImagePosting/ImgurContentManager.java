package ImagePosting;

import Utility.FileHelper;
import Utility.RESTHelper;
import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

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
     * @param authFile
     * @param fileHelper
     * @return
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
