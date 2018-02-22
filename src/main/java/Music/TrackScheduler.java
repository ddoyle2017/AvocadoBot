package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * TrackScheduler Class
 *
 * The TrackScheduler class acts as the Scheduler for the music track queue (recall Active Object design pattern). This class pairs
 * together an audio player with a blocking queue, then manages it. The class also implements the AudioEventAdapter interface, which
 * provides abstract methods for audio Event Listeners.
 *
 * The results of events like pausing a song, resuming it, starting a song, exception handling,and a song stream getting stuck are all
 * handled here.
 *
 */
public class TrackScheduler extends AudioEventAdapter
{
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private int tracksInQueue;
    private AudioTrack currentTrack;


    public TrackScheduler(AudioPlayer audioPlayer)
    {
        this.player = audioPlayer;
        this.queue  = new LinkedBlockingQueue<>();
        tracksInQueue = 0;
    }


    public boolean queue(AudioTrack track)
    {
        tracksInQueue++;
        if (!player.startTrack(track, true) && track != null)
        {
            if (queue.offer(track))
            {
                System.out.println("Playing " + track.getInfo().title);
                return true;
            }
        }
        return false;
    }


    public boolean nextTrack() // force next track to play
    {
        return player.startTrack(queue.poll(), false);
    }


    public BlockingQueue<AudioTrack> getQueue()
    {
        return queue;
    }


    public int getTracksInQueue()
    {
        return tracksInQueue;
    }


    public AudioTrack getCurrentTrack()
    {
        return currentTrack;
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext)
        {
            nextTrack();
        }
        if (tracksInQueue >= 0) tracksInQueue--;
    }


    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track)
    {
        currentTrack = track;
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
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException ex)
    {

    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
    {

    }
}
