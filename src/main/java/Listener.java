import Music.AudioPlayerSendHandler;
import Music.MusicLoadResultHandler;
import Music.MusicManager;
import Music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * Listener Class
 *
 * - All event listeners go here. This class grabs any messages received via Discord, parses the strings, then
 *   handles the commands.
 *
 */
public class Listener extends ListenerAdapter
{
    private AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private MusicManager       musicManager  = new MusicManager(playerManager);

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        Message             message = event.getMessage();
        String              content = message.getContentDisplay();
        MessageChannel      channel = event.getChannel();
        AudioManager        manager = event.getGuild().getAudioManager();

        manager.setSendingHandler(musicManager.getSendHandler());
        AudioSourceManagers.registerRemoteSources(playerManager);


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
            String trackUrl = content.substring(content.lastIndexOf("play")+5, content.length()).trim();
            joinVoiceChannel(event, channel);
            playerManager.loadItemOrdered(musicManager, trackUrl, new MusicLoadResultHandler(musicManager, channel));
        }
        else if (content.equals("!avocado join") || content.equals("!a join"))
        {
            joinVoiceChannel(event, channel);
        }
        else if (content.equals("!avocado leave") || content.equals("!a leave"))
        {
            leaveVoiceChannel(event, channel);
        }
    }

    private MessageEmbed getCommandList()
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(java.awt.Color.GREEN);
        embedBuilder.addField("!a play [URL | Query]", "Give AvocadoBot a song or YouTube link to play.\n", false);
        embedBuilder.addField("!a join", "AvocadoBot will join your voice channel.\n", false);
        embedBuilder.addField("!a leave", "Disconnects AvocadoBot from your voice channel.\n", false);
        embedBuilder.addField("!a help", "Provides a list of commands.\n", false);
        embedBuilder.addField("!a hook [URL]", "Creates a webhook for updates from the given link.", false);
        return embedBuilder.build();
    }

    private void joinVoiceChannel(MessageReceivedEvent event, MessageChannel channel)
    {
        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
        AudioManager manager = event.getGuild().getAudioManager(); // a Discord server is called a "Guild"

        if (voiceChannel == null)
        {
            channel.sendMessage(":x: **You have to be in a voice channel to use this command.**").queue();
        }
        else if (manager.getConnectionStatus() != ConnectionStatus.CONNECTED)
        {
            manager.openAudioConnection(voiceChannel);
            channel.sendMessage(":ok_hand: **Joined** `" + voiceChannel.getName() + "`").queue();
        }
    }

    private void leaveVoiceChannel(MessageReceivedEvent event, MessageChannel channel)
    {
        AudioManager manager = event.getGuild().getAudioManager();

        if (manager.getConnectionStatus() == ConnectionStatus.CONNECTED)
        {
            manager.closeAudioConnection();
            channel.sendMessage(":last_quarter_moon_with_face: **Successfully disconnected.**").queue();
        }
        else
        {
            channel.sendMessage(":x: **I am not connected to a voice channel.**").queue();
        }
    }
}
