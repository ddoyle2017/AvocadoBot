import Music.TrackScheduler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
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
    private AudioTrackInfo trackInfo;
    private YoutubeAudioSourceManager youtubeManager;


    @Before
    public void setUp()
    {
        youtubeManager = new YoutubeAudioSourceManager();
        trackInfo = new AudioTrackInfo(TRACK_TITLE, TRACK_AUTHOR, TRACK_LENGTH, TRACK_IDENTIFIER, IS_TRACK_STREAMED, TRACK_YOUTUBE_URL);
        manager = new DefaultAudioPlayerManager();
        scheduler = new TrackScheduler(manager.createPlayer());
        track = new YoutubeAudioTrack(trackInfo, youtubeManager);
    }

    @Test
    public void queue_OfferedAudioTrackIsNull_ReturnsFalse()
    {
        Assert.assertFalse(scheduler.queue(null));
    }

    @Test
    public void queue_OfferedProperAudioTrack_ReturnsTrue()
    {
        Assert.assertTrue(scheduler.queue(track));
    }

    @After
    public void tearDown()
    {
        manager = null;
        scheduler = null;
    }
}
