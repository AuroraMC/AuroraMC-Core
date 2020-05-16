package network.auroramc.core.commands;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.UUIDUtil;
import network.auroramc.core.gui.punish.PunishmentHistoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandPunishHistory extends Command {


    public CommandPunishHistory() {
        super("punishhistory", new ArrayList<>(Arrays.asList("ph","history","punishmenthistory")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("player"))), true, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            if (player.hasPermission("moderation")) {
                if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            UUID uuid = UUIDUtil.getUUID(args.get(0));
                            if (uuid == null) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("No matches for [**%s**]", args.get(0))));
                                return;
                            }

                            int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                            if (id < 1) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot have received a punishment.", args.get(0))));
                                return;
                            }

                            String name = args.remove(0);
                            PunishmentHistoryGUI gui = new PunishmentHistoryGUI(player, id, name, ((args.size() > 0)?String.join(" ", args):null), uuid);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    gui.open(player);
                                    AuroraMCAPI.openGUI(player, gui);
                                }
                            }.runTask(AuroraMCAPI.getCore());
                        }
                    }.runTaskAsynchronously(AuroraMCAPI.getCore());
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Invalid syntax. Correct syntax: **/punishhistory [player] [reason]**"));
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punishments","You cannot view the punishment history of another player."));
            }
        } else {
            new BukkitRunnable(){
                @Override
                public void run() {
                    PunishmentHistoryGUI gui = new PunishmentHistoryGUI(player, player.getId(), player.getPlayer().getName(), null, null);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gui.open(player);
                            AuroraMCAPI.openGUI(player, gui);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            if (player.hasPermission("moderation")) {
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                        completions.add(player1.getName());
                    }
                }
            }
        }
        return completions;
    }
}
