package net.auroramc.core.commands.moderation;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandStaffMessage extends Command {

    public CommandStaffMessage() {
        super("messagestaff", Arrays.asList("smsg", "messagestaff", "ms", "msgs"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (player.getActiveMutes().size() == 0) {
                Player proxiedPlayer = Bukkit.getPlayer(args.get(0));
                if (proxiedPlayer != null) {
                    AuroraMCPlayer target = AuroraMCAPI.getPlayer(proxiedPlayer);
                    if (target.getActiveDisguise() != null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", String.format("No match found for [**%s**]", args.get(0))));
                        return;
                    }
                    args.remove(0);
                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                    BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, target, message);
                    target.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageFrom(player, message));
                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(target, message));
                    player.setLastAdminMessage(target.getPlayer().getUniqueId());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p != target.getPlayer() && p != player.getPlayer()) {
                            if (AuroraMCAPI.getPlayer(p) != null) {
                                if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                    p.spigot().sendMessage(formatted);
                                }
                            }
                        }
                    }
                } else {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                            if (uuid != null) {
                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                    //ProtocolMessage protocolMessage = new ProtocolMessage();
                                    //CommunicationUtils.sendMessage(protocolMessage);

                                    args.remove(0);
                                    String message = AuroraMCAPI.getFilter().filter(String.join(" ", args));

                                    String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);

                                    BaseComponent formatted = AuroraMCAPI.getFormatter().formatStaffMessage(player, rank, name, message);
                                    player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessageTo(rank, name, message));

                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        if (p != player.getPlayer()) {
                                            if (AuroraMCAPI.getPlayer(p) != null) {
                                                if (AuroraMCAPI.getPlayer(p).hasPermission("moderation")) {
                                                    p.spigot().sendMessage(formatted);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                                }
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", String.format("No match found for [**%s**]", args.get(0))));
                            }
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "You cannot send messages while muted!"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Message", "Invalid syntax. Correct syntax: **/staffmessage [player] [message]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
