package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import javafx.scene.media.AudioTrack;

/**
 *  TrackScheduler Class
 *
 *  Event handler class for the AudioPlayer object. Handles user interactions like Play, Pause, Skip, etc.,
 *  as well as keeping track of the song queue.
 *
 */
public class TrackScheduler extends AudioEventAdapter
{
    @Override
    public void onPlayerPause(AudioPlayer player)
    {

    }

    @Override
    public void onPlayerResume(AudioPlayer player)
    {

    }

    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {

    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            // play next track
        }
    }

    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException ex)
    {

    }

    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
    {

    }
}
