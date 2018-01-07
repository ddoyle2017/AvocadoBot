package Music;

import Resources.BotReply;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

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
    private final int SECONDS_IN_AN_HOUR = 3600;
    private final int SECONDS_IN_A_MINUTE = 60;
    private final int MS_IN_A_SECOND = 1000;


    MusicLoadResultHandler(MusicManager manager, MessageChannel channel)
    {
        this.channel  = channel;
        this.manager  = manager;
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

        if (currentTrack == null)   // If no track is currently selected, grab the first one from the queue
        {
            currentTrack = tracks.get(0);
            channel.sendMessage(getTrackInfo(currentTrack)).queue();
        }
        manager.getScheduler().queue(currentTrack);
    }

    private MessageEmbed getTrackInfo(AudioTrack track)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GRAY);
        //embedBuilder.setTitle(track.getInfo().title, track.getIdentifier());
        embedBuilder.addField("Duration", convertMSToTimeStamp(track.getInfo().length), true);

        return embedBuilder.build();
    }

    private String convertMSToTimeStamp(Long duration)
    {
        long durationInSeconds = duration / MS_IN_A_SECOND;

        long hours = durationInSeconds / SECONDS_IN_AN_HOUR;
        long minutes = (durationInSeconds / SECONDS_IN_A_MINUTE) % 60;
        long seconds = durationInSeconds % 60;

        String minString;
        String secString;

        if (seconds < 10)
        {
            secString = "0" + seconds;
        }
        else
        {
            secString = Long.toString(seconds);
        }

        if (hours > 0)
        {
            if (minutes < 10)
            {
                minString = "0" + minutes;
            }
            else
            {
                minString = Long.toString(minutes);
            }
            return (hours + ":" + minString + ":" + secString);
        }
        else
        {
            minString = Long.toString(minutes);
            return (minString + ":" + secString);
        }
    }
}
