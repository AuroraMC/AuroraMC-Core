package net.auroramc.core.listeners;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.ChatLogs;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatChannel;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TempChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        if (AuroraMCAPI.getPlayer(e.getPlayer()).isVanished()) {
            e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You cannot talk while vanished!"));
            return;
        }
        switch (AuroraMCAPI.getPlayer(e.getPlayer()).getChannel()) {
            case STAFF: {
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCPlayer p = AuroraMCAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, e.getMessage()));
                                p.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                            }
                        }

                    }
                }
                break;
            }
            case ALL:
            case PARTY:
                AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
                if (!player.getPreferences().isChatVisibilityEnabled()) {
                    e.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "You currently have chat disabled! Please enable chat in order to send messages again."));
                    return;
                }
                e.setMessage(AuroraMCAPI.getFilter().filter(e.getMessage()));
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (AuroraMCAPI.getPlayer(player2).getPreferences().isChatVisibilityEnabled()) {
                        player2.spigot().sendMessage(AuroraMCAPI.getFormatter().chatMessage(player, e.getMessage()));
                    }
                }
                ChatLogs.chatMessage(player.getId(), player.getName(), player.getRank(), e.getMessage(), false, ChatChannel.ALL);
                break;
        }

    }
}
