/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.ItemFactory;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.utils.Item;
import net.auroramc.api.utils.TextFormatter;
import org.apache.commons.text.WordUtils;

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
    private final String material;
    private final short data;
    private final Rarity rarity;

    public Cosmetic(int id, CosmeticType type, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, boolean showIfNotUnlocked, String material, short data, Rarity rarity) {
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
        this.rarity = rarity;
    }

    public abstract void onEquip(AuroraMCPlayer player);

    public abstract void onUnequip(AuroraMCPlayer player);

    public boolean shouldBypassDisabled() {
        return type.shouldBypassDisabled();
    }

    public Item getItem(AuroraMCPlayer player) {
        String material = this.material;
        short data = this.data;
        boolean hasUnlocked = hasUnlocked(player);
        if (!hasUnlocked) {
            material = "INK_SACK";
            data = 8;
        }

        String name = TextFormatter.convert(displayName);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(Arrays.asList(TextFormatter.convert(("&r&f" + WordUtils.wrap(description, 40, ";&r&f", false))).split(";")));
        lore.add(TextFormatter.convert("&r&fRarity: " + rarity.getDisplayName()));
        lore.add("");
        if (hasUnlocked) {
            if (player.getActiveCosmetics().get(type) != null) {
                if (player.getActiveCosmetics().get(type).equals(this)) {
                    lore.add(TextFormatter.convert("&cClick to disable!"));
                } else {
                    lore.add(TextFormatter.convert("&aClick to enable!"));
                }
            } else {
                lore.add(TextFormatter.convert("&aClick to enable!"));
            }
        } else {
            if (unlockMode == UnlockMode.TICKETS) {
                if (player.getBank().getTickets() >= currency) {
                    lore.add(TextFormatter.convert(String.format("&eClick to unlock for %s tickets!", currency)));
                } else {
                    lore.add(TextFormatter.convert("&cYou have insufficient funds"));
                    lore.add(TextFormatter.convert("&cto purchase this cosmetic."));
                }
            } else {
                lore.addAll(Arrays.asList(TextFormatter.convert("&9" + WordUtils.wrap(unlockMessage, 40, ";&9", false)).split(";")));
            }
        }
        return ItemFactory.generateItem(material, name, 1, String.join(";", lore), data, player.getActiveCosmetics().get(type) != null && player.getActiveCosmetics().get(type).equals(this));
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

    public Rarity getRarity() {
        return rarity;
    }

    public String getMaterial() {
        return material;
    }

    public short getData() {
        return data;
    }

    public enum CosmeticType {
        PARTICLE("Particle Effect", false),
        HAT("Hat", false),
        BANNER("Banner", false),
        KILL_MESSAGE("Kill Message", true),
        PROJECTILE_TRAIL("Projectile Trail", true),
        DEATH_EFFECT("Death Effect", true),
        WIN_EFFECT("Win Effect", true),
        GADGET("Gadget", false),
        FRIEND_STATUS("Friend Status", true),
        SERVER_MESSAGE("Server Message", true),
        PLUS_SYMBOL("Plus Symbol", true),
        CHAT_EMOTE("Chat Emote", false);

        static {
            HAT.conflicts = new CosmeticType[]{BANNER};
            BANNER.conflicts = new CosmeticType[]{HAT};
        }

        private final String name;
        private CosmeticType[] conflicts;
        private boolean bypassDisabled;

        CosmeticType(String name, boolean bypassDisabled) {
            this.name = name;
            this.bypassDisabled = bypassDisabled;
            conflicts = new CosmeticType[]{};
        }

        public String getName() {
            return this.name;
        }

        public CosmeticType[] getConflicts() {
            return conflicts;
        }

        public boolean shouldBypassDisabled() {
            return bypassDisabled;
        }
    }

    public enum UnlockMode {
        PERMISSION,
        RANK,
        TICKETS,
        STORE_PURCHASE,
        //Adds to automatic loot pool of certain crates.
        CRATE,
        //Does not add to automatic loot pool of certain crates.
        SPECIAL_CRATE,
        LEVEL,
        GIVEAWAY,
        ALL
    }

    public enum Rarity {
        COMMON("&fCommon"),
        UNCOMMON("&aUncommon"),
        RARE("&9Rare"),
        EPIC("&5Epic"),
        LEGENDARY("&6Legendary"),
        MYTHICAL("&cMythical");

        private final String displayName;
        Rarity(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
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
            case GIVEAWAY:
            case LEVEL:
            case TICKETS:{
                return player.getUnlockedCosmetics().contains(this);
            }
            default: {
                return player.hasPermission("all");
            }
        }
    }



}
