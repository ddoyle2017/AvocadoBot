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
    private Gallery gallery;


    ImgurContentManager() throws IOException
    {
        if (!getAuthenticationInfo())
        {
            throw new IOException();
        }
    }


    public Gallery getWallpaperGallery()
    {
        BufferedReader imgurResponse;
        URL url;
        HttpURLConnection connection;

        if (gallery != null)
        {
            return gallery;
        }

        try
        {
            url = new URL(IMGUR_API_URL + GRAB_NEWEST_SLASHW_ALBUM + AS_JSON);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + secrets.getClientID());
            connection.connect();
            imgurResponse = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            gallery = gson.fromJson(imgurResponse, Gallery.class);
            imgurResponse.close();

            return gallery;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    private boolean getAuthenticationInfo()
    {
        if (!authFile.toFile().exists())
        {
            System.err.println(CANT_FIND_AUTH_FILE);
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
            ex.printStackTrace();
            return false;
        }
    }
}
