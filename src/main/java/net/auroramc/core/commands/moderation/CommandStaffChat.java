package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandStaffChat extends Command {


    public CommandStaffChat() {
        super("staffchat", Arrays.asList("sc", "s", "staff", "doyourjobmod", "helpmemoderatoryouresupposedtohelppeoplewhichiswhyyouramoderator"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
            if (args.size() > 0) {
                String message = String.join(" ", args);
                player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, message));
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                for (Player target : Bukkit.getOnlinePlayers()) {
                    AuroraMCPlayer p = AuroraMCAPI.getPlayer(target);
                    if (p != null) {
                        if (p != player) {
                            if (p.hasPermission("moderation")) {
                                p.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatStaffMessage(player, message));
                                p.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 2);
                            }
                        }

                    }
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Staff Chat", "Invalid syntax. Correct syntax: **/s [message]**"));
            }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
