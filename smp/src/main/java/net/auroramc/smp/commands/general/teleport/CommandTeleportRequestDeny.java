/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.teleport;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandTeleportRequestDeny extends ServerCommand {


    public CommandTeleportRequestDeny() {
        super("tpdeny", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
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

            if (player.getPendingTPARequests().remove(target.getUniqueId()) || player.getPendingTPAHereRequests().remove(target.getUniqueId())) {
                target.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("Your teleport request to **%s** was denied.", player.getByDisguiseName())));
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("**%s's** teleport request has been denied.", target.getByDisguiseName())));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Teleport", String.format("You do not have any incoming teleport request from user **%s**.", args.get(0))));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "Invalid syntax. Correct syntax: **/tpdeny [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
