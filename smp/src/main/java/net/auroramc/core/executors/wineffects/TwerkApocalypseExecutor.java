/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.executors.wineffects;

import com.mojang.authlib.GameProfile;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TwerkApocalypseExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public TwerkApocalypseExecutor() {
        super(AuroraMCAPI.getCosmetics().get(601));
    }

    @Override
    public void execute(AuroraMCPlayer pl1) {
    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
