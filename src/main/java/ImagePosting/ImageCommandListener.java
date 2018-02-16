package ImagePosting;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

import static Resources.BotReply.*;


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
            wallpaperGallery = imgurContentManager.getWallpaperGallery();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            channel.sendMessage(CANT_CONNECT_WITH_IMGUR).queue();
            return;
        }

        if (content.equals("!a wallpaper") || content.equals("!avocado wallpaper"))
        {
            channel.sendMessage(PULLING_WALLPAPERS).queue();

            if (wallpaperGallery != null && wallpaperGallery.getData() != null)
            {
                channel.sendMessage(wallpaperGallery.getData().get(0).getLink()).queue();
                channel.sendMessage(wallpaperGallery.getData().get(1).getLink()).queue();
                channel.sendMessage(wallpaperGallery.getData().get(2).getLink()).queue();
            }
        }
    }


}
