package network.auroramc.core.api.permissions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public abstract class SubRank {

    private final int id;
    private final String name;
    private final ArrayList<Permission> permissions;

    public SubRank(@NotNull int id, @NotNull String name, @NotNull ArrayList<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public final String getName() {
        return name;
    }

    public final ArrayList<Permission> getPermissions() {
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
