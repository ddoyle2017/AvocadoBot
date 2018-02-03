import Music.MusicManager;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.ReceivedMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;

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


    @Before
    public void setUp()
    {
        controller = new SessionControllerAdapter();
        clientBuilder = new OkHttpClient.Builder();
        webSocketFactory = new WebSocketFactory();
        map = new ConcurrentHashMap<>();
        jda = new JDAImpl(BOT_ACCOUNT, BOT_TOKEN, controller, clientBuilder, webSocketFactory, AUTO_RECONNECT, ENABLE_AUDIO, USE_SHUTDOWN_HOOK,
                ENABLE_DELETE_SPLITTING, RETRY_ON_TIMEOUT, ENABLE_MDC, CORE_POOL_SIZE, MAX_RECONNECT_DELAY, map);

        /*
         * TO-DO: finish instantiating the  ReceivedMessage object. Check http://bit.ly/2ngf1iM for parameters needed
         * for the constructor
         */
        message = new ReceivedMessage(22, null, MessageType.DEFAULT, false, false, false,
                false, "content", "nonce", null, null, null, null, null);
        audioManager = new DefaultAudioPlayerManager();
        musicManager = new MusicManager(audioManager);
        event = new MessageReceivedEvent(jda, 111, message);
    }



    @After
    public void tearDown()
    {
        audioManager = null;
        musicManager = null;

    }
}
