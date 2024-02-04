/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.chest;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.listeners.LockChestListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandChestAdd extends ServerCommand {


    public CommandChestAdd() {
        super("add", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("team")) {
                if (player.getSmpTeam() != null) {
                    LockChestListener.getWaitingForInput().put(player.getUniqueId(), "add;team~" + player.getSmpTeam().getUuid());
                    player.sendMessage(TextFormatter.pluginMessage("Chests", "Shift-Click on the block you wish to add your team to."));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Chests", "You cannot add your team to a chest if you are not in one."));
                }
            } else {
                String name = args.get(0);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID uuid = AuroraMCAPI.getDbManager().getUUID(name);
                        if (uuid != null) {
                            LockChestListener.getWaitingForInput().put(player.getUniqueId(), "add;player~" + uuid);
                            player.sendMessage(TextFormatter.pluginMessage("Chests", "Shift-Click on the block you wish to add that player to."));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Chests", "That player was not found."));
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Chests", "Invalid syntax. Correct syntax: **/chest add [username | team]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
