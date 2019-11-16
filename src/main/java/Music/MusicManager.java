package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;


/**
 * MusicManager
 *
 * Overall music manager for a Guild (Discord Server. Encapsulates the LavaPlayer API and JDA API objects into
 * a single object. Allows for control of the track scheduling, audio player controls, and audio streaming.
 */
public class MusicManager
{
    private final AudioPlayer    player;
    private final TrackScheduler scheduler;

    public MusicManager(AudioPlayerManager manager)
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    AudioPlayerSendHandler getSendHandler()
    {
        return (new AudioPlayerSendHandler(player));
    }

    public AudioPlayer getPlayer()
    {
        return player;
    }

    TrackScheduler getScheduler()
    {
        return scheduler;
    }
}
