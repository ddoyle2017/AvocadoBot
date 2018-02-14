package ImagePosting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static Resources.ImgurValues.*;


@SuppressWarnings("FieldCanBeLocal")
class ImgurContentManager
{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path authFile = new File(".").toPath().resolve("imgur.json");
    private ImgurSecrets secrets;
    private URL url;
    private HttpURLConnection connection;


    ImgurContentManager() throws IOException
    {
        // Need to find a better way to handle this case
        if (!getImgurAuthenticationInfo())
        {
            throw new IOException();
        }
        else
        {
            url = new URL(IMGUR_API_URL + "album/A08qZ.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + secrets.getClientID());
        }
    }


    String getWallpaper()
    {
        BufferedReader imgurResponse;
        ImgurAlbum album;

        try
        {
            connection.connect();
            imgurResponse = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            album = gson.fromJson(imgurResponse, ImgurAlbum.class);
            imgurResponse.close();

            return album.getData().getLink();
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }


    private boolean getImgurAuthenticationInfo()
    {
        if (!authFile.toFile().exists())
        {
            System.err.println("Can't find authentication info");
            return false;
        }

        BufferedReader fileInput;

        try
        {
            fileInput = Files.newBufferedReader(authFile, StandardCharsets.UTF_8);
            this.secrets = gson.fromJson(fileInput, ImgurSecrets.class);
            fileInput.close();
            return true;
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
            return false;
        }
    }
}
