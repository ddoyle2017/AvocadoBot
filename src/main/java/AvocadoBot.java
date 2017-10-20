/*
 * AvocadoBot Main Class
 *
 * Handles the instantiation of the bot through the JDA API, including
 * the bot's login information and token.
 *
 */
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;


public class AvocadoBot extends ListenerAdapter
{
    private static JDA api;

    public static void main (String[] args) throws InterruptedException, UnsupportedEncodingException
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

            api.addEventListener(new Listener());
        }
        catch (LoginException e) // problems with the bot account logging into Discord.
        {
            e.printStackTrace();
        }
        catch (RateLimitedException e) // too many user HTTP requests in a given time frame
        {
            e.printStackTrace();
        }
    }

    public static JDA getAPI ()
    {
        return api;
    }
}
