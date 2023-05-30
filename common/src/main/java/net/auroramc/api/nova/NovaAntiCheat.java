/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.nova;

import net.auroramc.api.player.AuroraMCPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NovaAntiCheat {

    private static final Map<AuroraMCPlayer, ViolationLog> log;

    static {
        log = new HashMap<>();
    }

    public static synchronized void logViolation(Violation violation) {
        if (log.containsKey(violation.getPlayer())) {
            log.get(violation.getPlayer()).logViolation(violation);
        } else {
            ViolationLog violationLog = new ViolationLog(violation.getPlayer());
            violationLog.logViolation(violation);
            log.put(violation.getPlayer(), violationLog);
        }
    }

    public static ViolationLog getLogs(AuroraMCPlayer player) {
        return log.get(player);
    }

}
