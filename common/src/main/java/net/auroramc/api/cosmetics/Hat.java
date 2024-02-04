/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.CosmeticFactory;
import net.auroramc.api.abstraction.ItemFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.Item;
import org.apache.commons.text.WordUtils;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Hat extends Cosmetic {

    private Item head;

    public Hat(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String headUrl, boolean showIfNotUnlocked, Rarity rarity) {
        super(id, CosmeticType.HAT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage, showIfNotUnlocked, "SKULL_ITEM", (short)3, rarity);

        head = ItemFactory.generateHead(displayName, 1, null, false, Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", headUrl).getBytes()));
    }

    @Override
    public final void onEquip(AuroraMCPlayer player) {
        CosmeticFactory.onEquipHat(player, this);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        CosmeticFactory.onUnequipHat(player, this);
    }

    @Override
    public Item getItem(AuroraMCPlayer player) {
        Item item = super.getItem(player);
        item.setBase64(head.getBase64());
        return item;
    }
}
