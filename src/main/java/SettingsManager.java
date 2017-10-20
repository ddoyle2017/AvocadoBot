/**
 * SettingsManager Class
 *
 * - This class acts as an interface between the SettingsClass and the rest of the project. All changes to the settings
 *   of the bot are done through this class, which then interacts with the actual settings class.
 *
 *
 * This code is based on Smbarbour's RavenBot, which can be found at:
 * https://github.com/MCUpdater/RavenBot/blob/master/src/main/java/org/mcupdater/ravenbot/SettingsManager.java
 */

public class SettingsManager
{
    private static SettingsManager instance;

    // this getter implements the Singleton design pattern
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
    }

    public SettingsManager() {
        // instantiate instance of the Interface here
    }
}
