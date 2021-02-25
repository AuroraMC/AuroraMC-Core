package net.auroramc.core.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.UUIDUtil;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandDisguise extends Command {


    public CommandDisguise() {
        super("disguise", Collections.emptyList(), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.size()) {
                case 1:
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (args.get(0).equalsIgnoreCase(player.getName())) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as yourself."));
                            return;
                        }
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                UUID uuid = UUIDUtil.getUUID(args.get(0));
                                if (uuid != null) {
                                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                                    if (rank != null) {
                                        if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they have a non-purchasable rank."));
                                            return;
                                        }
                                    }
                                }

                                //Hand this off to the Bungee to do some checks on the username to check that they:
                                //1 - are not online
                                //2 - are not listed in the username blacklist
                                //3 - would not get filtered if the username is put through the filter.
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("DisguiseCheck");
                                out.writeUTF(player.getName());
                                if (player.isDisguised()) {
                                    out.writeUTF(player.getName());
                                } else {
                                    out.writeUTF("-");
                                }
                                out.writeUTF(args.get(0));
                                //This is the variation of the command that was used, so the Bukkit plugin knows what message to send the player.
                                out.writeInt(1);
                                AuroraMCAPI.getPendingDisguiseChecks().put(player.getPlayer(),args.get(0) + ";" + args.get(0) + ";" + Rank.PLAYER.getId());
                                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                            }
                        }.runTaskAsynchronously(AuroraMCAPI.getCore());
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
                    }
                    break;
                case 2:
                    //Set skin and name seperately.
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}") && args.get(1).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (args.get(0).equalsIgnoreCase(player.getName())) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as yourself."));
                            return;
                        }
                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid != null) {
                            Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                            if (rank != null) {
                                if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as this player as they have a non-purchasable rank."));
                                    return;
                                }
                            }
                        }
                        //Hand this off to the Bungee to do some checks on the username to check that they:
                        //1 - are not online
                        //2 - are not listed in the username blacklist
                        //3 - would not get filtered if the username is put through the filter.
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("DisguiseCheck");
                        out.writeUTF(player.getName());
                        if (player.isDisguised()) {
                            out.writeUTF(player.getName());
                        } else {
                            out.writeUTF("-");
                        }
                        out.writeUTF(args.get(0));
                        //This is the variation of the command that was used, so the Bukkit plugin knows what message to send the player.
                        out.writeInt(2);
                        AuroraMCAPI.getPendingDisguiseChecks().put(player.getPlayer(),args.get(1) + ";" + args.get(0) + ";" + Rank.PLAYER.getId());
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
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

                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid != null) {
                            Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                            if (rank != null) {
                                if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You may not disguise as this player as they have a non-purchasable rank."));
                                    return;
                                }
                            }
                        }
                        //Hand this off to the Bungee to do some checks on the username to check that they:
                        //1 - are not online
                        //2 - are not listed in the username blacklistw r
                        //3 - would not get filtered if the username is put through the filter.
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("DisguiseCheck");
                        out.writeUTF(player.getName());
                        if (player.isDisguised()) {
                            out.writeUTF(player.getName());
                        } else {
                            out.writeUTF("-");
                        }
                        out.writeUTF(args.get(0));
                        //This is the variation of the command that was used, so the Bukkit plugin knows what message to send the player.
                        out.writeInt(2);
                        AuroraMCAPI.getPendingDisguiseChecks().put(player.getPlayer(),args.get(1) + ";" + args.get(0) + ";" + chosenRank.getId());
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
                    }
                    break;
                default:
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
                    break;
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
