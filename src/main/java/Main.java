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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;


public class Main extends ListenerAdapter
{
    public static void main (String[] args) throws LoginException, RateLimitedException
    {
        JDA API = new JDABuilder(AccountType.BOT)
                .setToken("MzY5NzI2NzUwMTM5MDg4ODk3.DMculg.pJQSj_5-0LJS6-6Wu137qFNaK9c")
                .buildAsync();

    }

    //  Event listener
    @Override
    public void onMessageReceived (MessageReceivedEvent event)
    {
        System.out.println("I received a message in my butt");
    }
}
