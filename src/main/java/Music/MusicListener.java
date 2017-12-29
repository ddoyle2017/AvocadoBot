package Music;
import Resources.BotReply;

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
 *  - An event listener for all bot commands relevant to music playing and audio.
 *
 */
public class MusicListener extends ListenerAdapter
{
    private AudioPlayerManager playerManager  = new DefaultAudioPlayerManager();
    private MusicManager       musicManager   = new MusicManager(playerManager);
    private boolean            isMusicPlaying = false;
    private AudioManager       manager;
    private MessageChannel     channel;


    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        String content = event.getMessage().getContentDisplay();
        channel = event.getChannel();
        manager = event.getGuild().getAudioManager();
        manager.setSendingHandler(musicManager.getSendHandler());
        AudioSourceManagers.registerRemoteSources(playerManager);


        if (content.equals("!avocado") || content.equals("!a"))
        {
            channel.sendMessage(BotReply.MISSING_COMMAND).queue();
        }
        else if (content.equals("!avocado play") || content.equals("!a play"))
        {
            if (!isMusicPlaying)
            {
                channel.sendMessage(BotReply.MISSING_SONG).queue();
            }
            else
            {
                resumeSong();
            }
        }
        else if (content.startsWith("!avocado play") || content.startsWith("!a play"))
        {
            playSong(event, content);
        }
        else if (content.equals("!avocado stop") || content.equals("!a stop"))
        {
            stopSong();
        }
        else if (content.equals("!avocado pause") || content.equals("!a pause"))
        {
            pauseSong();
        }
        else if (content.equals("!avocado resume") || content.equals("!a resume"))
        {
            resumeSong();
        }
        else if (content.equals("!avocado skip") || content.equals("!a skip") || content.equals("!avocado next") || content.equals("!a next"))
        {
            skipSong();
        }
        else if (content.equals("!avocado join") || content.equals("!a join"))
        {
            joinVoiceChannel(event);
        }
        else if (content.equals("!avocado leave") || content.equals("!a leave"))
        {
            leaveVoiceChannel();
        }
    }

//=================================================================================================================================================//
//  Music Control Methods                                                                                                                          //
//=================================================================================================================================================//
    private void playSong(MessageReceivedEvent event, String content)
    {
        String trackUrl = content.substring(content.lastIndexOf("play") + 5, content.length()).trim();

        if (!isMusicPlaying)
        {
            joinVoiceChannel(event);
            isMusicPlaying = true;
        }
        playerManager.loadItemOrdered(musicManager, trackUrl, new MusicLoadResultHandler(musicManager, channel));
    }

    private void stopSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!isMusicPlaying)
        {
            channel.sendMessage(BotReply.NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(BotReply.SONG_STOPPED).queue();
            isMusicPlaying = false;
            musicManager.getPlayer().stopTrack();
            leaveVoiceChannel();
        }
    }

    private void skipSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!isMusicPlaying)
        {
            channel.sendMessage(BotReply.NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(BotReply.SONG_SKIPPED).queue();
            musicManager.getScheduler().nextTrack();
        }
    }

    private void pauseSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!isMusicPlaying)
        {
            channel.sendMessage(BotReply.NOTHING_IS_PLAYING).queue();
        }
        else if (musicManager.getPlayer().isPaused())
        {
            channel.sendMessage(BotReply.SONG_ALREADY_PAUSED).queue();
        }
        else
        {
            channel.sendMessage(BotReply.SONG_PAUSED).queue();
            musicManager.getPlayer().setPaused(true);
        }
    }

    private void resumeSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (musicManager.getPlayer().isPaused())
        {
            channel.sendMessage(BotReply.SONG_RESUMED).queue();
            musicManager.getPlayer().setPaused(false);
        }
        else
        {
            channel.sendMessage(BotReply.SONG_NOT_PAUSED).queue();
        }
    }

//=================================================================================================================================================//
//  Helper Functions                                                                                                                               //
//=================================================================================================================================================//
    private void joinVoiceChannel(MessageReceivedEvent event)
    {
        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();

        if (voiceChannel == null)
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!isAudioConnected())
        {
            manager.openAudioConnection(voiceChannel);
            channel.sendMessage(":ok_hand: **Joined** `" + voiceChannel.getName() + "`").queue();
        }
    }

    private void leaveVoiceChannel()
    {
        if (isAudioConnected())
        {
            manager.closeAudioConnection();
            channel.sendMessage(BotReply.VOICE_CHANNEL_DISCONNECT).queue();
        }
        else
        {
            channel.sendMessage(BotReply.NOT_IN_VOICE_CHANNEL).queue();
        }
    }

    private boolean isAudioConnected()
    {
        return (manager.getConnectionStatus() == ConnectionStatus.CONNECTED);
    }
}
