package net.auroramc.core.cosmetics.banners;

import net.auroramc.core.api.cosmetics.Banner;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Plus extends Banner {

    public static final Map<Character, DyeColor> colorMappings;

    static {
        colorMappings = new HashMap<>();
        colorMappings.put('0', DyeColor.BLACK);
        colorMappings.put('1', DyeColor.BLUE);
        colorMappings.put('2', DyeColor.GREEN);
        colorMappings.put('3', DyeColor.CYAN);
        colorMappings.put('4', DyeColor.RED);
        colorMappings.put('5', DyeColor.PURPLE);
        colorMappings.put('6', DyeColor.ORANGE);
        colorMappings.put('7', DyeColor.GRAY);
        colorMappings.put('8', DyeColor.SILVER);
        colorMappings.put('9', DyeColor.CYAN);
        colorMappings.put('a', DyeColor.LIME);
        colorMappings.put('b', DyeColor.LIGHT_BLUE);
        colorMappings.put('c', DyeColor.RED);
        colorMappings.put('d', DyeColor.MAGENTA);
        colorMappings.put('e', DyeColor.YELLOW);
        colorMappings.put('f', DyeColor.WHITE);
    }

    public Plus() {
        super(16, "Plus", "&3Plus", "&3Show of your swaggy plus status. Changes based on your chosen plus colour", UnlockMode.PERMISSION, -1, Collections.singletonList(Permission.PLUS), new ArrayList<>(), "Purchase Plus to unlock this banner!", new ArrayList<>(), DyeColor.BLACK);
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.setBaseColor(DyeColor.WHITE);
        DyeColor color = DyeColor.CYAN;
        if (player.getActiveSubscription() != null) {
            color = colorMappings.get(player.getActiveSubscription().getColor());
        }
        meta.addPattern(new Pattern(color, PatternType.STRIPE_CENTER));
        meta.addPattern(new Pattern(color, PatternType.STRIPE_MIDDLE));
        meta.addPattern(new Pattern(color, PatternType.STRIPE_TOP));
        meta.addPattern(new Pattern(color, PatternType.STRIPE_BOTTOM));
        meta.addPattern(new Pattern(color, PatternType.BORDER));
        player.getPlayer().getInventory().setHelmet(item);
    }
}
