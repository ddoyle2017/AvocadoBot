package Music;

import Resources.BotReply;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.MessageChannel;
import java.util.List;


/**
 * MusicLoadResultHandler
 *
 * - Handles the results of the TrackScheduler's actions on the tracks.
 *
 */
public class MusicLoadResultHandler implements AudioLoadResultHandler
{
    private final MessageChannel channel;
    private final MusicManager   manager;


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

    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {
        List<AudioTrack> tracks = playlist.getTracks();
        AudioTrack currentTrack = playlist.getSelectedTrack();

        if (currentTrack == null)   // If no track is currently selected, grab the first one from the queue
        {
            currentTrack = tracks.get(0);
        }
        manager.getScheduler().queue(currentTrack);
    }
}
