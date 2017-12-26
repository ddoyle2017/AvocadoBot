import javafx.scene.paint.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
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
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        Message message = event.getMessage();
        String content = message.getContentDisplay();
        MessageChannel channel = event.getChannel();


        if (content.equals("!avocado") || content.equals("!a"))
        {
            channel.sendMessage(":x: **I need a command!**").queue();
        }
        else if (content.equals("!avocado help") || content.equals("!a help") || content.equals("!avocado commands") || content.equals("!a commands"))
        {
            channel.sendMessage(":white_check_mark: **Here is the command list**").queue();
            channel.sendMessage(getCommandList()).queue();
        }
        else if (content.startsWith("!avocado play") || content.startsWith("!a play"))
        {
            channel.sendMessage(":notes: **Playing music**").queue();

            //
            // Here is where we start working with LavaPlayer
            //
        }
    }

    private MessageEmbed getCommandList()
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(java.awt.Color.GREEN);
        embedBuilder.addField("!a play [URL | Query]", "Give AvocadoBot a song or YouTube link to play\n", false);
        embedBuilder.addField("!a help", "Provides a list of commands\n", false);
        embedBuilder.addField("!a hook [URL]", "Creates a webhook for updates from the given link", false);
        return embedBuilder.build();
    }
}
