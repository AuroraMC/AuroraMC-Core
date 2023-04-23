/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.api;

import net.auroramc.api.abstraction.AbstractedMethods;
import net.auroramc.api.cosmetics.*;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;
import net.auroramc.common.cosmetics.crates.DiamondCrate;
import net.auroramc.common.cosmetics.crates.EmeraldCrate;
import net.auroramc.common.cosmetics.crates.GoldCrate;
import net.auroramc.common.cosmetics.crates.IronCrate;
import net.auroramc.proxy.api.player.ProxyDisguise;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public class ProxyAbstractedMethods extends AbstractedMethods {


    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        return new ProxyDisguise(player, name, skin, rank);
    }

    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        return new ProxyDisguise(player, name, skin, signature, rank);
    }

    @Override
    public Disguise newDisguise(String name, String skin, String signature, Rank rank) {
        return new ProxyDisguise(name, skin, signature, rank);
    }

    @Override
    public void scheduleAsyncTask(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), runnable);
    }

    @Override
    public void scheduleSyncTask(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), runnable);
    }

    @Override
    public Crate generateIronCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new IronCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateGoldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new GoldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateDiamondCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new DiamondCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateEmeraldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new EmeraldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public void onEquipBanner(AuroraMCPlayer player, Banner banner) {
    }

    @Override
    public void onUnequipBanner(AuroraMCPlayer player, Banner banner) {
    }

    @Override
    public void onEquipHat(AuroraMCPlayer player, Hat hat) {
    }

    @Override
    public void onUnequipHat(AuroraMCPlayer player, Hat hat) {
    }

    @Override
    public void onEquipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
    }

    @Override
    public void onUnequipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
    }

    @Override
    public void onEquipGadget(AuroraMCPlayer player, Gadget gadget) {
    }

    @Override
    public void onUnequipGadget(AuroraMCPlayer player, Gadget gadget) {
    }

    @Override
    public void broadcastModerationMessage(BaseComponent message, AuroraMCPlayer issuer) {

    }
}
