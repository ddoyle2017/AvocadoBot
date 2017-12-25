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
    public void onMessageReceived(MessageReceivedEvent event)
    {
        // block other bots from giving AvocadoBot commands
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getRawContent();  // getRawContent is an atomic getter that keeps discord formatting
        String[] tokens = content.split("\\s");

        if (tokens[0].equals("!avocado") || tokens[0].equals("!a"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Hi, I joined this channel").queue();

            try {
                switch (tokens[1])
                {
                    case "play":
                        if (tokens[2] != null)
                        {
                            channel.sendMessage("Playing " + tokens[2]).queue();
                        }
                        else
                        {
                            channel.sendMessage("I need a song name or YouTube link!").queue();
                        }
                        break;
                    case "hook":
                        channel.sendMessage("Not implemented yet").queue();
                        break;
                    case "help":
                        channel.sendMessage("Command list:\n play [NAME | URL] - plays a song\n").queue();
                    default:
                        break;
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                channel.sendMessage("I need a command, type '!a help' for a list of commands").queue();
            }
        }
    }
}
