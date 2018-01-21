import Music.MusicManager;

import com.neovisionaries.ws.client.WebSocketFactory;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.TextChannelImpl;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import okhttp3.OkHttpClient;
import org.junit.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static Resources.TestValues.*;
import static org.junit.Assert.*;


@SuppressWarnings("FieldCanBeLocal")
public class MusicLoadResultHandlerTests
{
    private MessageChannel channel;
    private MusicManager   manager;
    private User author;
    private SessionController controller;
    private OkHttpClient.Builder clientBuilder;
    private WebSocketFactory webSocketFactory;
    private ConcurrentMap<String, String> map;
    private JDAImpl testJDA;

    @Before
    public void setUp()
    {
        controller = new SessionControllerAdapter();
        clientBuilder = new OkHttpClient.Builder();
        webSocketFactory = new WebSocketFactory();
        map = new ConcurrentHashMap<>();

        testJDA = new JDAImpl(BOT_ACCOUNT, BOT_TOKEN, controller, clientBuilder, webSocketFactory, AUTO_RECONNECT, ENABLE_AUDIO, USE_SHUTDOWN_HOOK,
                              ENABLE_DELETE_SPLITTING, RETRY_ON_TIMEOUT, ENABLE_MDC, CORE_POOL_SIZE, MAX_RECONNECT_DELAY, map);
        channel = new TextChannelImpl(MY_CHANNEL_ID, new GuildImpl(testJDA, MY_GUILD_ID));
        manager = new MusicManager(new DefaultAudioPlayerManager());
        author = new UserImpl(MY_USER_ID, testJDA);
    }



    @After
    public void tearDown()
    {
        controller = null;
        clientBuilder = null;
        webSocketFactory = null;
        map = null;
        testJDA = null;
        channel = null;
        manager = null;
        author  = null;
    }
}
