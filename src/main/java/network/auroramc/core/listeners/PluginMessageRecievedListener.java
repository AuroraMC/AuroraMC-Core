package network.auroramc.core.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessageRecievedListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player2, byte[] bytes) {
        if (channel.equals("Server")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            if (subchannel.equals("PlaySound")) {
                switch (in.readUTF()) {
                    case "Message":
                        Player player = Bukkit.getPlayer(in.readUTF());
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 100, 2);
                        break;
                    default:
                        break;
                }
            } else if (subchannel.equalsIgnoreCase("DisguiseCheckFail")) {
                AuroraMCAPI.getPendingDisguiseChecks().remove(Bukkit.getPlayer(in.readUTF()));
            } else if (subchannel.equalsIgnoreCase("DisguiseCheckSuccess")) {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(Bukkit.getPlayer(in.readUTF()));
                String[] disguise = AuroraMCAPI.getPendingDisguiseChecks().get(player.getPlayer()).split(";");
                Rank chosenRank = AuroraMCAPI.getRanks().get(Integer.parseInt(disguise[2]));
                if (player.disguise(disguise[0], disguise[1], chosenRank)) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Disguise");
                    out.writeUTF(player.getName());
                    out.writeUTF(disguise[0]);
                    out.writeUTF(disguise[1]);
                    out.writeInt(chosenRank.getId());
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    switch (in.readInt()) {
                        case 1:
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s**. To undisguise, simply type **/undisguise**.", disguise[0])));
                            break;
                        case 2:
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**. To undisguise, simply type **/undisguise**.", disguise[1], disguise[0])));
                            break;
                    }
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Something went wrong when attempting to disguise. Maybe the disguise skin doesn't exist?"));
                }
            }
        }
    }
}
