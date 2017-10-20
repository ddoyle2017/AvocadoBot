import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.StringTokenizer;

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
        String[] tokens = content.split("\\s");

<<<<<<< HEAD
<<<<<<< HEAD
        // handle the command
        if (tokens[0].equals("!avocado")||tokens[0].equals("!a"))
=======
=======
>>>>>>> 4b70d0a52ca15df40dbf2102f8ac9b6449a79b44
        if (content.equals("!avocado"))
>>>>>>> master
        {
            MessageChannel channel = event.getChannel(); // get which channel the message came from
            channel.sendMessage("HUGE ASSES").queue(); // add message response to the bot's action queue

            try {
                switch (tokens[1]) {
                    case "play":
                        channel.sendMessage("OPPAIIII~ ~ ~ ~ ~").queue(); // add message response to the bot's action queue
                        break;
                    case "hook":
                        channel.sendMessage("FRIED CHICKEN").queue(); // add message response to the bot's action queue
                        break;
                    default:
                        break;
                }
            }

            catch(IndexOutOfBoundsException e) {
                channel.sendMessage("Avocado needs your commands to grow <3\n").queue(); // sends error message if there is no commands added
            }//end catch
        }//end if
    }//end onMessageReceived
}//end class
