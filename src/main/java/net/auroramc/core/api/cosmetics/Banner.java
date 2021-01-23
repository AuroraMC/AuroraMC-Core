package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Banner extends Cosmetic {

    private final List<Pattern> patterns;
    private final DyeColor base;

    public Banner(int id, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, List<Pattern> patterns, DyeColor base) {
        super(id, CosmeticType.BANNER, name, displayName, description, unlockMode, currency, permissions, ranks);
        this.patterns = patterns;
        this.base = base;
    }

    public List<Pattern> getPatterns() {
        return new ArrayList<>(patterns);
    }

    @Override
    public final void onEquip(AuroraMCPlayer player) {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.setBaseColor(base);
        for (Pattern pattern : patterns) {
            meta.addPattern(pattern);
        }
        player.getPlayer().getInventory().setHelmet(item);
    }

    @Override
    public final void onUnequip(AuroraMCPlayer player) {
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }
}
