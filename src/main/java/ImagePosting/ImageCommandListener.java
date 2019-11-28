package ImagePosting;

import Utility.RESTHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.net.URL;
import java.util.List;

import static Resources.BotReply.NO_IMAGE_FOUND;

/**
 * Handles all Imgur related commands that the bot receives.
 */
@SuppressWarnings("WeakerAccess")
public class ImageCommandListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(final MessageReceivedEvent event)
    {
        if (!isEventValid(event)) return;

        final MessageChannel channel = event.getTextChannel();
        final String content = event.getMessage().getContentDisplay();
        final RESTHelper RESThelper = new RESTHelper();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (content.startsWith("!avocado wallpaper") || content.startsWith("!a wallpaper"))
        {
//            final String searchQuery = content.substring(content.lastIndexOf("wallpaper") + 5).trim();
            channel.sendMessage(getWallpapers("2149777", channel, new ImgurContentManager(gson, RESThelper))).queue();
        }
        else if (content.startsWith("!avocado imgur") || content.startsWith("!a imgur"))
        {
            final String searchQuery = content.substring(content.lastIndexOf("imgur") + 5).trim();
            channel.sendMessage(getImage(searchQuery, channel, new ImgurContentManager(gson, RESThelper))).queue();
        }
    }

    String getWallpapers(final String opID, final MessageChannel channel, final ImgurContentManager contentManager)
    {
        channel.sendMessage(":eye_in_speech_bubble: Grabbing wallpapers").queue();

        List<URL> wallpapers = contentManager.getWallpapers(opID);
        if (wallpapers != null && !wallpapers.isEmpty())
        {
            return wallpapers.get(0).toString();
        }
        return "No Wallpapers Found";
    }

    /**
     * Retrieves a post from Imgur matching the given search query.
     * @param searchQuery the query to search Imgur for.
     * @param channel The text channel that the Bot is posting the results to.
     * @return The URL of the matching Imgur post, or an error message if nothing is found.
     */
    String getImage(final String searchQuery, final MessageChannel channel, final ImgurContentManager contentManager)
    {
        channel.sendMessage(":eye_in_speech_bubble: **Searching Imgur for** `" + searchQuery + "`").queue();
        Gallery searchResults = contentManager.getImgurGallery(searchQuery);

        if (searchResults != null && !searchResults.getData().isEmpty())
        {
            return searchResults.getData().get(0).getLink();
        }
        return NO_IMAGE_FOUND;
    }

    /**
     * Determines if an MessageReceivedEvent is valid by checking if its null, from a bot, and from a text channel.
     * @param event A MessageReceivedEvent received by the bot from a channel its listening to.
     * @return True if the event is valid, false if not.
     */
    boolean isEventValid(final MessageReceivedEvent event)
    {
        return (event != null && !event.getAuthor().isBot() && event.getMessage().isFromType(ChannelType.TEXT));
    }
}
