package net.auroramc.core.api.permissions;

import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SubRank {

    private final int id;
    private final String name;
    private final List<Permission> permissions;
    private final Color color;
    private final char menuColor;

    public SubRank(@NotNull int id, @NotNull String name, @NotNull List<Permission> permissions, @NotNull Color color, @NotNull char menuColor) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
        this.color = color;
        this.menuColor = menuColor;
    }

    public final String getName() {
        return name;
    }

    public final List<Permission> getPermissions() {
        return new ArrayList<>(permissions);
    }

    public final boolean hasPermission(String node) {
        for (Permission permission : getPermissions()) {
            if (permission.getNode().equals(node) || permission.getNode().equals("all")) {
                return true;
            }
        }
        return false;
    }

    public final boolean hasPermission(int id) {
        for (Permission permission : getPermissions()) {
            if (permission.getId() == id || permission.getId() == -1) {
                return true;
            }
        }


        return false;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public char getMenuColor() {
        return menuColor;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubRank subRank = (SubRank) o;
        return id == subRank.id &&
                name.equals(subRank.name) &&
                permissions.equals(subRank.permissions);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, name, permissions);
    }
}
