package Resources;

public final class BotReply
{
    private BotReply() {}

    // Music related responses
    public static final String MISSING_VOICE_CHANNEL = ":x: **You have to be in a voice channel to use this command.**";
    public static final String MISSING_COMMAND = ":x: **I need a command!**";
    public static final String MISSING_SONG = ":x: **Missing a song or URL.**";
    public static final String NOTHING_IS_PLAYING = ":x: **Nothing is playing right now.**";
    public static final String SONG_STOPPED = "**Stopped** :stop_button:";
    public static final String SONG_PAUSED = "**Paused** :pause_button:";
    public static final String SONG_SKIPPED = "**Skipped** :fast_forward:";
    public static final String SONG_RESUMED = "**Resuming** :play_pause:";
    public static final String SONG_ALREADY_PAUSED = ":x: **Player is already paused.**";
    public static final String SONG_NOT_PAUSED = ":x: **Player is not paused.**";
    public static final String SONG_NOT_FOUND = ":x: **Could not find song.**";
    public static final String VOICE_CHANNEL_DISCONNECT = ":last_quarter_moon_with_face: **Successfully disconnected.**";
    public static final String NOT_IN_VOICE_CHANNEL = ":x: **I am not connected to a voice channel.**";

    // Image pulling related responses
    public static final String CANT_CONNECT_WITH_IMGUR = ":x: **I can't connect with Imgur**";
    public static final String PULLING_WALLPAPERS = ":eye_in_speech_bubble: **Searching** `r/slashw` **for wallpapers**";
}
