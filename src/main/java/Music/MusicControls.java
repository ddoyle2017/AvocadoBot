package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;

import static Resources.BotReply.*;


public class MusicControls
{
    private AudioPlayerManager  playerManager;
    private MusicManager        musicManager;
    private MessageChannel      channel;
    private AudioManager        manager;
    private String              content;
    private User                author;
    private VoiceChannel        voiceChannel;
    private static boolean      musicPlaying = false;
    private static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";


    MusicControls(MessageReceivedEvent event, AudioPlayerManager playerManager, MusicManager musicManager)
    {
        channel = event.getChannel();
        manager = event.getGuild().getAudioManager();
        content = event.getMessage().getContentDisplay();
        author  = event.getAuthor();
        voiceChannel = event.getMember().getVoiceState().getChannel();
        this.playerManager = playerManager;
        this.musicManager  = musicManager;
    }


    void playSong(User author)
    {
        String songQuery = content.substring(content.lastIndexOf("play") + 5, content.length()).trim();

        if (!musicPlaying)
        {
            if (!joinVoiceChannel()) return;
            musicPlaying = true;
        }
        if (!isUrl(songQuery))
        {
            channel.sendMessage("**Searching for :mag_right:** `" + songQuery + "`").queue();
            songQuery = buildYouTubeQuery(songQuery);
        }
        playerManager.loadItemOrdered(musicManager, songQuery, new MusicLoadResultHandler(musicManager, channel, author));
    }


    void stopSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
        {
            channel.sendMessage(NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(SONG_STOPPED).queue();
            musicPlaying = false;
            musicManager.getPlayer().stopTrack();
            leaveVoiceChannel();
        }
    }


    void skipSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
        {
            channel.sendMessage(NOTHING_IS_PLAYING).queue();
        }
        else
        {
            channel.sendMessage(SONG_SKIPPED).queue();
            musicManager.getScheduler().nextTrack();
        }
    }


    void pauseSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(MISSING_VOICE_CHANNEL).queue();
        }
        else if (!musicPlaying)
        {
            channel.sendMessage(NOTHING_IS_PLAYING).queue();
        }
        else if (musicManager.getPlayer().isPaused())
        {
            channel.sendMessage(SONG_ALREADY_PAUSED).queue();
        }
        else
        {
            channel.sendMessage(SONG_PAUSED).queue();
            musicManager.getPlayer().setPaused(true);
        }
    }


    void resumeSong()
    {
        if (!isAudioConnected())
        {
            channel.sendMessage(MISSING_VOICE_CHANNEL).queue();
        }
        else if (musicManager.getPlayer().isPaused())
        {
            channel.sendMessage(SONG_RESUMED).queue();
            musicManager.getPlayer().setPaused(false);
        }
        else
        {
            channel.sendMessage(SONG_NOT_PAUSED).queue();
        }
    }


    boolean joinVoiceChannel()
    {
        if (!isAudioConnected() && voiceChannel != null)
        {
            manager.openAudioConnection(voiceChannel);
            channel.sendMessage(":ok_hand: **Joined** `" + voiceChannel.getName() + "`").queue();
            return true;
        }
        else
        {
            channel.sendMessage(MISSING_VOICE_CHANNEL).queue();
            return false;
        }
    }


    void leaveVoiceChannel()
    {
        if (isAudioConnected())
        {
            manager.closeAudioConnection();
            channel.sendMessage(VOICE_CHANNEL_DISCONNECT).queue();
        }
        else
        {
            channel.sendMessage(NOT_IN_VOICE_CHANNEL).queue();
        }
    }


    void nowPlaying()
    {
        if (isMusicPlaying())
        {
            AudioTrack track = musicManager.getScheduler().getCurrentTrack();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.BLUE);
            embedBuilder.setAuthor("Now Playing ♪", null, author.getAvatarUrl());
            embedBuilder.setTitle(track.getInfo().title, track.getInfo().uri);
            embedBuilder.setThumbnail("http://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg");

            channel.sendMessage(embedBuilder.build()).queue();
        }
    }


    boolean isMusicPlaying()
    {
        return musicPlaying;
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
}
