/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.general.ignore;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.player.IgnoredPlayer;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnoreAdd extends ServerCommand {

    public CommandIgnoreAdd() {
        super("add", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getName().equalsIgnoreCase(args.get(0)) || player.getName().equalsIgnoreCase(args.get(0))) {
                player.sendMessage(TextFormatter.pluginMessage("Ignore", "You cannot ignore yourself, silly!"));
                return;
            }
            AuroraMCServerPlayer target = ServerAPI.getPlayer(args.get(0));
            if (target != null) {
                if (player.isIgnored(target.getId())) {
                    player.sendMessage(TextFormatter.pluginMessage("Ignore", "You already have that player ignored."));
                    return;
                }
                player.addIgnored(new IgnoredPlayer(target.getId(), target.getName()), true);
                player.sendMessage(TextFormatter.pluginMessage("Ignore", String.format("**%s** added to your ignore list.", target.getName())));
            } else {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        int target = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                        if (target != -1) {
                            if (player.isIgnored(target)) {
                                player.sendMessage(TextFormatter.pluginMessage("Ignore", "You already have that player ignored."));
                                return;
                            }
                            String name = AuroraMCAPI.getDbManager().getNameFromID(target);
                            IgnoredPlayer ignoredPlayer = new IgnoredPlayer(target, name);
                            player.addIgnored(ignoredPlayer, true);
                            player.sendMessage(TextFormatter.pluginMessage("Ignore", String.format("**%s** added to your ignore list.", name)));
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Ignore", String.format("No matches found for [**%s**]", args.get(0))));
                        }
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore add [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
