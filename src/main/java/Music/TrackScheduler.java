package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 *  TrackScheduler Class
 *
 *  Event handler class for the AudioPlayer object. Handles user interactions like Play, Pause, Skip, etc.,
 *  as well as keeping track of the song queue.
 *
 */
public class TrackScheduler extends AudioEventAdapter
{
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;


    public TrackScheduler(AudioPlayer audioPlayer)
    {
        this.player = audioPlayer;
        this.queue  = new LinkedBlockingQueue<>();
    }

    public void queue (AudioTrack track)
    {
        if (player.startTrack(track, true))
        {
            queue.offer(track);
        }
    }


    @Override
    public void onPlayerPause(AudioPlayer player)
    {

    }

    @Override
    public void onPlayerResume(AudioPlayer player)
    {

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            // play next track
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException ex)
    {

    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
    {

    }
}
