package ImagePosting;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.junit.Before;
import org.junit.Test;

public class ImageCommandListenerTests
{
    private MessageReceivedEvent event;
    private MessageChannel channel;
    private String content;

    @Before
    public void setUp()
    {

    }

    @Test
    public void onMessageReceived_GivenInvalidEvent_DoesNotProcessCommand()
    {

    }

    @Test
    public void onMessageReceived_GivenImgurSearchCommand_CallsGetImageMethod()
    {

    }

    @Test
    public void onMessageReceived_GivenWallpaperCommand_CallsGetWallpaperMethod()
    {

    }

    //
    // TO-DO: Add getWallpapers() tests here.
    //

    @Test
    public void getImage_GivenMalformedSearchQuery_ReturnsErrorMessage()
    {

    }

    @Test
    public void getImage_GivenProperSearchQuery_ReturnsURL()
    {

    }
}
