package ImagePosting;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static Resources.BotReply.NO_IMAGE_FOUND;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class ImageCommandListenerTests
{
    @Mock
    private User user;
    @Mock
    private Message message;
    @Mock
    private MessageAction messageAction;
    @Mock
    private TextChannel channel;
    @Mock
    private MessageReceivedEvent event;
    @Mock
    private ImgurContentManager contentManager;
    @Mock
    private Gallery gallery;
    @Mock
    private Album.Data data;
    @Mock
    private List<Album.Data> dataList;

    private ImageCommandListener imageCommandListener;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        imageCommandListener = new ImageCommandListener();
    }

    @Test
    public void isEventValid_GivenNullEvent_ReturnsFalse()
    {
        assertFalse(imageCommandListener.isEventValid(null));
    }

    @Test
    public void isEventValid_GivenEventFromBot_ReturnsFalse()
    {
        doReturn(true)
                .when(message).isFromType(any());
        doReturn(true)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        assertFalse(imageCommandListener.isEventValid(event));
    }

    @Test
    public void isEventValid_GivenNonTextChannelEvent_ReturnsFalse()
    {
        doReturn(false)
                .when(message).isFromType(any());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        assertFalse(imageCommandListener.isEventValid(event));
    }

    @Test
    public void isEventValid_GivenValidEvent_ReturnsTrue()
    {
        doReturn(true)
                .when(message).isFromType(any());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        assertTrue(imageCommandListener.isEventValid(event));
    }

    @Test
    public void onMessageReceived_GivenLongImgurSearchCommand_CallsGetImageMethod()
    {
        final ImageCommandListener imageCommandListenerSpy = Mockito.spy(ImageCommandListener.class);

        doReturn(true)
                .when(message).isFromType(any());
        doReturn("!avocado imgur cats")
                .when(message).getContentDisplay();
        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        doReturn(channel)
                .when(event).getTextChannel();

        imageCommandListenerSpy.onMessageReceived(event);
        verify(imageCommandListenerSpy, atLeastOnce()).getImage(anyString(), any(), any());
    }

    @Test
    public void onMessageReceived_GivenShortImgurSearchCommand_CallsGetImageMethod()
    {
        final ImageCommandListener imageCommandListenerSpy = Mockito.spy(ImageCommandListener.class);

        doReturn(true)
                .when(message).isFromType(any());
        doReturn("!a imgur cats")
                .when(message).getContentDisplay();
        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        doReturn(channel)
                .when(event).getTextChannel();

        imageCommandListenerSpy.onMessageReceived(event);
        verify(imageCommandListenerSpy, atLeastOnce()).getImage(anyString(), any(), any());
    }

    @Test
    public void onMessageReceived_GivenLongWallpaperCommand_CallsGetWallpaperMethod()
    {
        final ImageCommandListener imageCommandListenerSpy = Mockito.spy(ImageCommandListener.class);

        doReturn(true)
                .when(message).isFromType(any());
        doReturn("!avocado wallpaper")
                .when(message).getContentDisplay();
        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        doReturn(channel)
                .when(event).getTextChannel();

        imageCommandListenerSpy.onMessageReceived(event);
        verify(imageCommandListenerSpy, atLeastOnce()).getWallpapers(anyString(), any(), any());
    }

    @Test
    public void onMessageReceived_GivenShortWallpaperCommand_CallsGetWallpaperMethod()
    {
        final ImageCommandListener imageCommandListenerSpy = Mockito.spy(ImageCommandListener.class);

        doReturn(true)
                .when(message).isFromType(any());
        doReturn("!a wallpaper")
                .when(message).getContentDisplay();
        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(false)
                .when(user).isBot();
        doReturn(user)
                .when(event).getAuthor();
        doReturn(message)
                .when(event).getMessage();
        doReturn(channel)
                .when(event).getTextChannel();

        imageCommandListenerSpy.onMessageReceived(event);
        verify(imageCommandListenerSpy, atLeastOnce()).getWallpapers(anyString(), any(), any());
    }

    @Test
    public void getImage_GivenMalformedSearchQuery_ReturnsErrorMessage()
    {
        final String query = "";

        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(null)
                .when(contentManager).getImgurGallery(query);
        assertEquals(NO_IMAGE_FOUND, imageCommandListener.getImage(query, channel, contentManager));
    }

    @Test
    public void getImage_GivenProperSearchQuery_ReturnsURL()
    {
        final String query = "cats";

        doReturn(messageAction)
                .when(channel).sendMessage(anyString());
        doReturn(data)
                .when(dataList).get(anyInt());
        doReturn(dataList)
                .when(gallery).getData();
        doReturn(gallery)
                .when(contentManager).getImgurGallery(query);
        assertEquals(NO_IMAGE_FOUND, imageCommandListener.getImage(query, channel, contentManager));
    }

    //
    // TO-DO: Add getWallpapers() tests here when the method is finished
    //
}
