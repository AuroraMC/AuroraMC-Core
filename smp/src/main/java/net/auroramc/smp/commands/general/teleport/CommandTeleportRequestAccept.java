/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.teleport;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandTeleportRequestAccept extends ServerCommand {


    public CommandTeleportRequestAccept() {
        super("tpaccept", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase(player.getName())) {
                player.sendMessage(TextFormatter.pluginMessage("Teleport", "You cannot teleport to yourself."));
                return;
            }
            AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(args.get(0));
            if (target == null) {
                target = ServerAPI.getPlayer(args.get(0));
                if (target == null || target.isDisguised()) {
                    player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("No matches found for user [**%s**]. Ensure you are on the same realm.", args.get(0))));
                    return;
                }
            }

            if (player.getPendingTPARequests().remove(target.getUniqueId())) {
                player.setLastTeleport(System.currentTimeMillis());
                target.setBackLocation(new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getPitch(), target.getLocation().getYaw(), SMPLocation.Reason.HOME));
                target.teleport(player.getLocation());
                target.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You have been teleported to player **%s**.", player.getByDisguiseName())));
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("**%s** has been teleported to you.", target.getByDisguiseName())));
            } else if (player.getPendingTPAHereRequests().remove(target.getUniqueId())) {
                player.setBackLocation(new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw(), SMPLocation.Reason.HOME));
                player.teleport(target.getLocation());
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You have been teleported to player **%s**.", target.getByDisguiseName())));
                target.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("**%s** has been teleported to you.", player.getByDisguiseName())));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You do not have any incoming teleport request from user **%s**.", args.get(0))));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tpaccept [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
