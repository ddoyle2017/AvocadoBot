import Music.MusicListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * AvocadoBot Class
 *
 *  - This is where the initialization of the bot takes place. We create an instance of the SettingsManager class, get
 *    the Discord Bot authentication information, then logs into Discord's servers.
 *
 *  - Afterwards, we attach a GeneralListener object to the bot so that it can listen to text channels in the server
 *    for commands
 *
 */
public class AvocadoBot extends ListenerAdapter
{
    private static JDA api;


    public static void main (String[] args)
    {
       if (!System.getProperty("file.encoding").equals("UTF-8")) return;

       setUpBot();
    }

    private static void setUpBot()
    {
        try
        {
            Settings settings = SettingsManager.getInstance().getSettings();

            api = new JDABuilder(AccountType.BOT)
                    .setToken(settings.getBotToken())
                    .buildAsync();

            api.addEventListener(new GeneralListener());
            api.addEventListener(new MusicListener());
        }
        catch (LoginException | RateLimitedException | NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }

    public static JDA getAPI ()
    {
        return api;
    }
}
