import Music.MusicManager;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static Resources.TestValues.*;


@SuppressWarnings("FieldCanBeLocal")
public class MusicControlsTests
{
    private AudioPlayerManager audioManager;
    private MusicManager musicManager;
    private SessionController controller;
    private OkHttpClient.Builder clientBuilder;
    private WebSocketFactory webSocketFactory;
    private ConcurrentMap<String, String> map;
    private JDAImpl jda;
    private ReceivedMessage message;
    private MessageReceivedEvent event;
    private User author;
    private OffsetDateTime editTime;
    private List<MessageReaction> messageReactions;
    private List<Message.Attachment> messageAttachments;
    private List<MessageEmbed> messageEmbeds;
    private TextChannelImpl channel;
    private GuildImpl guild;


    @Before
    public void setUp()
    {
        controller = new SessionControllerAdapter();
        clientBuilder = new OkHttpClient.Builder();
        webSocketFactory = new WebSocketFactory();
        map = new ConcurrentHashMap<>();
        editTime = OffsetDateTime.MAX;
        messageReactions = new ArrayList<>();
        messageAttachments = new ArrayList<>();
        messageEmbeds = new ArrayList<>();

        jda = new JDAImpl(BOT_ACCOUNT, BOT_TOKEN, controller,
                        clientBuilder, webSocketFactory, AUTO_RECONNECT,
                        ENABLE_AUDIO, USE_SHUTDOWN_HOOK, ENABLE_DELETE_SPLITTING,
                        RETRY_ON_TIMEOUT, ENABLE_MDC, CORE_POOL_SIZE,
                        MAX_RECONNECT_DELAY, map);

        author = new UserImpl(MY_USER_ID, jda);
        guild = new GuildImpl(jda, MY_GUILD_ID);
        channel = new TextChannelImpl(MY_CHANNEL_ID, guild);

        message = new ReceivedMessage(MESSAGE_ID, channel, TEXT_MESSAGE,
                                    NOT_FROM_WEBHOOK, MENTIONS_EVERYONE_FALSE, NOT_TEXT_TO_SPEECH,
                                    NOT_A_PINNED_MESSAGE, TEST_MESSAGE_CONTENT, MESSAGE_VALIDATION_NONCE,
                                    author, editTime, messageReactions,
                                    messageAttachments, messageEmbeds);

        audioManager = new DefaultAudioPlayerManager();
        musicManager = new MusicManager(audioManager);
        event = new MessageReceivedEvent(jda, 111, message);
    }




    @After
    public void tearDown()
    {
        audioManager = null;
        musicManager = null;
        controller = null;
        clientBuilder = null;
        webSocketFactory = null;
        map = null;
        jda = null;
        message = null;
        event = null;
        author = null;
        editTime = null;
        messageReactions = null;
        messageAttachments = null;
        messageEmbeds = null;
    }
}
