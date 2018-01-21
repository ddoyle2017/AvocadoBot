package Resources;

import net.dv8tion.jda.core.AccountType;

public final class TestValues
{
    private TestValues() {}

    public static final String BOT_TOKEN = "testtoken";
    public static final String TRACK_TITLE = "title";
    public static final String TRACK_AUTHOR = "author";
    public static final String TRACK_IDENTIFIER = "g_c49ISFUZE";
    public static final String TRACK_YOUTUBE_URL = "https://www.youtube.com/watch?v=" + TRACK_IDENTIFIER;
    public static final AccountType BOT_ACCOUNT = AccountType.BOT;
    public static final boolean AUTO_RECONNECT = true;
    public static final boolean ENABLE_AUDIO = true;
    public static final boolean USE_SHUTDOWN_HOOK = true;
    public static final boolean ENABLE_DELETE_SPLITTING = true;
    public static final boolean RETRY_ON_TIMEOUT = true;
    public static final boolean ENABLE_MDC = true;
    public static final boolean IS_TRACK_STREAMED = true;
    public static final int MY_CHANNEL_ID = 0;
    public static final int MY_GUILD_ID = 1;
    public static final int MY_USER_ID = 2;
    public static final int CORE_POOL_SIZE = 10;
    public static final int MAX_RECONNECT_DELAY = 100;
    public static final long TRACK_LENGTH = 60;
}
