import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;


/**
 * GeneralListener Class
 *
 *  - An event listener for AvocadoBot. Handles all "general" commands to the bot that aren't specific to a feature, e.g. providing a command
 *    list or checking to see if a command was properly given.
 *
 */
public class GeneralListener extends ListenerAdapter
{

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        Message         message = event.getMessage();
        String          content = message.getContentDisplay();
        MessageChannel  channel = event.getChannel();


        if (content.equals("!avocado") || content.equals("!a"))
        {
            channel.sendMessage(":x: **I need a command!**").queue();
        }
        else if (content.equals("!avocado help") || content.equals("!a help") || content.equals("!avocado commands") || content.equals("!a commands"))
        {
            channel.sendMessage(":white_check_mark: **Here is the command list**").queue();
            channel.sendMessage(getCommandList()).queue();
        }
        else if (content.equals("!avocado whoami") || content.equals("!a whoami"))
        {
            channel.sendMessage(getBotInfo()).queue();
        }
    }

    private MessageEmbed getCommandList()
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(java.awt.Color.GREEN);
        embedBuilder.addField("!a play [URL | Query]", "Give AvocadoBot a song or YouTube link to play.\n", false);
        embedBuilder.addField("!a stop", "Stops the current song\n", false);
        embedBuilder.addField("!a pause", "Pauses the current song\n", false);
        embedBuilder.addField("!a resume", "Continues playing a paused song\n", false);
        embedBuilder.addField("!a join", "AvocadoBot will join your voice channel.\n", false);
        embedBuilder.addField("!a leave", "Disconnects AvocadoBot from your voice channel.\n", false);
        embedBuilder.addField("!a hook [URL]", "Creates a webhook for updates from the given link.\n", false);
        embedBuilder.addField("!a help", "Provides a list of commands.\n", false);
        embedBuilder.addField("!a whoami", "Information about AvocadoBot.", false);

        return embedBuilder.build();
    }

    private MessageEmbed getBotInfo()
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField(":avocado: **AvocadoBot**\n", "I'm a general purpose bot for Discord!\n",false);
        embedBuilder.addField("Info:", ":keyboard:**Developer:** W O L F:first_quarter_moon_with_face:#6218", false);

        return embedBuilder.build();
    }
}
