package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public class SocialMedia extends SubRank {
    public SocialMedia() {
        super(7, "Social Media", new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("socialmedia"))), Color.fromRGB(85, 85, 255), '9');
    }
}
