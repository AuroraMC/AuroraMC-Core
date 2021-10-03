package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.punish.AdminNotes;
import net.auroramc.core.gui.punish.PunishmentHistoryGUI;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandNotes extends Command {
    public CommandNotes() {
        super("notes", Collections.singletonList("adminnotes"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() >= 1) {
            if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                        if (id < 1) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Admin Notes", String.format("User [**%s**] has never joined the network, so cannot have received an Admin Note.", args.get(0))));
                            return;
                        }
                        String name = args.remove(0);
                        String extraDetails = null;
                        if (args.size() > 0) {
                            extraDetails = String.join(" ", args);
                        }
                        AdminNotes gui = new AdminNotes(player, name, id, extraDetails);
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
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Admin Notes", "Invalid syntax. Correct syntax: **/notes <player> [reason]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Admin Notes", "Invalid syntax. Correct syntax: **/notes <player> [reason]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
