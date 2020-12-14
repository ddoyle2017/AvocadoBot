package music;

import constants.BotReplies;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.List;


/**
 * MusicLoadResultHandler
 *
 * Handles the results of the TrackScheduler's actions on the tracks.
 *
 */
@SuppressWarnings("FieldCanBeLocal")
public class MusicLoadResultHandler extends MusicObject implements AudioLoadResultHandler
{
    private final MessageChannel channel;
    private final MusicManager manager;
    private final User author;


    MusicLoadResultHandler(MusicManager manager, MessageChannel channel, User author)
    {
        this.channel = channel;
        this.manager = manager;
        this.author = author;
    }


    @Override
    public void trackLoaded(AudioTrack track)     // called when a URL is provided
    {
        if (manager.getScheduler().getQueue().isEmpty())
        {
            channel.sendMessage("**Playing** :notes: `" + track.getInfo().title + "`").queue();
        }
        else
        {
            channel.sendMessage("**Added** `" + track.getInfo().title + "` **to queue** :musical_note:").queue();
        }
    }


    @Override
    public void loadFailed(FriendlyException ex)
    {
        channel.sendMessage(":x: **Could not play: " + ex.getMessage() + "**").queue();
    }


    @Override
    public void noMatches()
    {
        channel.sendMessage(BotReplies.SONG_NOT_FOUND).queue();
    }


    @Override
    public void playlistLoaded(AudioPlaylist playlist)     // called when a keyphrase for a YouTube search is provided
    {
        List<AudioTrack> tracks = playlist.getTracks();
        AudioTrack currentTrack = playlist.getSelectedTrack();
        int tracksInQueue = manager.getScheduler().getTracksInQueue();

        if (currentTrack == null)
        {
            currentTrack = tracks.get(0);
        }
        manager.getScheduler().queue(currentTrack);
        String queuePosition = (tracksInQueue <= 0) ? ("**Currently playing**") : Integer.toString(tracksInQueue);
        channel.sendMessage(getTrackInfo(currentTrack, queuePosition)).queue();
    }


    private MessageEmbed getTrackInfo(AudioTrack track, String queuePosition)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.DARK_GRAY);
        embedBuilder.setAuthor("Added to queue", null, author.getAvatarUrl());
        embedBuilder.setTitle(track.getInfo().title, track.getInfo().uri);
        embedBuilder.addField("Channel", track.getInfo().author, true);
        embedBuilder.addField("Duration", convertMSToTimeStamp(track.getInfo().length), true);
        embedBuilder.addField("Requested by", author.getName(), true);
        embedBuilder.addField("Position in queue", queuePosition, true);
        embedBuilder.setThumbnail(getYouTubeVideoThumbnail(track.getIdentifier()));

        return embedBuilder.build();
    }
}
