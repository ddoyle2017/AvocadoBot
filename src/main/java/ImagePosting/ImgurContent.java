package ImagePosting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static Resources.ImgurValues.*;


@SuppressWarnings("FieldCanBeLocal")
class ImgurContent
{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path authFile = new File(".").toPath().resolve("imgur.json");
    private ImgurSecrets secrets;
    private URL url;
    private HttpURLConnection connection;


    ImgurContent() throws IOException
    {
        // Need to find a better way to handle this case
        if (!getImgurAuthenticationInfo())
        {
            throw new IOException();
        }
        else
        {
            System.out.println("Loaded Imgur connection info");
            url = new URL(IMGUR_API_URL + GET_SLASHW_ALBUMS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", secrets.getClientID() + secrets.getClientSecret());
        }
    }

    // refactor this to return the image being grabbed
    void getWallpaper()
    {
        try
        {
            connection.connect();
            System.out.println("Successfully connected with Imgur servers");
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
            return;
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
