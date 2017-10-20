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


public class AvocadoBot extends ListenerAdapter
{
    private static JDA api;

    public static void main (String[] args) throws LoginException, RateLimitedException
    {
        api = new JDABuilder(AccountType.BOT)
                .setToken("REDACTED")
                .buildAsync();

        api.addEventListener(new Listener()); // link an instance of the Listener class to the bot
    }

    public static JDA getAPI ()
    {
        return api;
    }
}
