/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.CosmeticFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.Item;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Banner extends Cosmetic {

    private final List<Item.Pattern> patterns;
    private final String base;

    public Banner(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, List<Item.Pattern> patterns, String base, boolean showIfNotUnlocked, Rarity rarity) {
        super(id, CosmeticType.BANNER, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, "BANNER", (short) 1, rarity);
        this.patterns = patterns;
        this.base = base;
    }

    public List<Item.Pattern> getPatterns() {
        return new ArrayList<>(patterns);
    }

    public String getBase() {
        return base;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        CosmeticFactory.onEquipBanner(player, this);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        CosmeticFactory.onUnequipBanner(player, this);
    }

    @Override
    public Item getItem(AuroraMCPlayer player) {
        Item item = super.getItem(player);
        item.setBaseColour(base);
        item.setPatterns(new ArrayList<>(patterns));
        return item;
    }
}
