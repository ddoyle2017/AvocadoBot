import Resources.BotReply;
import Music.MusicListener;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.junit.Assert;
import org.junit.Test;

public class MusicCommandsTest
{
    private JDA api = AvocadoBot.getAPI();
    private boolean isMusicPlaying;
    private boolean isAudioConnected;

    private String emptyCommandLong   = "!avocado";
    private String emptyCommandShort  = "!a";
    private String playCommandLong    = "!avocado play";
    private String playCommandShort   = "!a play";
    private String stopCommandLong    = "!avocado stop";
    private String stopCommandShort   = "!a stop";
    private String pauseCommandLong   = "!avocado pause";
    private String pauseCommandShort  = "!a pause";
    private String resumeCommandLong  = "!avocado resume";
    private String resumeCommandShort = "!a resume";
    private String skipCommandLong    = "!avocado skip";
    private String skipCommandShort   = "!a skip";
    private String nextCommandLong    = "!avocado next";
    private String nextCommandShort   = "!a next";
    private String joinCommandLong    = "!avocado join";
    private String joinCommandShort   = "!a join";
    private String leaveCommandLong   = "!avocado leave";
    private String leaveCommandShort  = "!a leave";


    @Test
    public void emptyCommandTest()
    {

    }

    @Test
    public void playCommand_MissingSongTest()
    {

    }

    @Test
    public void playCommand_ResumeSongTest()
    {

    }

    @Test
    public void playCommand_QueueSongTest()
    {

    }

    @Test
    public void playCommand_StartMusicPlayingTest()
    {

    }
}
