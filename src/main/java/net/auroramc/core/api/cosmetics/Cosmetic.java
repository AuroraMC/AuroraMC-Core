package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.permissions.Rank;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Cosmetic {

    private final int id;
    private final CosmeticType type;
    private final String name;
    private final String displayName;
    private final String description;
    private final UnlockMode unlockMode;
    private final int currency;
    private final List<Permission> permissions;
    private final List<Rank> ranks;
    private final String unlockMessage;
    private final boolean showIfNotUnlocked;
    private final Material material;
    private final short data;

    public Cosmetic(int id, CosmeticType type, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, Material material, short data) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.unlockMode = unlockMode;
        this.currency = currency;
        this.permissions = permissions;
        this.ranks = ranks;
        this.unlockMessage = unlockMessage;
        this.showIfNotUnlocked = showIfNotUnlocked;
        this.material = material;
        this.data = data;
    }

    public abstract void onEquip(AuroraMCPlayer player);

    public abstract void onUnequip(AuroraMCPlayer player);

    public ItemStack getItem(AuroraMCPlayer player) {
        Material material = this.material;
        short data = this.data;
        boolean hasUnlocked = hasUnlocked(player);
        if (!hasUnlocked) {
            material = Material.INK_SACK;
            data = 8;
        }

        ItemStack item = new ItemStack(material, 1, data);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(AuroraMCAPI.getFormatter().convert(displayName));

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(Arrays.asList(AuroraMCAPI.getFormatter().convert(("&r" + WordUtils.wrap(description, 40, ";&r", false))).split(";")));
        lore.add("");
        if (hasUnlocked) {
            if (player.getActiveCosmetics().get(type) != null) {
                if (player.getActiveCosmetics().get(type).equals(this)) {
                    lore.add(AuroraMCAPI.getFormatter().convert("&cClick to disable!"));
                } else {
                    lore.add(AuroraMCAPI.getFormatter().convert("&aClick to enable!"));
                }
            } else {
                lore.add(AuroraMCAPI.getFormatter().convert("&aClick to enable!"));
            }
        } else {
            if (unlockMode == UnlockMode.TICKETS) {
                if (player.getBank().getTickets() >= currency) {
                    lore.add(AuroraMCAPI.getFormatter().convert(String.format("&eClick to unlock for %s tickets!", currency)));
                } else {
                    lore.add(AuroraMCAPI.getFormatter().convert("&cYou have insufficient funds"));
                    lore.add(AuroraMCAPI.getFormatter().convert("&cto purchase this cosmetic."));
                }
            } else {
                lore.add(AuroraMCAPI.getFormatter().convert("&9" + unlockMessage));
            }
        }

        meta.setLore(lore);
        if (player.getActiveCosmetics().get(type) != null) {
            if (player.getActiveCosmetics().get(type).equals(this)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public CosmeticType getType() {
        return type;
    }

    public int getCurrency() {
        return currency;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UnlockMode getUnlockMode() {
        return unlockMode;
    }

    public String getUnlockMessage() {
        return unlockMessage;
    }

    public boolean showIfNotUnlocked() {
        return showIfNotUnlocked;
    }

    public enum CosmeticType {
        PARTICLE("Particle Effect"),
        PET("Pet"),
        HAT("Hat"),
        BANNER("Banner"),
        MORPH("Morph"),
        KILL_MESSAGE("Kill Message"),
        PROJECTILE_TRAIL("Projectile Trail"),
        DEATH_EFFECT("Death Effect"),
        WIN_EFFECT("Win Effect"),
        GADGET("Gadget"),
        FRIEND_STATUS("Friend Status");

        static {
            HAT.conflicts = new CosmeticType[]{BANNER};
            BANNER.conflicts = new CosmeticType[]{HAT};
        }

        private final String name;
        private CosmeticType[] conflicts;

        CosmeticType(String name) {
            this.name = name;
            conflicts = new CosmeticType[]{};
        }

        public String getName() {
            return this.name;
        }

        public CosmeticType[] getConflicts() {
            return conflicts;
        }
    }

    public enum UnlockMode {
        PERMISSION,
        RANK,
        TICKETS,
        STORE_PURCHASE,
        CRATE,
        ALL
    }

    public boolean hasUnlocked(AuroraMCPlayer player) {
        switch (unlockMode) {
            case ALL:
                return true;
            case RANK: {
                if (player.hasPermission("admin")) {
                    return true;
                }
                for (Rank rank : ranks) {
                    if (player.getRank().isParent(rank)) {
                        return true;
                    }
                }
                return false;
            }
            case PERMISSION: {
                if (player.hasPermission("admin")) {
                    return true;
                }
                for (Permission permission : permissions) {
                    if (player.hasPermission(permission.getId())) {
                        return true;
                    }
                }
                return false;
            }
            case STORE_PURCHASE:
            case CRATE:
            case TICKETS:{
                return player.getUnlockedCosmetics().contains(this);
            }
            default: {
                return player.hasPermission("all");
            }
        }
    }

}
