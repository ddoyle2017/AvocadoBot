package ImagePosting;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

import static Resources.BotReply.*;
import static Resources.ImgurValues.*;


public class ImageCommandListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;

        MessageChannel channel = event.getTextChannel();
        String content = event.getMessage().getContentDisplay();
        ImgurContentManager imgurContentManager;
        Gallery wallpaperGallery;

        try
        {
            imgurContentManager = new ImgurContentManager();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            channel.sendMessage(CANT_CONNECT_WITH_IMGUR).queue();
            return;
        }

        if (content.equals("!avocado wallpaper") || content.equals("!a wallpaper"))
        {
            channel.sendMessage(PULLING_WALLPAPERS).queue();
            wallpaperGallery = imgurContentManager.getImgurGallery(IMGUR_API_URL + GRAB_NEWEST_SLASHW_ALBUMS + AS_JSON);


            if (wallpaperGallery != null && !wallpaperGallery.getData().isEmpty())
            {
                channel.sendMessage(wallpaperGallery.getData().get(0).getLink()).queue();
                channel.sendMessage(wallpaperGallery.getData().get(1).getLink()).queue();
                channel.sendMessage(wallpaperGallery.getData().get(2).getLink()).queue();
            }
            else
            {
                channel.sendMessage(NO_IMAGE_FOUND).queue();
            }
        }

        if (content.startsWith("!avocado imgur") || content.startsWith("!a imgur"))
        {
            String imageQuery = content.substring(content.lastIndexOf("imgur") + 5, content.length()).trim();
            channel.sendMessage(":eye_in_speech_bubble: **Searching Imgur for** `" + imageQuery + "`").queue();
            Gallery searchResults = imgurContentManager.getImgurGallery(IMGUR_API_URL + SEARCH_IMGUR + "'" + imageQuery + "'");

            if (searchResults != null && !searchResults.getData().isEmpty())
            {
                channel.sendMessage(searchResults.getData().get(0).getLink()).queue();
            }
            else
            {
                channel.sendMessage(NO_IMAGE_FOUND).queue();
            }
        }
    }


}
