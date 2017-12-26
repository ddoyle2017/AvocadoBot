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
 *      - The TrackScheduler class acts as the Scheduler for the music track queue (recall Active Object design pattern). This class pairs together
 *        an audio player with a blocking queue, then manages it. The class also implements the AudioEventAdapter interface, which provides
 *        abstract methods for audio Event Listeners. Events like pausing a song, resuming it, starting a song, exception handling, and a song
 *        stream getting stuck are all handled here.
 *
 */
public class TrackScheduler extends AudioEventAdapter
{
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;      // A blocking queue blocks adding items to the queue if its full, and blocks the removal
                                                        // of items when the queue is empty.


    public TrackScheduler(AudioPlayer audioPlayer)
    {
        this.player = audioPlayer;
        this.queue  = new LinkedBlockingQueue<>();
    }

    // If queue is empty, play track ASAP. Else, add the track to the queue
    public void queue(AudioTrack track)
    {
        if (!player.startTrack(track, true))
        {
            queue.offer(track);
        }
    }

    // Force next track to play
    public void nextTrack(AudioTrack track)
    {
        player.startTrack(queue.poll(), false);     // nointerrupt is false here because we want to force the next track to play
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
            nextTrack(track);
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
