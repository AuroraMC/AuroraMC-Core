/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.staff;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandPlusLookup extends ProxyCommand {

    public CommandPlusLookup() {
        super("pluslookup", Collections.singletonList("checkplus"), Collections.singletonList(Permission.MODERATION), false, null);
    }


    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                if (uuid != null) {
                    long expire = AuroraMCAPI.getDbManager().getExpire(uuid);
                    if (expire != -1 && expire > System.currentTimeMillis()) {
                        double value = (expire - System.currentTimeMillis()) / 3600000d;

                        String suffix = "Hours";
                        if (value >= 24) {
                            suffix = "Days";
                            value = value / 24;
                        }
                        value = (Math.round(value * 10))/10.0;
                        String expiresIn = value + " " + suffix;

                        if (player.hasPermission("admin")) {
                            long streakStart = AuroraMCAPI.getDbManager().getStreakStartTimestamp(uuid);
                            int streak = AuroraMCAPI.getDbManager().getStreak(uuid, streakStart);
                            int daysSubbed = AuroraMCAPI.getDbManager().getDaysSubscribed(uuid);

                            player.sendMessage(TextFormatter.pluginMessage("Plus", String.format("Plus Subscription information for **%s**:\n" +
                                    "Total Days Subscribed (including future days): **%s**\n" +
                                    "Current Subscription Streak: **%s**\n" +
                                    "Streak started at: **%s**\n" +
                                    "Expires: **%s** (**%s** from now)", args.get(0), daysSubbed, streak, new Date(streakStart), new Date(expire), expiresIn)));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Plus", String.format("Plus Subscription information for **%s**:\n" +
                                    "Expires: **%s** (**%s** from now)", args.get(0), new Date(expire), expiresIn)));
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Plus", "That player does not have an active Plus subscription."));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Plus", "Could not find a user by that name."));
                }
            });
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Plus", "Invalid syntax. Correct syntax: **/pluslookup [user]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
