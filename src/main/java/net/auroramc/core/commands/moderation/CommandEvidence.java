package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.punishments.Punishment;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEvidence extends Command {


    public CommandEvidence() {
        super("evidence", new ArrayList<>(Arrays.asList("attachevidence","setevidence","addevidence")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("moderation"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            if (args.get(0).matches("[A-Z0-9]{8}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String code = args.remove(0);
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if (punishment != null) {
                            AuroraMCAPI.getDbManager().attachEvidence(code, String.join(" ", args));
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Evidence", "The evidence has been attached to the punishment."));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Evidence", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Evidence", "Invalid syntax. Correct syntax: **/evidence [Punishment ID] [Evidence]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Evidence", "Invalid syntax. Correct syntax: **/evidence [Punishment ID] [Evidence]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
        return new ArrayList<>();
    }
}
