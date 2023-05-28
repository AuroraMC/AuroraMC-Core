/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.nova;

import net.auroramc.api.player.AuroraMCPlayer;

import java.util.HashMap;
import java.util.Map;

public class ViolationLog {

    private final AuroraMCPlayer player;
    private final Map<NovaCheck, CheckLog> logs;

    public ViolationLog(AuroraMCPlayer player) {
        this.player = player;
        logs = new HashMap<>();
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public synchronized void logViolation(Violation violation) {
        if (logs.containsKey(violation.getCheck())) {
            logs.get(violation.getCheck()).logViolation(violation);
        } else {
            CheckLog log = new CheckLog(player, violation.getCheck());
            log.logViolation(violation);
            logs.put(violation.getCheck(), log);
        }
    }

    public CheckLog getLog(NovaCheck check) {
        return logs.get(check);
    }
}
