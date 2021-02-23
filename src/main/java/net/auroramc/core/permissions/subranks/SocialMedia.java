package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class SocialMedia extends SubRank {
    public SocialMedia() {
        super(7, "Social Media", Collections.singletonList(Permission.SOCIAL_MEDIA), Color.fromRGB(85, 85, 255), '9');
    }
}
