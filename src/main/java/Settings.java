/**
 * Settings Class
 *
 *  - This class stores all login, authentication, and configuration data relevant to the instance of the bot it is
 *    attached to.
 *
 *
 *  This code is based on Smbarbour's RavenBot, which can be found at:
 *  https://github.com/MCUpdater/RavenBot/blob/master/src/main/java/org/mcupdater/ravenbot/Settings.java
 */

public class Settings
{
    private String botToken;

    public void setBotToken(String token)
    {
        this.botToken = token;
    }

    public String getBotToken()
    {
        return botToken;
    }
}
