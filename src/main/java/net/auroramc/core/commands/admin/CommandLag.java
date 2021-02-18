package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLag extends Command {


    public CommandLag() {
        super("lag", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("all"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        int threadsCurrentlyExecuting = Bukkit.getScheduler().getActiveWorkers().size();
        int threadsAwaitingExecution = Bukkit.getScheduler().getPendingTasks().size();
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Resource Monitor", String.format("Server lag statistics:\n" +
                "Used RAM: **%sMB**\n" +
                "Total RAM: **%sMB**\n" +
                "Threads Executing: **%s**\n" +
                "Threads Scheduled: **%s**\n" +
                "TPS (1m, 5m, 15m): **%s**, **%s**, **%s**\n" +
                "Stored values:\n" +
                " - Players: **%s**\n" +
                " - Open GUI's: **%s**", bean.getHeapMemoryUsage().getUsed()/1048576,Runtime.getRuntime().maxMemory()/1048576, threadsCurrentlyExecuting, threadsAwaitingExecution, new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[0]),new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[1]),new DecimalFormat("##.##").format(MinecraftServer.getServer().recentTps[2]), AuroraMCAPI.getPlayers().size(), AuroraMCAPI.getOpenGUIs())));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
