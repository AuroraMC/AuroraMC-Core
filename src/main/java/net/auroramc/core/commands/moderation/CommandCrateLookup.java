/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.store.Payment;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Crate;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.support.PaymentHistory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCrateLookup extends Command {

    public CommandCrateLookup() {
        super("cratelookup", Collections.singletonList("cl"), Arrays.asList(Permission.DEBUG_ACTION, Permission.ADMIN, Permission.SUPPORT), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(args.get(0));
                    } catch (IllegalArgumentException e) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crate", "That is not a valid UUID."));
                        return;
                    }


                    Crate crate = AuroraMCAPI.getDbManager().getCrate(uuid);
                    if (crate == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crate", "That crate does not exist or has expired."));
                        return;
                    }
                    String name = AuroraMCAPI.getDbManager().getNameFromID(crate.getOwner());
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crate", "Crate information for crate **" + crate.getUuid().toString() + "**:\n" +
                            "Owner: **" + name + "**\n" +
                            "Type: **" + crate.getType() + "**\n" +
                            "Generated: **" + new Date(crate.getGenerated()) + "**\n" +
                            "Opened: **" + ((crate.getOpened() != -1)?new Date(crate.getOpened()):"N/A") + "**\n" +
                            "Reward: **" + ((crate.getLoot() != null)?crate.getLoot().getRewardTitle():"N/A") + "**"));

                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crate", "Invalid syntax. Correct syntax: **/cratelookup [uuid]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
