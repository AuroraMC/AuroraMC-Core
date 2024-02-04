/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation.blocklog;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBlockLogRollback extends ServerCommand {


    public CommandBlockLogRollback() {
        super("rollback", Collections.emptyList(), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            int radius, hours;

            try {
                radius = Integer.parseInt(args.get(0));
                hours = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog rollback [radius] [hours]**"));
                return;
            }

            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Rolling back changes, please wait..."));;
            int initX = player.getLocation().getBlockX();
            int initZ = player.getLocation().getBlockZ();


        } else {
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog rollback [radius] [hours]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}


