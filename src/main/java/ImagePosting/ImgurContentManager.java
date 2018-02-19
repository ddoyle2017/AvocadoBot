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


    ImgurContentManager() throws IOException
    {
        if (!getAuthenticationInfo())
        {
            throw new IOException();
        }
    }


    public Gallery getImgurGallery(String imgurQuery)
    {
        HttpURLConnection connection;
        BufferedReader imgurResponse;
        Gallery queryResult;

        try
        {
            URL url = new URL(imgurQuery);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + secrets.getClientID());
            connection.connect();

            imgurResponse = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            queryResult = gson.fromJson(imgurResponse, Gallery.class);
            imgurResponse.close();

            return queryResult;
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
