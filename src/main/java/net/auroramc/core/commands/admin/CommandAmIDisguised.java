/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Disguise;
import net.auroramc.core.api.utils.DiscordWebhook;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandAmIDisguised extends Command {


    public CommandAmIDisguised() {
        super("amidisguised", Collections.singletonList("disguised?"), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (player.isDisguised()) {
            player.sendTitle("§a§lYes.", "", 0, 100, 20, ChatColor.GREEN, ChatColor.GRAY, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().convert("&a&lYes."));
        } else {
            player.sendTitle("§c§lNo.", "", 0, 100, 20, ChatColor.RED, ChatColor.GRAY, true, false);
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().convert("&c&lNo."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
