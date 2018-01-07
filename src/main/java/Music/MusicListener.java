package Music;
import Resources.BotReply;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;


/**
 * MusicListener Class
 *
 * - An event listener for all bot commands relevant to music playing and audio. Calls the necessary
 *   functions to execute the given commands.
 */
public class MusicListener extends ListenerAdapter
{
    private AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private MusicManager       musicManager  = new MusicManager(playerManager);


    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        MessageChannel  channel = event.getChannel();
        MusicControls   musicControls = new MusicControls(event, playerManager, musicManager);
        String          content = event.getMessage().getContentDisplay();

        event.getGuild().getAudioManager().setSendingHandler(musicManager.getSendHandler());
        AudioSourceManagers.registerRemoteSources(playerManager);


        if (content.equals("!avocado play") || content.equals("!a play"))
        {
            if (!musicControls.isMusicPlaying())
            {
                channel.sendMessage(BotReply.MISSING_SONG).queue();
            }
            else
            {
                musicControls.resumeSong();
            }
        }
        else if (content.startsWith("!avocado play") || content.startsWith("!a play"))
        {
            musicControls.playSong();
        }
        else if (content.equals("!avocado stop") || content.equals("!a stop"))
        {
            musicControls.stopSong();
        }
        else if (content.equals("!avocado pause") || content.equals("!a pause"))
        {
            musicControls.pauseSong();
        }
        else if (content.equals("!avocado resume") || content.equals("!a resume"))
        {
            musicControls.resumeSong();
        }
        else if (content.equals("!avocado skip") || content.equals("!a skip") || content.equals("!avocado next") || content.equals("!a next"))
        {
            musicControls.skipSong();
        }
        else if (content.equals("!avocado join") || content.equals("!a join"))
        {
            musicControls.joinVoiceChannel();
        }
        else if (content.equals("!avocado leave") || content.equals("!a leave"))
        {
            musicControls.leaveVoiceChannel();
        }
    }
}
