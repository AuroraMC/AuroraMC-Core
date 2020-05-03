package network.auroramc.core.commands;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.UUIDUtil;
import network.auroramc.core.gui.SetRank;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandSetRank extends Command {


    public CommandSetRank() {
        super("setrank", new ArrayList<>(Arrays.asList("sr", "updaterank")), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("admin"),AuroraMCAPI.getPermissions().get("btm"),AuroraMCAPI.getPermissions().get("staffmanagement"),AuroraMCAPI.getPermissions().get("support"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            String name = args.get(0);
            if (name.matches("[a-zA-Z0-9_]{3,16}")) {
                if (name.toLowerCase().equals(player.getPlayer().getName().toLowerCase())) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set your own rank."));
                    return;
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(name);
                        if (uuid == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("No matches for [**%s**]", name)));
                            return;
                        }

                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                        if (id < 1) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("User [**%s**] has never joined the network, so cannot receive a rank.", name)));
                            return;
                        }

                        Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                        if (rank == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "There has been an error trying to retrieve their rank from the Database. Please try again."));
                            return;
                        }

                        if (player.getRank().hasPermission("admin")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.FULL);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                    AuroraMCAPI.openGUI(player, setrank);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        } else if (player.hasPermission("btm")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.BTM);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                    AuroraMCAPI.openGUI(player, setrank);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        } else if (player.hasPermission("staffmanagement")) {
                            if (rank.getId() >= player.getRank().getId()) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set the rank of a user who has a higher or the same rank as you."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.STM);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                    AuroraMCAPI.openGUI(player, setrank);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        } else if (player.hasPermission("support")) {
                            if (rank.getId() > 4) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set the rank of a user who is a staff member."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.SUPPORT);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                    AuroraMCAPI.openGUI(player, setrank);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        } else {
                            if (rank.hasPermission("admin") || rank.getId() == player.getRank().getId()) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "You cannot set the rank of a user who is leadership staff."));
                                return;
                            }
                            SetRank setrank = new SetRank(player, name, uuid, id, rank, SetRank.SetRankVariation.FULL);
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    setrank.open(player);
                                    AuroraMCAPI.openGUI(player, setrank);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "Invalid syntax. Correct syntax: **/setrank [username]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", "Invalid syntax. Correct syntax: **/setrank [username]**"));
        }
    }
}
