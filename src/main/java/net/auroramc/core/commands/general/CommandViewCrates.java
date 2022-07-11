/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Crate;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.support.PaymentCrates;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandViewCrates extends Command {


    public CommandViewCrates() {
        super("viewcrates", Collections.singletonList("vc"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1 && (player.hasPermission("support") || player.hasPermission("admin") || player.hasPermission("debug.info"))) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crates", "Performing crate lookup for user " + args.get(0) + "..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id < 1) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crates", String.format("User [**%s**] has never joined the network, so cannot have any crates.", args.get(0))));
                        return;
                    }

                    String name = AuroraMCAPI.getDbManager().getNameFromID(id);
                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);

                    List<Crate> crateList = AuroraMCAPI.getDbManager().getCrates(id);
                    if (crateList.size() > 0) {
                        PaymentCrates crates = new PaymentCrates(player, uuid, name, null, crateList);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                crates.open(player);
                                AuroraMCAPI.openGUI(player, crates);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crates", "No crates were found attached to that account. If a crate was opened more than 30 days ago, it has since expired."));
                    }

                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crates", "Performing crate lookup..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<Crate> crateList = AuroraMCAPI.getDbManager().getCrates(player.getId());
                    if (crateList.size() > 0) {
                        PaymentCrates crates = new PaymentCrates(player, player.getPlayer().getUniqueId(), player.getName(), null, crateList);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                crates.open(player);
                                AuroraMCAPI.openGUI(player, crates);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Crates", "No crates were found attached to your account. If you opened a crate more than 30 days ago, it has since expired."));
                    }

                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
