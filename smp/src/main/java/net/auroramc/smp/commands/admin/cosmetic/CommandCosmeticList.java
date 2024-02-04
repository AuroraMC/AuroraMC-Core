/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.commands.admin.cosmetic;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandCosmeticList extends ServerCommand {

    public CommandCosmeticList() {
        super("list", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("Looking up cosmetics for player [**%s**].", args.get(0))));
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id == -1) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("No results found for player [**%s**]. Try searching for an old name.", args.get(0))));
                        return;
                    }
                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);
                    List<Cosmetic> cosmetics = AuroraMCAPI.getDbManager().getUnlockedCosmetics(uuid);
                    StringBuilder sb = new StringBuilder();
                    for (Cosmetic cosmetic : cosmetics) {
                        sb.append(" - **").append(ChatColor.stripColor(TextFormatter.convert(cosmetic.getName()))).append(" ").append(cosmetic.getType().getName()).append("**\n");
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("Cosmetics unlocked for player [**%s**]:\n", args.get(0)) + sb.toString().trim()));

                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic list [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
