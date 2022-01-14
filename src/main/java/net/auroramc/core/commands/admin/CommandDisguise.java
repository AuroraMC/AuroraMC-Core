/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.DiscordWebhook;
import net.auroramc.core.api.utils.UUIDUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandDisguise extends Command {


    public CommandDisguise() {
        super("disguise", Collections.singletonList("nick"), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.size()) {
                case 1:
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (args.get(0).equalsIgnoreCase("random")) {

                        } else if (!player.hasPermission("disguise.custom")) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
                        }
                        if (args.get(0).equalsIgnoreCase(player.getName())) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as yourself."));
                            return;
                        }
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                                if (uuid != null) {
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    if (rank != null) {
                                        if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they have a non-purchasable rank."));
                                            return;
                                        }
                                    }
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                        return;
                                    }
                                }

                                if (AuroraMCAPI.getDbManager().isUsernameBanned(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username is blacklisted."));
                                    return;
                                }
                                if (AuroraMCAPI.getFilter() == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                    return;
                                }
                                if (!AuroraMCAPI.getFilter().filter(args.get(0)).equals(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username would be filtered."));
                                    return;
                                }

                                if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                    return;
                                }

                                switch (player.getRank().getId()) {
                                    case 6:
                                    case 5: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928786853421809694/5KJn2zEdjECztUgLzUXNt38GRuLlcEOmcstENVTjv6reFUmHqRUmCWbIveFi8HZoy8FV");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s**.", player.getName(), args.get(0))).setColor(((player.getRank().getId() == 5)?new Color(255, 170, 0):new Color(170, 0, 170))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                    default: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928785758599405680/gnzddoyMeiq9wMKxkKVRptVx3PX8EfGeRtFz9ZuXlpXGJV1G37l_g-KeJgLJIX5WNIUF");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s**.", player.getName(), args.get(0))).setColor(((player.getRank().getId() >=11)?new Color(255, 85, 85):new Color(255, 170, 0))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        player.disguise(args.get(0), args.get(0), Rank.PLAYER);
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s**. To undisguise, simply type **/undisguise**.", args.get(0))));
                                    }
                                }.runTask(AuroraMCAPI.getCore());

                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    } else {
                        if (!player.hasPermission("disguise.custom")) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**\n" +
                                    "If you wish to use a random disguise, use **/disguise random**."));
                        }
                    }
                    break;
                case 2:
                    //Set skin and name seperately.
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}") && args.get(1).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (args.get(0).equalsIgnoreCase(player.getName())) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as yourself."));
                            return;
                        }
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                                if (uuid != null) {
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    if (rank != null) {
                                        if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they have a non-purchasable rank."));
                                            return;
                                        }
                                    }
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                        return;
                                    }
                                }

                                if (AuroraMCAPI.getDbManager().isUsernameBanned(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username is blacklisted."));
                                    return;
                                }

                                if (AuroraMCAPI.getFilter() == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                    return;
                                }
                                if (!AuroraMCAPI.getFilter().filter(args.get(0)).equals(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username would be filtered."));
                                    return;
                                }

                                if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                    return;
                                }

                                switch (player.getRank().getId()) {
                                    case 6:
                                    case 5: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928786853421809694/5KJn2zEdjECztUgLzUXNt38GRuLlcEOmcstENVTjv6reFUmHqRUmCWbIveFi8HZoy8FV");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s** with the skin of **%s**.", player.getName(), args.get(0), args.get(1))).setColor(((player.getRank().getId() == 5)?new Color(255, 170, 0):new Color(170, 0, 170))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                    default: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928785758599405680/gnzddoyMeiq9wMKxkKVRptVx3PX8EfGeRtFz9ZuXlpXGJV1G37l_g-KeJgLJIX5WNIUF");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s** with the skin of **%s**.", player.getName(), args.get(0), args.get(1))).setColor(((player.getRank().getId() >=11)?new Color(255, 85, 85):new Color(255, 170, 0))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        player.disguise(args.get(1), args.get(0), Rank.PLAYER);
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**. To undisguise, simply type **/undisguise**.", args.get(0), args.get(1))));
                                    }
                                }.runTask(AuroraMCAPI.getCore());
                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    } else {
                        if (!player.hasPermission("disguise.custom")) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**\n" +
                                    "If you wish to use a random disguise, use **/disguise random**."));
                        }
                    }
                    break;
                case 3:
                    //Set skin and name seperately.
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}") && args.get(1).matches("[a-zA-Z0-9_]{3,16}") && args.get(2).matches("[a-zA-Z]{3,16}")) {
                        if (args.get(0).equalsIgnoreCase(player.getName())) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as yourself."));
                            return;
                        }
                        Rank chosenRank = null;
                        for (Rank rank : Rank.values()) {
                            if (rank.getName().equalsIgnoreCase(args.get(2))) {
                                if (rank.hasPermission("moderation") || rank.hasPermission("disguise") || rank.hasPermission("debug.info") || rank.hasPermission("build")) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as that rank, you may only choose between premium ranks."));
                                    return;
                                }
                                chosenRank = rank;
                            }
                        }
                        if (chosenRank ==  null) {
                            chosenRank = Rank.PLAYER;
                        }

                        final Rank rank = chosenRank;

                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                                if (uuid != null) {
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    if (rank != null) {
                                        if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they have a non-purchasable rank."));
                                            return;
                                        }
                                    }
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                        return;
                                    }
                                }

                                if (AuroraMCAPI.getDbManager().isUsernameBanned(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username is blacklisted."));
                                    return;
                                }

                                if (AuroraMCAPI.getFilter() == null) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Message", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                                    return;
                                }

                                if (!AuroraMCAPI.getFilter().filter(args.get(0)).equals(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as their username would be filtered."));
                                    return;
                                }

                                if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they are currently online."));
                                    return;
                                }

                                switch (player.getRank().getId()) {
                                    case 6:
                                    case 5: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928786853421809694/5KJn2zEdjECztUgLzUXNt38GRuLlcEOmcstENVTjv6reFUmHqRUmCWbIveFi8HZoy8FV");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s** with the skin of **%s**.", player.getName(), args.get(0), args.get(1))).setColor(((player.getRank().getId() == 5)?new Color(255, 170, 0):new Color(170, 0, 170))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                    default: {
                                        DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928785758599405680/gnzddoyMeiq9wMKxkKVRptVx3PX8EfGeRtFz9ZuXlpXGJV1G37l_g-KeJgLJIX5WNIUF");

                                        discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Disguise Log").setDescription(String.format("**%s** has disguised as **%s** with the skin of **%s**.", player.getName(), args.get(0), args.get(1))).setColor(((player.getRank().getId() >=11)?new Color(255, 85, 85):new Color(255, 170, 0))));
                                        try {
                                            discordWebhook.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                                new BukkitRunnable(){
                                    @Override
                                    public void run() {

                                        player.disguise(args.get(1), args.get(0), rank);
                                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**. To undisguise, simply type **/undisguise**.", args.get(0), args.get(1))));
                                    }
                                }.runTask(AuroraMCAPI.getCore());
                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    } else {
                        if (!player.hasPermission("disguise.custom")) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**\n" +
                                    "If you wish to use a random disguise, use **/disguise random**."));
                        }
                    }
                    break;
                default:
                    if (!player.hasPermission("disguise.custom")) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**\n" +
                                "If you wish to use a random disguise, use **/disguise random**."));
                    }
                    break;
            }
        } else {
            if (!player.hasPermission("disguise.custom")) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise random**"));
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**\n" +
                        "If you wish to use a random disguise, use **/disguise random**."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
