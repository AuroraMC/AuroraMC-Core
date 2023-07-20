/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
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
                player.sendMessage(TextFormatter.pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (%s%s§r)", target.getActiveDisguise().getName(), target.getName(), target.getRank().getPrefixColor(), target.getRank().getName())));
            } else {
               new BukkitRunnable(){
                   @Override
                   public void run() {
                       if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                           UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                           Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                           String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                           player.sendMessage(TextFormatter.pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (%s%s§r)", args.get(0), name, rank.getPrefixColor(), rank.getName())));
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
