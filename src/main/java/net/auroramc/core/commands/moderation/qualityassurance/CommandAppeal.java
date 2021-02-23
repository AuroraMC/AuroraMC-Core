package net.auroramc.core.commands.moderation.qualityassurance;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandAppeal extends Command {

    public CommandAppeal() {
        super("appeal", Collections.singletonList("acceptappeal"), Arrays.asList(Permission.DEBUG_INFO, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            if(args.get(0).matches("[A-Z0-9]{8}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String code = args.remove(0);
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if (punishment != null) {
                            if(punishment.getStatus() == 1 || punishment.getStatus() == 3) {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(punishment.getPunished());
                                List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(punishment.getPunished());
                                if (args.get(0).equalsIgnoreCase("Reprieve")) {
                                    AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "Reprieve", punishment, uuid, punishments);
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("Reprieved Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        out.writeUTF(punishment.getPunishedName());
                                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                    }
                                } else if (args.get(0).equalsIgnoreCase("False")) {
                                    AuroraMCAPI.getDbManager().removePunishment("AuroraMCAppeals", System.currentTimeMillis(), "False", punishment, uuid, punishments);
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("False Punishment with ID [**%s**] has been removed successfully.", code)));
                                    if (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getType() == 1) {
                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Unmute");
                                        out.writeUTF(punishment.getPunishmentCode());
                                        out.writeUTF(punishment.getPunishedName());
                                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                                    }
                                } else {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False]"));
                                }
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "This punishment currently cannot be appealed."));
                            }
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False]"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/appeal [Punishment ID] [Reprieve | False]"));
        }


    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
