/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.cosmetics.*;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public abstract class AbstractedMethods {

    public abstract Disguise newDisguise(AuroraMCPlayer player, String name, String skin, Rank rank);

    public abstract Disguise newDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank);

    public abstract Disguise newDisguise(String name, String skin, String signature, Rank rank);

    public abstract void scheduleAsyncTask(Runnable runnable);

    public abstract Object scheduleAsyncTaskLater(Runnable runnable, long delay);

    public abstract void scheduleSyncTask(Runnable runnable);

    public abstract Crate generateIronCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened);

    public abstract Crate generateGoldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened);

    public abstract Crate generateDiamondCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened);

    public abstract Crate generateEmeraldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened);

    public abstract void onEquipBanner(AuroraMCPlayer player, Banner banner);
    public abstract void onUnequipBanner(AuroraMCPlayer player, Banner banner);

    public abstract void onEquipHat(AuroraMCPlayer player, Hat hat);
    public abstract void onUnequipHat(AuroraMCPlayer player, Hat hat);

    public abstract void onEquipSymbol(AuroraMCPlayer player, PlusSymbol symbol);
    public abstract void onUnequipSymbol(AuroraMCPlayer player, PlusSymbol symbol);

    public abstract void onEquipGadget(AuroraMCPlayer player, Gadget gadget);
    public abstract void onUnequipGadget(AuroraMCPlayer player, Gadget gadget);

    public abstract void broadcastModerationMessage(BaseComponent message, AuroraMCPlayer issuer);

    public abstract void broadcastNovaMessage(BaseComponent message);

    public abstract void firePreferenceEvent(AuroraMCPlayer player);
    
}

