package ImagePosting;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

public class ImageCommandListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;

        MessageChannel channel = event.getTextChannel();
        String content = event.getMessage().getContentDisplay();
        ImgurContent imgurContent;

        try
        {
            imgurContent = new ImgurContent();

        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
            return;
        }

        if (content.equals("!a wallpaper") || content.equals("!avocado wallpaper"))
        {
            imgurContent.getWallpaper();
        }
    }
}
