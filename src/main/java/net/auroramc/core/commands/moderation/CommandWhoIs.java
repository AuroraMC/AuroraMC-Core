/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandWhoIs extends Command {


    public CommandWhoIs() {
        super("whois", Collections.singletonList("who"), Collections.singletonList(Permission.MODERATION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            AuroraMCPlayer target = AuroraMCAPI.getDisguisedPlayer(args.get(0));
            if (target != null) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (&%s%s&r)", target.getActiveDisguise().getName(), target.getName(), target.getRank().getPrefixColor(), target.getRank().getName())));
            } else {
               new BukkitRunnable(){
                   @Override
                   public void run() {
                       if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                           UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0));
                           Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                           String name = AuroraMCAPI.getDbManager().getNameFromUUID(uuid.toString());
                           player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Who Is", String.format("Player **%s** is actually **%s** (&%s%s&r)", args.get(0), name, rank.getPrefixColor(), rank.getName())));
                       } else {
                           player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Who Is", String.format("No matches found for player **%s**", args.get(0))));
                       }
                   }
               }.runTaskAsynchronously(AuroraMCAPI.getCore());
            }

        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Who Is", "Invalid syntax. Correct syntax: **/whois [player name]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
