import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import images.ImageCommandListener;
import music.MusicCommandListener;
import music.MusicManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/***
 * This is where the initialization of the bot takes place. We create an instance of the SettingsManager class, get
 * the Discord Bot authentication information, then logs into Discord's servers.
 */
public class AvocadoBot extends ListenerAdapter
{
    private static JDA api;

    public static void main(final String... args) {
       if (!System.getProperty("file.encoding").equals("UTF-8")) return;
       setUpBot();
    }

    private static void setUpBot() {
        try {
            final Settings settings = SettingsManager.getInstance().getSettings();
            final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            final MusicManager musicManager = new MusicManager(playerManager);

            api = new JDABuilder(AccountType.BOT)
                    .setToken(settings.getBotToken())
                    .buildAsync();

            api.addEventListener(new GeneralCommandListener());
            api.addEventListener(new MusicCommandListener(playerManager, musicManager));
            api.addEventListener(new ImageCommandListener());
        }
        catch (final LoginException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static JDA getAPI()
    {
        if (api == null) {
            setUpBot();
        }
        return api;
    }
}
