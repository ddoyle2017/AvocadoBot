package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;


public class MusicListener extends ListenerAdapter
{
    private AudioPlayerManager playerManager  = new DefaultAudioPlayerManager();
    private MusicManager       musicManager   = new MusicManager(playerManager);
    private boolean            isMusicPlaying = false;
    private AudioManager       manager;


    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;                          // block other bots from giving AvocadoBot commands
        if (!event.getMessage().isFromType(ChannelType.TEXT)) return;   // we only accept messages from a text channel (no DMs)

        Message         message = event.getMessage();
        String          content = message.getContentDisplay();
        MessageChannel  channel = event.getChannel();
        manager = event.getGuild().getAudioManager();

        manager.setSendingHandler(musicManager.getSendHandler());
        AudioSourceManagers.registerRemoteSources(playerManager);


        if (content.equals("!avocado") || content.equals("!a"))
        {
            channel.sendMessage(":x: **I need a command!**").queue();
        }
        else if (content.equals("!avocado play") || content.equals("!a play"))
        {
            if (!isMusicPlaying)
            {
                channel.sendMessage(":x: **Missing a song or URL.**").queue();
            }
            else if (musicManager.getPlayer().isPaused())
            {
                channel.sendMessage("**Resuming** :play_pause:").queue();
                musicManager.getPlayer().setPaused(false);
            }
            else if (!musicManager.getPlayer().isPaused())
            {
                channel.sendMessage(":x: **Player is not paused.**").queue();
            }
        }
        else if (content.startsWith("!avocado play") || content.startsWith("!a play"))
        {
            String trackUrl = content.substring(content.lastIndexOf("play") + 5, content.length()).trim();
            System.out.println("PRINT CHECK: " + trackUrl);

            joinVoiceChannel(event, channel);
            playerManager.loadItemOrdered(musicManager, trackUrl, new MusicLoadResultHandler(musicManager, channel));
            isMusicPlaying = true;
        }
        else if (content.equals("!avocado pause") || content.equals("!a pause") || content.equals("!avocado stop") || content.equals("!a stop"))
        {
            if (musicManager.getPlayer().isPaused())
            {
                channel.sendMessage(":x: **Player is already paused.**").queue();
            }
            else if (manager.getConnectionStatus() != ConnectionStatus.CONNECTED)
            {
                channel.sendMessage(":x: **You have to be in a voice channel to use this command.**").queue();
            }
            else
            {
                channel.sendMessage("**Paused** :pause_button:").queue();
                musicManager.getPlayer().setPaused(true);
            }
        }
        else if (content.equals("!avocado resume") || content.equals("!a resume"))
        {
            if (musicManager.getPlayer().isPaused())
            {
                channel.sendMessage("**Resuming** :play_pause:").queue();
                musicManager.getPlayer().setPaused(false);
            }
            else if (manager.getConnectionStatus() != ConnectionStatus.CONNECTED)
            {
                channel.sendMessage(":x: **You have to be in a voice channel to use this command.**").queue();
            }
            else
            {
                channel.sendMessage(":x: **Player is not paused.**").queue();
            }
        }
        else if (content.equals("!avocado join") || content.equals("!a join"))
        {
            joinVoiceChannel(event, channel);
        }
        else if (content.equals("!avocado leave") || content.equals("!a leave"))
        {
            leaveVoiceChannel(event, channel);
        }
    }

    private void joinVoiceChannel(MessageReceivedEvent event, MessageChannel channel)
    {
        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
        manager = event.getGuild().getAudioManager(); // a Discord server is called a "Guild"

        if (voiceChannel == null)
        {
            channel.sendMessage(":x: **You have to be in a voice channel to use this command.**").queue();
        }
        else if (manager.getConnectionStatus() != ConnectionStatus.CONNECTED)
        {
            manager.openAudioConnection(voiceChannel);
            channel.sendMessage(":ok_hand: **Joined** `" + voiceChannel.getName() + "`").queue();
        }
    }

    private void leaveVoiceChannel(MessageReceivedEvent event, MessageChannel channel)
    {
        manager = event.getGuild().getAudioManager();

        if (manager.getConnectionStatus() == ConnectionStatus.CONNECTED)
        {
            manager.closeAudioConnection();
            channel.sendMessage(":last_quarter_moon_with_face: **Successfully disconnected.**").queue();
        }
        else
        {
            channel.sendMessage(":x: **I am not connected to a voice channel.**").queue();
        }
    }
}
