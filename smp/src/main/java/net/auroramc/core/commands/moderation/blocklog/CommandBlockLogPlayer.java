/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

import java.util.*;

public class CommandBlockLogPlayer extends ServerCommand {


    public CommandBlockLogPlayer() {
        super("player", Collections.emptyList(), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            String searchType = args.get(0).toUpperCase();
            BlockLogEvent.LogType type;
            try {
                type = BlockLogEvent.LogType.valueOf(searchType);
            } catch (IllegalArgumentException e) {
                player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog player [type | ALL] [username]**"));
                return;
            }

            String name = args.get(1);

            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Searching logs for player **" + name + "**, please wait..."));;
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(name);
                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog player [type | ALL] [username]**"));
                        return;
                    }

                    List<BlockLogEvent> events = AuroraMCAPI.getDbManager().getBlockLog(uuid, SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), ServerAPI.getLimit(), type);
                    if (events.size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (BlockLogEvent e : events) {
                            builder.append("\n");
                            builder.append("**[").append(new Date(e.getTimestamp())).append("]** ");
                            builder.append(e.getType().name());
                            builder.append(" ");
                            builder.append(e.getMaterial());
                            builder.append(" at ");
                            builder.append(e.getLocation().getX());
                            builder.append(" ");
                            builder.append(e.getLocation().getY());
                            builder.append(" ");
                            builder.append(e.getLocation().getZ());
                        }
                        player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Block logs for player **" + name + "**:" + builder));
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Block Logs", "No block logs found for player **" + name + "**."));
                    }
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Block Logs", "Invalid syntax. Correct syntax: **/blocklog player [type | ALL] [username]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }

}
