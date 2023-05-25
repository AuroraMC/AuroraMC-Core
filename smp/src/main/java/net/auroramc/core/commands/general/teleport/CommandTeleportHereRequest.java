/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.teleport;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandTeleportHereRequest extends ServerCommand {


    public CommandTeleportHereRequest() {
        super("tpahere", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
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

            if (target.getPendingTPARequests().contains(player.getUniqueId()) || target.getPendingTPAHereRequests().contains(player.getUniqueId())) {
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You already have an outgoing teleport request to user **%s**. Please wait until any previous requests have expired.", args.get(0))));
                return;
            }

            if (player.getLastTeleport() != 0 && ((System.currentTimeMillis() - player.getLastTeleport())/1000) < 60) {
                player.sendMessage(TextFormatter.pluginMessage("Teleport", "You cannot teleport for **" + String.format("%.2f", (60-((System.currentTimeMillis() - player.getLastTeleport())/1000d))) + "**."));
                return;
            }

            target.getPendingTPAHereRequests().add(player.getUniqueId());
            player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You have sent a teleport request to player **%s**.", target.getByDisguiseName())));
            target.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You **%s** has requested that you teleport to them. Use **/tpaccept %s** to accept this request.", player.getByDisguiseName(), player.getByDisguiseName())));
            AuroraMCServerPlayer finalTarget = target;
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (finalTarget.getPendingTPAHereRequests().remove(player.getUniqueId())) {
                        player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("Your teleport request to player **%s** has expired.", finalTarget.getByDisguiseName())));
                        finalTarget.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("**%s's** teleport request has expired.", player.getByDisguiseName())));
                    }
                }
            }.runTaskLater(ServerAPI.getCore(), 1200);
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tpahere [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
