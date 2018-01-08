package Music;

import Resources.BotReply;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeFormatInfo;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.Date;
import java.util.List;


/**
 * MusicLoadResultHandler
 *
 * - Handles the results of the TrackScheduler's actions on the tracks.
 */
public class MusicLoadResultHandler implements AudioLoadResultHandler
{
    private final MessageChannel channel;
    private final MusicManager   manager;
    private final User author;
    private final int SECONDS_IN_AN_HOUR = 3600;
    private final int SECONDS_IN_A_MINUTE = 60;
    private final int MINUTES_IN_AN_HOUR = 60;
    private final int MS_IN_A_SECOND = 1000;


    MusicLoadResultHandler(MusicManager manager, MessageChannel channel, User author)
    {
        this.channel  = channel;
        this.manager  = manager;
        this.author   = author;
    }

    @Override
    public void trackLoaded(AudioTrack track)
    {
        if (manager.getScheduler().getQueue().isEmpty())
        {
            channel.sendMessage("**Playing** :notes: `" + track.getInfo().title + "`").queue();
        }
        else
        {
            channel.sendMessage("**Added** `" + track.getInfo().title + "` **to queue** :musical_note:").queue();
        }
        manager.getScheduler().queue(track);
    }

    @Override
    public void loadFailed(FriendlyException ex)
    {
        channel.sendMessage(":x: **Could not play: " + ex.getMessage() + "**").queue();
    }

    @Override
    public void noMatches()
    {
        channel.sendMessage(BotReply.SONG_NOT_FOUND).queue();
    }

    // YouTube tracks are encapsulated in a playlist, so this method is the event listener for playing music found from YouTube queries
    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {
        List<AudioTrack> tracks = playlist.getTracks();
        AudioTrack currentTrack = playlist.getSelectedTrack();

        if (currentTrack == null)
        {
            currentTrack = tracks.get(0);
            channel.sendMessage(getTrackInfo(currentTrack, tracks.size())).queue();
        }
        manager.getScheduler().queue(currentTrack);
    }

    private MessageEmbed getTrackInfo(AudioTrack track, int queuePosition)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.DARK_GRAY);
        embedBuilder.setAuthor("Added to queue", null, author.getAvatarUrl());
        embedBuilder.setTitle(track.getInfo().title, track.getInfo().uri);
        embedBuilder.addField("Channel", track.getInfo().author, true);
        embedBuilder.addField("Duration", convertMSToTimeStamp(track.getInfo().length), true);
        embedBuilder.addField("Filler", "fill", true);
        embedBuilder.addField("Position in queue", Integer.toString(queuePosition), true);
        embedBuilder.setThumbnail(getYouTubeVideoThumbnail(track.getIdentifier()));

        return embedBuilder.build();
    }

    private String convertMSToTimeStamp(Long duration)
    {
        long durationInSeconds = duration / MS_IN_A_SECOND;
        long hours = durationInSeconds / SECONDS_IN_AN_HOUR;
        long minutes = (durationInSeconds / SECONDS_IN_A_MINUTE) % MINUTES_IN_AN_HOUR;
        long seconds = durationInSeconds % SECONDS_IN_A_MINUTE;

        String minString;
        String secString = (seconds < 10) ? ("0" + seconds) : Long.toString(seconds);

        if (hours > 0)
        {
            minString = (minutes < 10) ? ("0" + minutes) : Long.toString(minutes);
            return (hours + ":" + minString + ":" + secString);
        }
        else
        {
            minString = Long.toString(minutes);
            return (minString + ":" + secString);
        }
    }

    // from the YouTube API documentation
    private String getYouTubeVideoThumbnail(String videoID)
    {
        return "http://img.youtube.com/vi/" + videoID +"/0.jpg";
    }
}
