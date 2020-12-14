import constants.Commands;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import static constants.BotReplies.*;

/**
 * An event listener for AvocadoBot. Handles all "general" commands to the bot that aren't specific to a feature, e.g. providing a command
 * list or checking to see if a command was properly given.
 */
public class GeneralCommandListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(final MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        final Message message = event.getMessage();
        final String content = message.getContentDisplay();
        final MessageChannel channel = event.getChannel();


        if (content.equals(Commands.LONG_COMMAND) || content.equals(Commands.SHORT_COMMAND)) {
            channel.sendMessage(MISSING_COMMAND).queue();
        }
        else if (content.equals(Commands.HELP_LONG) || content.equals(Commands.HELP_SHORT)) {
            channel.sendMessage(":white_check_mark: **Here is the command list**").queue();
            channel.sendMessage(getCommandList()).queue();
        }
        else if (content.equals(Commands.WHO_AM_I_LONG) || content.equals(Commands.WHO_AM_I_SHORT)) {
            channel.sendMessage(getBotInfo()).queue();
        }
    }

    private MessageEmbed getCommandList()
    {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(java.awt.Color.GREEN);
        embedBuilder.addField("!a play [URL | Query]", "Give AvocadoBot a song or YouTube link to play.\n", false);
        embedBuilder.addField("!a stop", "Stops the current song\n", false);
        embedBuilder.addField("!a pause", "Pauses the current song\n", false);
        embedBuilder.addField("!a resume", "Continues playing a paused song\n", false);
        embedBuilder.addField("!a skip", "Skips to the next song in the queue\n", false);
        embedBuilder.addField("!a np", "Shows what song is currently playing\n", false);
        embedBuilder.addField("!a join", "AvocadoBot will join your voice channel.\n", false);
        embedBuilder.addField("!a leave", "Disconnects AvocadoBot from your voice channel.\n", false);
        embedBuilder.addField("!a wallpaper", "Retrieves the 3 newest posts from the r/slashw gallery.\n", false);
        embedBuilder.addField("!a imgur [Query]", "Retrieves an Imgur album found with the given query\n", false);
        embedBuilder.addField("!a help", "Provides a list of commands.\n", false);
        embedBuilder.addField("!a whoami", "Information about AvocadoBot.", false);

        return embedBuilder.build();
    }

    private MessageEmbed getBotInfo()
    {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField(":avocado: **AvocadoBot**\n", "I'm a general purpose bot for Discord!\n",false);
        embedBuilder.addField("Info:", ":keyboard:**Developer:** W O L F:first_quarter_moon_with_face:#6218", false);

        return embedBuilder.build();
    }
}
