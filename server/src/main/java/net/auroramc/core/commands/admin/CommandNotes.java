/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.punish.AdminNotes;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNotes extends ServerCommand {
    public CommandNotes() {
        super("notes", Collections.singletonList("adminnotes"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                        if (id < 1) {
                            player.sendMessage(TextFormatter.pluginMessage("Admin Notes", String.format("User [**%s**] has never joined the network, so cannot have received an Admin Note.", args.get(0))));
                            return;
                        }
                        String name = args.remove(0);
                        String extraDetails = null;
                        if (args.size() > 0) {
                            extraDetails = String.join(" ", args);
                        }
                        AdminNotes gui = new AdminNotes(player, name, id, extraDetails);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                gui.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Admin Notes", "Invalid syntax. Correct syntax: **/notes <player> [reason]**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Admin Notes", "Invalid syntax. Correct syntax: **/notes <player> [reason]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
