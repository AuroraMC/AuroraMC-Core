/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.*;
import net.auroramc.api.player.AuroraMCPlayer;

public class CosmeticFactory {

    public static void onEquipBanner(AuroraMCPlayer player, Banner banner) {
        AuroraMCAPI.getAbstractedMethods().onEquipBanner(player, banner);
    }

    public static void onUnequipBanner(AuroraMCPlayer player, Banner banner) {
        AuroraMCAPI.getAbstractedMethods().onUnequipBanner(player, banner);
    }

    public static void onEquipGadget(AuroraMCPlayer player, Gadget gadget) {
        AuroraMCAPI.getAbstractedMethods().onEquipGadget(player, gadget);
    }

    public static void onUnequipGadget(AuroraMCPlayer player, Gadget gadget) {
        AuroraMCAPI.getAbstractedMethods().onUnequipGadget(player, gadget);
    }

    public static void onEquipHat(AuroraMCPlayer player, Hat hat) {
        AuroraMCAPI.getAbstractedMethods().onEquipHat(player, hat);
    }

    public static void onUnequipHat(AuroraMCPlayer player, Hat hat) {
        AuroraMCAPI.getAbstractedMethods().onUnequipHat(player, hat);
    }

    public static void onEquipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
        AuroraMCAPI.getAbstractedMethods().onEquipSymbol(player, symbol);
    }

    public static void onUnequipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
        AuroraMCAPI.getAbstractedMethods().onUnequipSymbol(player, symbol);
    }

    public static void onExecuteCosmetic(AuroraMCPlayer player, Cosmetic cosmetic) {
        if (!AuroraMCAPI.getCosmeticExecutors().containsKey(cosmetic.getId())) {
            throw new IllegalStateException("Cosmetic " + cosmetic.getName() + " does not have a valid executor for cosmetic type " + cosmetic.getType().name());
        }
        AuroraMCAPI.getCosmeticExecutors().get(cosmetic.getId()).execute(player);
    }

    public static void onExecuteCosmetic(AuroraMCPlayer player, Cosmetic cosmetic, double x, double y, double z) {
        if (!AuroraMCAPI.getCosmeticExecutors().containsKey(cosmetic.getId())) {
            throw new IllegalStateException("Cosmetic " + cosmetic.getName() + " does not have a valid executor for cosmetic type " + cosmetic.getType().name());
        }
        AuroraMCAPI.getCosmeticExecutors().get(cosmetic.getId()).execute(player, x, y, z);
    }

    public static void onCancelCosmetic(AuroraMCPlayer player, Cosmetic cosmetic) {
        if (!AuroraMCAPI.getCosmeticExecutors().containsKey(cosmetic.getId())) {
            throw new IllegalStateException("Cosmetic " + cosmetic.getName() + " does not have a valid executor for cosmetic type " + cosmetic.getType().name());
        }
        AuroraMCAPI.getCosmeticExecutors().get(cosmetic.getId()).cancel(player);
    }

    public static void onExecuteCosmetic(Object entity, Cosmetic cosmetic) {
        if (!AuroraMCAPI.getCosmeticExecutors().containsKey(cosmetic.getId())) {
            throw new IllegalStateException("Cosmetic " + cosmetic.getName() + " does not have a valid executor for cosmetic type " + cosmetic.getType().name());
        }
        AuroraMCAPI.getCosmeticExecutors().get(cosmetic.getId()).execute(entity);
    }

}
