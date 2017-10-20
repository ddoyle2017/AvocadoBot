import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Listener Class
 *
 * All event listeners go here. This class grabs any messages received via Discord, parses the strings, then
 * handles the commands.
 *
 */
public class Listener extends ListenerAdapter
{
    @Override
    public void onMessageReceived (MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;        //  is from a bot. If so, return nothing.

        Message message = event.getMessage();
        String content  = message.getRawContent();  // getRawContent is an atomic getter that keeps discord formatting

        if (content.equals("!avocado"))
        {
            MessageChannel channel = event.getChannel(); // get which channel the message came from
            channel.sendMessage("HUGE ASSES").queue(); // add message response to the bot's action queue
        }
    }
}
