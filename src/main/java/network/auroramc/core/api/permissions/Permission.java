package network.auroramc.core.api.permissions;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Permission {

    private final int id;
    private final String node;

    public Permission(@NotNull int id, @NotNull String node) {
        this.id = id;
        this.node = node;
    }

    public final int getId() {
        return id;
    }

    public final String getNode() {
        return node;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return id == that.id &&
                Objects.equals(node, that.node);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, node);
    }
}
