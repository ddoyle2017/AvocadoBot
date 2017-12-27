package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.MessageChannel;


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
        channel.sendMessage("**Playing** :notes: `" + track.getInfo().title + "`").queue();
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

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist)
    {

    }
}
