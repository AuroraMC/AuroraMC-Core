/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.punish.Punish;
import net.auroramc.core.gui.smp.InventorySee;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandInventorySee extends ServerCommand {


    public CommandInventorySee() {
        super("inventorysee", Collections.singletonList("invsee"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(args.get(0));
            if (target == null) {
                target = ServerAPI.getPlayer(args.get(0));
            }

            if (target != null) {
                InventorySee inventorySee = new InventorySee(player, target);
                inventorySee.open(player);
            } else {
                //The user is definitely not online, get from the Database.
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID id;
                        if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                            id = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                        } else {
                            id = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                        }
                        if (id == null) {
                            player.sendMessage(TextFormatter.pluginMessage("InvSee", String.format("User **%s** not found.", args.get(0))));
                            return;
                        }

                        String name = AuroraMCAPI.getDbManager().getNameFromUUID(id.toString());
                        String[] items = AuroraMCAPI.getDbManager().getInventory(id);
                        if (items == null) {
                            player.sendMessage(TextFormatter.pluginMessage("InvSee", String.format("User **%s** not found.", args.get(0))));
                            return;
                        }

                        args.remove(0);
                        InventorySee invsee = new InventorySee(player, items, name);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                invsee.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("InvSee", "Invalid syntax. Correct syntax: **/invsee [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
