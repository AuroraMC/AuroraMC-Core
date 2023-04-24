/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCrateLookup extends ServerCommand {

    public CommandCrateLookup() {
        super("cratelookup", Collections.singletonList("cl"), Arrays.asList(Permission.DEBUG_ACTION, Permission.ADMIN, Permission.SUPPORT), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(args.get(0));
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Crate", "That is not a valid UUID."));
                        return;
                    }


                    Crate crate = AuroraMCAPI.getDbManager().getCrate(uuid);
                    if (crate == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Crate", "That crate does not exist or has expired."));
                        return;
                    }
                    String name = AuroraMCAPI.getDbManager().getNameFromID(crate.getOwner());
                    player.sendMessage(TextFormatter.pluginMessage("Crate", "Crate information for crate **" + crate.getUuid().toString() + "**:\n" +
                            "Owner: **" + name + "**\n" +
                            "Type: **" + crate.getType() + "**\n" +
                            "Generated: **" + new Date(crate.getGenerated()) + "**\n" +
                            "Opened: **" + ((crate.getOpened() != -1)?new Date(crate.getOpened()):"N/A") + "**\n" +
                            "Reward: **" + ((crate.getLoot() != null)?TextFormatter.convert(crate.getLoot().getRewardTitle()):"N/A") + "**"));

                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Crate", "Invalid syntax. Correct syntax: **/cratelookup [uuid]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
