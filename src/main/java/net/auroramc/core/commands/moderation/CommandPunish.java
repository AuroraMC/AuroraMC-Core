/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.punish.Punish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandPunish extends Command {

    public CommandPunish() {
        super("punish", Arrays.asList("punishuser","pu"), Arrays.asList(Permission.MODERATION, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            AuroraMCPlayer target = AuroraMCAPI.getDisguisedPlayer(args.get(0));
            if (target == null) {
                target = AuroraMCAPI.getPlayer(args.get(0));
            }

            if (target != null) {
                args.remove(0);
                Punish punish = new Punish(player, target.getName(), target.getId(), String.join(" ", args));
                AuroraMCAPI.openGUI(player, punish);
                punish.open(player);
            } else {
                //The user is definitely not online, get from the Database.
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        int id;
                        if (AuroraMCAPI.getDbManager().isAlreadyDisguise(args.get(0))) {
                            id = AuroraMCAPI.getDbManager().getAuroraMCID(AuroraMCAPI.getDbManager().getUUIDFromDisguise(args.get(0)));
                        } else {
                            id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                        }
                        if (id < 1) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot be punished.", args.get(0))));
                            return;
                        }

                        String name = AuroraMCAPI.getDbManager().getNameFromID(id);

                        args.remove(0);
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
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish","Invalid syntax. Correct syntax: **/punish [user] [extra notes]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
        ArrayList<String> completions = new ArrayList<>();
        if (amountArguments == 1) {
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(player1.getName());
                }
            }
        }
        return completions;
    }
}
