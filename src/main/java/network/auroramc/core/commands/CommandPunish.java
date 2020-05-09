package network.auroramc.core.commands;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.UUIDUtil;
import network.auroramc.core.gui.SetRank;
import network.auroramc.core.gui.punish.Punish;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandPunish extends Command {

    public CommandPunish() {
        super("punish", new ArrayList<>(Arrays.asList("punishuser","p")), new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("moderation"), AuroraMCAPI.getPermissions().get("admin"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                if (Bukkit.getPlayer(args.get(0)) != null) {
                    if (Bukkit.getPlayer(args.get(0)).isOnline()) {
                        AuroraMCPlayer target = AuroraMCAPI.getPlayer(Bukkit.getPlayer(args.get(0)));
                        if (target != null) {
                            args.remove(0);
                            Punish punish = new Punish(player, target.getPlayer().getName(), target.getId(), String.join(" ", args));
                            punish.open(player);
                            AuroraMCAPI.openGUI(player, punish);
                            return;
                        }
                    }
                }

                for (AuroraMCPlayer target : AuroraMCAPI.getPlayers()) {
                    if (target.getActiveDisguise() != null) {
                        if (target.getName().equalsIgnoreCase(args.get(0))) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","That user is disguised. Please gather evidence and report to an admin. This punish usage has been logged."));
                        }
                    }
                }

                String name = args.remove(0);

                //The user is definitely not online, get from the Database.
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(name);
                        if (uuid == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("No matches for [**%s**]", name)));
                            return;
                        }

                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                        if (id < 1) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot receive be punished.", name)));
                            return;
                        }

                        Punish punish = new Punish(player, name, id, String.join(" ", args));
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                AuroraMCAPI.openGUI(player, punish);
                                punish.open(player);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","That is not a valid username."));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Invalid syntax. Correct syntax: **/punish [user] [extra notes]**"));
        }
    }
}
