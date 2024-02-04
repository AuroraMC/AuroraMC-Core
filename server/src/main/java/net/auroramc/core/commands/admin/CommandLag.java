/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.api.utils.TimeLength;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLag extends ServerCommand {


    public CommandLag() {
        super("lag", Collections.emptyList(), Arrays.asList(Permission.ALL, Permission.DEBUG_INFO), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        long threadsCurrentlyExecuting = Bukkit.getScheduler().getActiveWorkers().size();
        long threadsAwaitingExecution = Bukkit.getScheduler().getPendingTasks().size();
        player.sendMessage(TextFormatter.pluginMessage("Resource Monitor", String.format("Server lag statistics:\n" +
                "Uptime: **%s**\n" +
                "Used RAM: **%sMB**\n" +
                "Total RAM: **%sMB**\n" +
                "Threads Executing: **%s**\n" +
                "Threads Scheduled: **%s**\n" +
                "TPS (1m, 5m, 15m): **%s**, **%s**, **%s**\n" +
                "Stored values:\n" +
                " - Players: **%s**\n" +
                " - Open GUI's: **%s**", new TimeLength((System.currentTimeMillis() - AuroraMCAPI.getStartTime())/3600000d), bean.getHeapMemoryUsage().getUsed()/1048576,Runtime.getRuntime().maxMemory()/1048576, threadsCurrentlyExecuting, threadsAwaitingExecution, new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[0]),new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[1]),new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[2]), ServerAPI.getPlayers().size(), ServerAPI.getOpenGUIs())));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
