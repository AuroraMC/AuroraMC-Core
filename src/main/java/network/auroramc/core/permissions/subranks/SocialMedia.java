package network.auroramc.core.permissions.subranks;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class SocialMedia extends SubRank {
    public SocialMedia() {
        super(7, "Social Media", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("socialmedia"))), Color.fromRGB(85, 85, 255), '9');
    }
}
