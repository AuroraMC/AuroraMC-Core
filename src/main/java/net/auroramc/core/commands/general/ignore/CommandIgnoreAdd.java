/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.general.ignore;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.IgnoredPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnoreAdd extends Command {

    public CommandIgnoreAdd() {
        super("add", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getName().equalsIgnoreCase(args.get(0)) || player.getPlayer().getName().equalsIgnoreCase(args.get(0))) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "You cannot ignore yourself, silly!"));
                return;
            }
            AuroraMCPlayer target = AuroraMCAPI.getPlayer(args.get(0));
            if (target != null) {
                if (player.isIgnored(target.getId())) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "You already have that player ignored."));
                    return;
                }
                player.addIgnored(new IgnoredPlayer(target.getId(), target.getPlayer().getName()));
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", String.format("**%s** added to your ignore list.", target.getPlayer().getName())));
            } else {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        int target = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                        if (target != -1) {
                            if (player.isIgnored(target)) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "You already have that player ignored."));
                                return;
                            }
                            String name = AuroraMCAPI.getDbManager().getNameFromID(target);
                            IgnoredPlayer ignoredPlayer = new IgnoredPlayer(target, name);
                            player.addIgnored(ignoredPlayer);
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", String.format("**%s** added to your ignore list.", name)));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", String.format("No matches found for [**%s**]", args.get(0))));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore add [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
