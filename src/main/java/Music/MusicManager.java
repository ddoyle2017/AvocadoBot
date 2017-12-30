package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;


/**
 * MusicManager
 *
 * - Overall music manager for a Guild (Discord Server. Encapsulates the audio player (executes the playing of tracks) together
 *   with the track scheduler (manages the queue of tracks to play) into one object.
 *
 */
public class MusicManager
{
    private final AudioPlayer    player;
    private final TrackScheduler scheduler;


    MusicManager(AudioPlayerManager manager)
    {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    public AudioPlayerSendHandler getSendHandler()
    {
        return (new AudioPlayerSendHandler(player));
    }

    public AudioPlayer getPlayer()
    {
        return player;
    }

    public TrackScheduler getScheduler()
    {
        return scheduler;
    }
}
