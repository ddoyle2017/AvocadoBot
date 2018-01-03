package Music;

import Resources.BotReply;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MusicControls
{
    private AudioPlayerManager  playerManager;
    private MusicManager        musicManager;
    private MessageChannel      channel;
    private AudioManager        manager;
    private String              content;
    private VoiceChannel        voiceChannel;
    private static boolean      musicPlaying = false;
    private static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";


    MusicControls(MessageReceivedEvent event, AudioPlayerManager playerManager, MusicManager musicManager)
    {
        channel = event.getChannel();
        manager = event.getGuild().getAudioManager();
        content = event.getMessage().getContentDisplay();
        voiceChannel = event.getMember().getVoiceState().getChannel();

        this.playerManager = playerManager;
        this.musicManager  = musicManager;
    }

    void playSong()
    {
        String songQuery = content.substring(content.lastIndexOf("play") + 5, content.length()).trim();

        if (!isUrl(songQuery))
        {
            songQuery = buildYouTubeQuery(songQuery);
        }
        if (!musicPlaying)
        {
            joinVoiceChannel();
            musicPlaying = true;
        }
        playerManager.loadItemOrdered(musicManager, songQuery, new MusicLoadResultHandler(musicManager, channel));
    }

    void stopSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
        {
            channel.sendMessage(BotReply.NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(BotReply.SONG_STOPPED).queue();
            musicPlaying = false;
            musicManager.getPlayer().stopTrack();
            leaveVoiceChannel();
        }
    }

    void skipSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
        {
            channel.sendMessage(BotReply.NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(BotReply.SONG_SKIPPED).queue();
            musicManager.getScheduler().nextTrack();
        }
    }

    void pauseSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(BotReply.MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
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

    void resumeSong()
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

    void joinVoiceChannel()
    {
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

    void leaveVoiceChannel()
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

    private boolean isUrl(String songQuery)
    {
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(songQuery);
        return matcher.find();
    }

    private String buildYouTubeQuery(String keyphrase)
    {
        return ("ytsearch:" + keyphrase);
    }

    private boolean isAudioConnected()
    {
        return (manager.getConnectionStatus() == ConnectionStatus.CONNECTED);
    }

    boolean isMusicPlaying()
    {
        return musicPlaying;
    }
}
