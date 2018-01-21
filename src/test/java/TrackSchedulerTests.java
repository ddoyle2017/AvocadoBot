import Music.TrackScheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static Resources.TestValues.*;

@SuppressWarnings("FieldCanBeLocal")
public class TrackSchedulerTests
{
    private AudioPlayerManager manager;
    private TrackScheduler scheduler;
    private AudioTrack track;
    private YoutubeAudioSourceManager youtubeManager;


    @Before
    public void setUp()
    {
        youtubeManager = new YoutubeAudioSourceManager();
        manager = new DefaultAudioPlayerManager();
        scheduler = new TrackScheduler(manager.createPlayer());
        track = youtubeManager.buildTrackObject(TRACK_IDENTIFIER,TRACK_TITLE, TRACK_AUTHOR, IS_TRACK_STREAMED, TRACK_LENGTH);
    }


    // Passing a null track will stop the current song and cause queue to throw NullPointerException
    @Test
    public void queue_OfferedTrackIsNull_ReturnsFalse()
    {
        scheduler.getQueue().clear();
        Assert.assertFalse(scheduler.queue(null));
    }

    // If there's already music playing, trying to play another track will add it to the queue
    @Test
    public void queue_OfferedTrackWhileMusicIsPlaying_ReturnsTrue()
    {
        scheduler.getQueue().clear();
        scheduler.queue(track);
        Assert.assertTrue(scheduler.queue(track));
    }

    // If no music is playing, a track offered to the queue is immediately played and removed from queue
    @Test
    public void queue_OfferedTrackWhileNoMusicIsPlaying_ReturnsFalse()
    {
        scheduler.getQueue().clear();
        Assert.assertFalse(scheduler.queue(track));
    }

    // Force play next track in queue, which is null
    @Test
    public void nextTrack_GiveNullTrack_ReturnsFalse()
    {
        scheduler.getQueue().clear();
        scheduler.queue(null);
        Assert.assertFalse(scheduler.nextTrack());
    }

    // Force play next track in queue
    @Test
    public void nextTrack_GiveProperTrack_ReturnsTrue()
    {
        scheduler.getQueue().clear();
        scheduler.queue(track);
        scheduler.queue(track.makeClone());
        Assert.assertTrue(scheduler.nextTrack());
    }


    @After
    public void tearDown()
    {
        youtubeManager = null;
        manager = null;
        scheduler = null;
        track = null;
    }
}
