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
import java.util.Arrays;
import java.util.List;

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
            url = new URL(IMGUR_API_URL + SEARCH_IMGUR + "anime");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Client-ID " + secrets.getClientID());
        }
    }

    // refactor this to return the image being grabbed
    String getWallpaper()
    {
        StringBuilder builder;
        BufferedReader imgurResponse;

        try
        {
            connection.connect();
            builder = new StringBuilder();
            imgurResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = imgurResponse.readLine()) != null)
            {
                builder.append(line);
                System.out.println(line);
            }
            imgurResponse.close();
            System.out.println(builder.toString());

            /*----------------*/
            /* code to remove */
            /*----------------*/
            String split = builder.toString();
            split = split.replaceAll("[^A-Za-z0-9 ]", " ");
            String ID;
            String baseURL = "http://i.imgur.com/";
            List<String> items = Arrays.asList(split.split("\\s+"));
            ID = items.get(items.indexOf("id") + 1);
            String adrs = baseURL + ID;
            if (adrs.equalsIgnoreCase(baseURL) || ID == null) {
                return "No image found";
            }
            System.out.println(adrs);
            System.out.println(url.toString());
            return adrs;
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
