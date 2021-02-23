package net.auroramc.core.permissions.subranks;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;

public final class JrQA extends SubRank {
    public JrQA() {
        super(2, "Junior Quality Assurance", Collections.singletonList(Permission.DEBUG_INFO), Color.fromRGB(85, 255, 85), 'a');
    }
}
