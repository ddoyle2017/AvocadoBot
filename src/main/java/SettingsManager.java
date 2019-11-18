import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * SettingsManager Class
 *
 *  This code is based on Smbarbour's RavenBot, which can be found at:
 *  https://github.com/MCUpdater/RavenBot/blob/master/src/main/java/org/mcupdater/ravenbot/SettingsManager.java
 */
public class SettingsManager
{
    private static      SettingsManager instance;
    private final Gson  gson = new GsonBuilder().setPrettyPrinting().create();
    private Settings    settings;
    private final Path  authFile = new File(".").toPath().resolve("auth.json");


    // If a SettingsManager object exists, return it. If not, create one.
    public static SettingsManager getInstance()
    {
        if (instance == null)
        {
            instance = new SettingsManager();
        }
        return instance;
    }

    /**
     *
     */
    private SettingsManager()
    {
        if (!authFile.toFile().exists())
        {
            System.out.println("cannot find authentication info");
        }
        else
        {
            loadSettings();
        }
    }

    // Read in authentication info from the JSON file (using GSON) and store info into the
    // appropriate Settings object variables

    /**
     *
     */
    private void loadSettings()
    {
        BufferedReader fileInput;

        try
        {
            fileInput = Files.newBufferedReader(authFile, StandardCharsets.UTF_8);
            this.settings = gson.fromJson(fileInput, Settings.class);
            fileInput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Settings getSettings()
    {
        return settings;
    }
}
