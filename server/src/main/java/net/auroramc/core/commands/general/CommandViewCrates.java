/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.general;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Crate;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.support.PaymentCrates;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandViewCrates extends ServerCommand {


    public CommandViewCrates() {
        super("viewcrates", Collections.singletonList("vc"), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1 && (player.hasPermission("support") || player.hasPermission("admin") || player.hasPermission("debug.info"))) {
            player.sendMessage(TextFormatter.pluginMessage("Crates", "Performing crate lookup for user " + args.get(0) + "..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Crates", String.format("User [**%s**] has never joined the network, so cannot have any crates.", args.get(0))));
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
                            }
                        }.runTask(ServerAPI.getCore());
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Crates", "No crates were found attached to that account. If a crate was opened more than 30 days ago, it has since expired."));
                    }

                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Crates", "Performing crate lookup..."));
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<Crate> crateList = AuroraMCAPI.getDbManager().getCrates(player.getId());
                    if (crateList.size() > 0) {
                        PaymentCrates crates = new PaymentCrates(player, player.getUniqueId(), player.getName(), null, crateList);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                crates.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Crates", "No crates were found attached to your account. If you opened a crate more than 30 days ago, it has since expired."));
                    }

                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
