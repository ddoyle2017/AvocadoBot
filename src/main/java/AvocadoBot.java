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

            api.addEventListener(new Listener());
        }
        catch (LoginException | RateLimitedException | NullPointerException ex) // problems with the bot account logging into Discord.
        {
            ex.printStackTrace();
        }
    }

    public static JDA getAPI ()
    {
        return api;
    }
}
