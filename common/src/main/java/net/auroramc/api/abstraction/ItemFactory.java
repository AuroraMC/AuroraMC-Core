/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.abstraction;

import net.auroramc.api.utils.Item;

public class ItemFactory {

    public static Item generateItem(String material) {
        return generateItem(material, null);
    }

    public static Item generateItem(String material, String name) {
        return generateItem(material, name, 1);
    }

    public static Item generateItem(String material, String name, int amount) {
        return generateItem(material, name, amount, null);
    }

    public static Item generateItem(String material, String name, int amount, String lore) {
        return generateItem(material, name, amount, lore, (short) 0);
    }

    public static Item generateItem(String material, String name, int amount, String lore, short data) {
        return generateItem(material, name, amount, lore, data, false);
    }

    public static Item generateItem(String material, String name, int amount, String lore, short data, boolean glowing) {
        return generateItem(material, name, amount, lore, data, glowing, "");
    }

    public static Item generateItem(String material, String name, int amount, String lore, short data, boolean glowing, String skullOwner) {
        return new Item(material, name, amount, lore, data, glowing, skullOwner);
    }

    public static Item generateItem(String material, String name, int amount, String lore, short data, boolean glowing, int r, int g, int b) {
        return new Item(material, name, amount, lore, data, glowing, r, g, b);
    }

    public static Item generateHead(String name, int amount, String lore, boolean glowing, String base64Skin) {
        Item item = new Item("SKULL_ITEM", name, amount, lore, (short)3, glowing, null);
        item.setBase64(base64Skin);
        return item;
    }

}
