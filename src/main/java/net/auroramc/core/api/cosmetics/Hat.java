package net.auroramc.core.api.cosmetics;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class Hat extends Cosmetic {

    private ItemStack head;

    public Hat(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String headUrl) {
        super(id, CosmeticType.HAT, name, displayName, description, unlockMode, currency, permissions, ranks, unlockMessage);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", headUrl).getBytes())));

        head = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setDisplayName(AuroraMCAPI.getFormatter().convert(AuroraMCAPI.getFormatter().highlight(displayName)));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return;
        }
        field.setAccessible(true);
        try {
            field.set(meta, profile);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
    }

    @Override
    public final void onEquip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(head);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }
}
