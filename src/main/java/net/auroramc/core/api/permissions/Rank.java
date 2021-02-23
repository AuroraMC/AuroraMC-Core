package net.auroramc.core.api.permissions;

import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class Rank {

    public enum RankCategory {PLAYER, SOCIAL_MEDIA, CONTENT_CREATOR, MODERATION, LEADERSHIP}

    private final int id;
    private final String name;
    private final String prefixAppearance;
    private final String prefixHoverText;
    private final String prefixHoverURL;
    private final Character prefixColor;
    private final char nameColor;
    private final char connectorColor;
    private final char defaultChatColor;
    private final boolean canUseColorCodes;
    private final List<Permission> permissions;
    private final List<Rank> inheritance;
    private final RankCategory category;
    private final Color color;

    public Rank(@NotNull int id, @NotNull String name, @Nullable String prefixAppearance, @Nullable String prefixHoverText, @Nullable String prefixHoverURL, @Nullable Character prefixColor, @NotNull char nameColor, @NotNull char connectorColor, @NotNull char defaultChatColor, @NotNull boolean canUseColorCodes, @NotNull List<Rank> inherit, @NotNull List<Permission> permissions, @NotNull RankCategory category, @NotNull Color guiColor) {
        this.id = id;
        this.name = name;
        this.prefixAppearance = prefixAppearance;
        this.prefixHoverText = prefixHoverText;
        this.prefixHoverURL = prefixHoverURL;
        this.prefixColor = prefixColor;
        this.nameColor = nameColor;
        this.connectorColor = connectorColor;
        this.defaultChatColor = defaultChatColor;
        this.canUseColorCodes = canUseColorCodes;
        this.permissions = permissions;
        this.inheritance = inherit;
        this.category = category;
        this.color = guiColor;
    }

    public final String getName() {
        return name;
    }

    public final boolean canUseColorCodes() {
        return canUseColorCodes;
    }

    public final char getConnectorColor() {
        return connectorColor;
    }

    public final char getDefaultChatColor() {
        return defaultChatColor;
    }

    public final char getNameColor() {
        return nameColor;
    }

    public final Character getPrefixColor() {
        return prefixColor;
    }

    public final int getId() {
        return id;
    }

    public final List<Permission> getPermissions() {
        return permissions;
    }

    public final String getPrefixAppearance() {
        return prefixAppearance;
    }

    public final String getPrefixHoverText() {
        return prefixHoverText;
    }

    public final String getPrefixHoverURL() {
        return prefixHoverURL;
    }

    public final RankCategory getCategory() {
        return category;
    }

    public final boolean hasPermission(String node) {
        for (Permission permission : getPermissions()) {
            if (permission.getNode().equals(node) || permission.getNode().equals("all")) {
                return true;
            }
        }

        for (Rank rank : getInheritance()) {
            if (rank.hasPermission(node)) {
                return true;
            }
        }
        return false;
    }

    public final List<Rank> getInheritance() {
        return inheritance;
    }

    public final boolean hasPermission(int id) {
        for (Permission permission : getPermissions()) {
            if (permission.getId() == id || permission.getId() == -1) {
                return true;
            }
        }

        for (Rank rank : getInheritance()) {
            if (rank.hasPermission(id)) {
                return true;
            }
        }
        return false;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return id == rank.id &&
                nameColor == rank.nameColor &&
                connectorColor == rank.connectorColor &&
                defaultChatColor == rank.defaultChatColor &&
                canUseColorCodes == rank.canUseColorCodes &&
                name.equals(rank.name) &&
                Objects.equals(prefixAppearance, rank.prefixAppearance) &&
                Objects.equals(prefixHoverText, rank.prefixHoverText) &&
                Objects.equals(prefixHoverURL, rank.prefixHoverURL) &&
                Objects.equals(prefixColor, rank.prefixColor) &&
                permissions.equals(rank.permissions) &&
                Objects.equals(inheritance, rank.inheritance);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, prefixAppearance, prefixHoverText, prefixHoverURL, prefixColor, nameColor, connectorColor, defaultChatColor, canUseColorCodes, permissions, inheritance);
    }
}
