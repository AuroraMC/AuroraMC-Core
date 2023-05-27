/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation.blocklog;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.BlockLogEvent;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommandBlockLogSetLimit extends ServerCommand {


    public CommandBlockLogSetLimit() {
        super("setlimit", Collections.emptyList(), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            int x;
            try {
                x = Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog setlimit [limit]**"));
                return;
            }

            ServerAPI.setLimit(x);
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Block log limit set to **" + x + "**."));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog setlimit [limit]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}
