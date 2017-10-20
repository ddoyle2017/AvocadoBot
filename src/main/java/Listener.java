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
        //  For now, we don't want bot-to-bot communication. Check event object to see if the communication
        //  is from a bot. If so, return nothing.
        if (event.getAuthor().isBot()) return;

        // Remember that Discord communication is done via JSON files. We need to get the appropriate string from the
        // object that was created from the JSON file (the event class)
        Message message = event.getMessage();
        String content  = message.getRawContent();  // getRawContent is an atomic getter that keeps discord formatting

        // handle the command
        if (content.equals("!avocado"))
        {
            MessageChannel channel = event.getChannel(); // get which channel the message came from
            channel.sendMessage("HUGE ASSES").queue(); // add message response to the bot's action queue
        }
    }
}
