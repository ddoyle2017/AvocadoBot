import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.StringTokenizer;

/**
 * Listener Class
 *
 * -All event listeners go here. This class grabs any messages received via Discord, parses the strings, then
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
        MessageChannel channel = event.getChannel();

        if (tokens[0].equals("!avocado") || tokens[0].equals("!a"))
        {
            try
            {
                switch (tokens[1])
                {
                    case "play":
                        if (tokens.length < 3)
                        {
                            channel.sendMessage("I need a song name or YouTube link!").queue();
                        }
                        else
                        {
                            channel.sendMessage("Playing " + tokens[2]).queue();
                        }
                        break;
                    case "hook":
                        channel.sendMessage("Not implemented yet").queue();
                        break;
                    case "help":
                        channel.sendMessage("Command list:\n    * play [NAME | URL] - plays a song\n").queue();
                        break;
                    default:
                        channel.sendMessage("Invalid command. Try '!a help' for a list of commands").queue();
                        break;
                }
            }
            catch (IndexOutOfBoundsException ex)
            {
                channel.sendMessage("Please specify a command").queue();
            }
        }
    }
}
