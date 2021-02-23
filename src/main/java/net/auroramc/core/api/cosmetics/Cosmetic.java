package net.auroramc.core.api.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;

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

    public Cosmetic(int id, CosmeticType type, String name, String displayName, String description, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage) {
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

        AuroraMCAPI.registerCosmetic(this);
    }

    public abstract void onEquip(AuroraMCPlayer player);

    public abstract void onUnequip(AuroraMCPlayer player);

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
        GADGET("Gadget");

        private final String name;

        CosmeticType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum UnlockMode {
        PERMISSION,
        RANK,
        TICKETS,
        STORE_PURCHASE,
        CRATE
    }

}
