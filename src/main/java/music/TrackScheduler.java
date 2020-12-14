package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TrackScheduler Class
 *
 * The TrackScheduler class acts as the scheduler for the music track queue. This class pairs
 * together an audio player with a blocking queue, then manages it.
 *
 * The results of events like pausing a song, resuming it, starting a song, exception handling,and a song stream getting stuck are all
 * handled here.
 *
 */
@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter
{
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private int tracksInQueue;
    private AudioTrack currentTrack;

    void queue(final AudioTrack track) {
        tracksInQueue++;
        if (!player.startTrack(track, true) && track != null) {
            if (queue.offer(track)) {
                System.out.println("Playing " + track.getInfo().title);
            }
        }
    }

    boolean nextTrack() {
        return player.startTrack(queue.poll(), false);
    }


    BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }


    int getTracksInQueue() {
        return tracksInQueue;
    }


    AudioTrack getCurrentTrack() {
        return currentTrack;
    }


    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason)
    {
        if (endReason.mayStartNext) {
            nextTrack();
        }
        if (tracksInQueue >= 0) tracksInQueue--;
    }


    @Override
    public void onTrackStart(final AudioPlayer player, final AudioTrack track) {
        currentTrack = track;
    }

    @Override
    public void onPlayerPause(final AudioPlayer player) { }

    @Override
    public void onPlayerResume(final AudioPlayer player) { }

    @Override
    public void onTrackException(final AudioPlayer player, final AudioTrack track, final FriendlyException ex) { }

    @Override
    public void onTrackStuck(final AudioPlayer player, final AudioTrack track, final long thresholdMs) { }
}
