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

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommandBlockLogBlock extends ServerCommand {


    public CommandBlockLogBlock() {
        super("block", Collections.emptyList(), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 4) {
            String searchType = args.get(0).toUpperCase();
            BlockLogEvent.LogType type;
            try {
                type = BlockLogEvent.LogType.valueOf(searchType);
            } catch (IllegalArgumentException e) {
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog block [type | ALL] [x] [y] [z]**"));
                return;
            }

            int x, y, z;
            try {
                x = Integer.parseInt(args.get(1));
                y = Integer.parseInt(args.get(2));
                z = Integer.parseInt(args.get(3));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog block [type | ALL] [x] [y] [z]**"));
                return;
            }

            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Searching logs for changes at **" + x + " " + y + " " + z + "**, please wait..."));;
            new BukkitRunnable(){
                @Override
                public void run() {
                    List<BlockLogEvent> events = AuroraMCAPI.getDbManager().getBlockLog(x, y, z, SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), ServerAPI.getLimit(), type);
                    if (events.size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (BlockLogEvent e : events) {
                            builder.append("\n");
                            builder.append("**[").append(new Date(e.getTimestamp())).append("]** ");
                            if (e.getPlayer() != null) {
                                String name = AuroraMCAPI.getDbManager().getNameFromUUID(e.getPlayer().toString());
                                builder.append(name);
                            } else {
                                builder.append("World");
                            }
                            builder.append(" ");
                            builder.append(e.getType().name());
                            builder.append(" ");
                            builder.append(e.getMaterial());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Block logs for co-ordinates **" + x + " " + y + " " + z + "**:" + builder));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Block Logs", "No block logs found for block **" + x + " " + y + " " + z + "**."));
                    }
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog block [type | ALL] [x] [y] [z]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}
