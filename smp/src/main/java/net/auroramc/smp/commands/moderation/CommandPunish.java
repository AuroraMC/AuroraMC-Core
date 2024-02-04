/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.punish.Punish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandPunish extends ServerCommand {

    public CommandPunish() {
        super("punish", Arrays.asList("punishuser","pu"), Arrays.asList(Permission.MODERATION, Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 1) {
            AuroraMCServerPlayer target = ServerAPI.getDisguisedPlayer(args.get(0));
            if (target == null) {
                target = ServerAPI.getPlayer(args.get(0));
            }

            if (target != null) {
                args.remove(0);
                Punish punish = new Punish(player, target.getName(), target.getId(), String.join(" ", args));
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
                            player.sendMessage(TextFormatter.pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot be punished.", args.get(0))));
                            return;
                        }

                        String name = AuroraMCAPI.getDbManager().getNameFromID(id);

                        args.remove(0);
                        Punish punish = new Punish(player, name, id, String.join(" ", args));
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                punish.open(player);
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Punish","Invalid syntax. Correct syntax: **/punish [user] [extra notes]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int amountArguments) {
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
