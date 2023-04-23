/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandWhoIs extends ServerCommand {


    public CommandWhoIs() {
        super("whois", Collections.singletonList("who"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(args.get(0));
            if (target != null) {
                player.sendMessage(TextFormatter.pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (&%s%s&r)", target.getActiveDisguise().getName(), target.getName(), target.getRank().getPrefixColor(), target.getRank().getName())));
            } else {
               new BukkitRunnable(){
                   @Override
                   public void run() {
                       if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                           UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                           Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                           String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                           player.sendMessage(TextFormatter.pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (&%s%s&r)", args.get(0), name, rank.getPrefixColor(), rank.getName())));
                       } else {
                           player.sendMessage(TextFormatter.pluginMessage("Who Is", String.format("No matches found for player **%s**", args.get(0))));
                       }
                   }
               }.runTaskAsynchronously(ServerAPI.getCore());
            }

        } else {
            player.sendMessage(TextFormatter.pluginMessage("Who Is", "Invalid syntax. Correct syntax: **/whois [player name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
