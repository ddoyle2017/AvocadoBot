package music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import constants.Commands;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import static constants.BotReplies.MISSING_SONG;

/**
 * An event listener for all bot commands relevant to music playing and audio. Calls the necessary
 * functions to execute the given commands.
 */
@RequiredArgsConstructor
public class MusicCommandListener extends ListenerAdapter
{
    private final AudioPlayerManager playerManager;
    private final MusicManager musicManager;

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().isFromType(ChannelType.TEXT)) return;

        final MessageChannel channel = event.getChannel();
        final MusicControls musicControls = new MusicControls(event, playerManager, musicManager);
        final String content = event.getMessage().getContentDisplay();

        event.getGuild().getAudioManager().setSendingHandler(musicManager.getSendHandler());
        AudioSourceManagers.registerRemoteSources(playerManager);


        if (content.equals(Commands.PLAY_SONG_LONG) || content.equals(Commands.PLAY_SONG_SHORT)) {
            if (!musicControls.isMusicPlaying()) {
                channel.sendMessage(MISSING_SONG).queue();
            }
            else {
                musicControls.resumeSong();
            }
        }
        else if (content.startsWith(Commands.PLAY_SONG_LONG) || content.startsWith(Commands.PLAY_SONG_SHORT)) {
            musicControls.playSong(event.getAuthor());
        }
        else if (content.equals(Commands.STOP_SONG_LONG) || content.equals(Commands.STOP_SONG_SHORT)) {
            musicControls.stopSong();
        }
        else if (content.equals(Commands.PAUSE_SONG_LONG) || content.equals(Commands.PAUSE_SONG_SHORT)) {
            musicControls.pauseSong();
        }
        else if (content.equals(Commands.RESUME_SONG_LONG) || content.equals(Commands.RESUME_SONG_SHORT)) {
            musicControls.resumeSong();
        }
        else if (content.equals(Commands.SKIP_SONG_LONG) || content.equals(Commands.SKIP_SONG_SHORT) || content.equals(Commands.NEXT_SONG_LONG) || content.equals(Commands.NEXT_SONG_SHORT)) {
            musicControls.skipSong();
        }
        else if (content.equals(Commands.JOIN_CHANNEL_LONG) || content.equals(Commands.JOIN_CHANNEL_SHORT)) {
            musicControls.joinVoiceChannel();
        }
        else if (content.equals(Commands.LEAVE_CHANNEL_LONG) || content.equals(Commands.LEAVE_CHANNEL_SHORT)) {
            musicControls.leaveVoiceChannel();
        }
        else if (content.equals(Commands.NOW_PLAYING_LONG) || content.equals(Commands.NOW_PLAYING_SHORT)) {
            musicControls.nowPlaying();
        }
    }
}
