/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.general;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.SMPLocation;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandSpawn extends ServerCommand {


    public CommandSpawn() {
        super("spawn", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (player.getLastTeleport() != 0 && ((System.currentTimeMillis() - player.getLastTeleport())/1000) < 60) {
            player.sendMessage(TextFormatter.pluginMessage("Teleport", "You cannot teleport for **" + String.format("%.2f", (60-((System.currentTimeMillis() - player.getLastTeleport())/1000d))) + "**."));
            return;
        }
        player.setBackLocation(new SMPLocation(SMPLocation.Dimension.valueOf(ServerAPI.getCore().getConfig().getString("type")), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw(), SMPLocation.Reason.HOME));
        switch (((ServerInfo) AuroraMCAPI.getInfo()).getServerType().getString("smp_type")) {
            case "OVERWORLD": {
                player.setLastTeleport(System.currentTimeMillis());
                player.teleport(new Location(Bukkit.getWorld("smp"), 0.5, 63, 0.5, 0, 0));
                break;
            }
            case "END":
            case "NETHER": {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("SMPOverworld");
                        out.writeUTF(player.getUniqueId().toString());
                        player.saveData();
                        AuroraMCAPI.getDbManager().setSMPLogoutLocation(player.getUniqueId(), new SMPLocation(SMPLocation.Dimension.OVERWORLD, 0, 63, 0, 0, 0, SMPLocation.Reason.HOME));
                        player.sendPluginMessage(out.toByteArray());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());

                break;
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
