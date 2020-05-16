package network.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessageRecievedListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("Server")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            if (subchannel.equals("PlaySound")) {
                switch (in.readUTF()) {
                    case "Message":
                        Player target = Bukkit.getPlayer(in.readUTF());
                        target.playSound(target.getLocation(), Sound.NOTE_PLING, 100, 2);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
