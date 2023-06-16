/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.utils;

import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventoryUtil {

    public static String[] playerInventoryToBase64(AuroraMCServerPlayer player) throws IllegalStateException {
        String content = itemStackArrayToBase64(player.getInventory().getContents());
        String armor = itemStackArrayToBase64(player.getInventory().getArmorContents());
        String storage = itemStackArrayToBase64(player.getInventory().getStorageContents());
        String extra = itemStackArrayToBase64(player.getInventory().getExtraContents());
        String ender = itemStackArrayToBase64(player.getEnderChest().getContents());

        return new String[] { content, armor , storage, extra, ender};
    }

    public static void playerInventoryFromBase64(AuroraMCServerPlayer player, String[] inventory) throws IOException {

        player.getInventory().setContents(itemStackArrayFromBase64(inventory[0]));
        player.getInventory().setArmorContents(itemStackArrayFromBase64(inventory[1]));
        player.getInventory().setStorageContents(itemStackArrayFromBase64(inventory[2]));
        player.getInventory().setExtraContents(itemStackArrayFromBase64(inventory[3]));
        player.getEnderChest().setContents(itemStackArrayFromBase64(inventory[4]));
    }

    /**
     *
     * A method to serialize an {@link ItemStack} array to Base64 String.
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     * @throws IllegalStateException
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Gets an array of ItemStacks from Base64 string.
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
