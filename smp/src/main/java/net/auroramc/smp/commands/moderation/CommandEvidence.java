/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEvidence extends ServerCommand {


    public CommandEvidence() {
        super("evidence", Arrays.asList("attachevidence","setevidence","addevidence"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (args.get(0).matches("[A-Z0-9]{8}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String code = args.remove(0);
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if (punishment != null) {
                            AuroraMCAPI.getDbManager().attachEvidence(code, String.join(" ", args));
                            player.sendMessage(TextFormatter.pluginMessage("Evidence", "The evidence has been attached to the punishment."));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Evidence", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Evidence", "Invalid syntax. Correct syntax: **/evidence [Punishment ID] [Evidence]**"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Evidence", "Invalid syntax. Correct syntax: **/evidence [Punishment ID] [Evidence]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
        return new ArrayList<>();
    }
}
