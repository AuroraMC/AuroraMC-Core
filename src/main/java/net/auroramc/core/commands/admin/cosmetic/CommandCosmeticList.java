/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.cosmetic;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandCosmeticList extends Command {

    public CommandCosmeticList() {
        super("list", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("Looking up cosmetics for player [**%s**].", args.get(0))));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id == -1) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("No results found for player [**%s**]. Try searching for an old name.", args.get(0))));
                        return;
                    }
                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);
                    List<Cosmetic> cosmetics = AuroraMCAPI.getDbManager().getUnlockedCosmetics(uuid);
                    StringBuilder sb = new StringBuilder();
                    for (Cosmetic cosmetic : cosmetics) {
                        sb.append(" - **").append(cosmetic.getName()).append(" ").append(cosmetic.getType().getName()).append("**\n");
                    }
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("Cosmetics unlocked for player [**%s**]:\n", args.get(0)) + sb.toString()));

                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic list [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
