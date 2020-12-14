package music;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 * A wrapper class for the LavaPlayer API's AudioPlayer class which makes it compatible with JDA's AudioSendHandler class.
 * The Opus Codec is used to compress and transfer audio data from our streaming source (Youtube for example). Audio data
 * is transferred in "frames" of 20 milliseconds of play time.
 *
 * The canProvide() method is called before every provide20MsAudio() call, to prime the next Opus frame before its
 * delivered to the output destination (AvocadoBot's audio output). If canProvide() returns false, our stream is over.
 *
 */
@RequiredArgsConstructor
public class AudioPlayerSendHandler implements AudioSendHandler
{
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return (lastFrame != null);
    }

    @Override
    public byte[] provide20MsAudio() {
        return lastFrame.data;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
