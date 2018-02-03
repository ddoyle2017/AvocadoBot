package Music;

import Resources.BotReply;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.List;


/**
 * MusicLoadResultHandler
 *
 * Handles the results of the TrackScheduler's actions on the tracks.
 */
@SuppressWarnings("FieldCanBeLocal")
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
        this.channel = channel;
        this.manager = manager;
        this.author  = author;
    }

    // called when a URL is provided
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

    // called when a keyphrase for a YouTube search is provided
    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {
        List<AudioTrack> tracks = playlist.getTracks();
        AudioTrack currentTrack = playlist.getSelectedTrack();
        int tracksInQueue = manager.getScheduler().getTracksInQueue();

        if (currentTrack == null)
        {
            currentTrack = tracks.get(0);
        }
        manager.getScheduler().queue(currentTrack);
        String queuePosition = (tracksInQueue <= 0) ? ("**Currently playing**") : Integer.toString(tracksInQueue);
        channel.sendMessage(getTrackInfo(currentTrack, queuePosition)).queue();
    }

    private MessageEmbed getTrackInfo(AudioTrack track, String queuePosition)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.DARK_GRAY);
        embedBuilder.setAuthor("Added to queue", null, author.getAvatarUrl());
        embedBuilder.setTitle(track.getInfo().title, track.getInfo().uri);
        embedBuilder.addField("Channel", track.getInfo().author, true);
        embedBuilder.addField("Duration", convertMSToTimeStamp(track.getInfo().length), true);
        embedBuilder.addField("Filler", "fill", true);
        embedBuilder.addField("Position in queue", queuePosition, true);
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
