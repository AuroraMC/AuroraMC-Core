/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.stats;

import java.util.HashMap;
import java.util.Map;

public class GameStatistics {

    private final int gameID;
    private final Map<String, Long> stats;

    public GameStatistics(int gameID, Map<String, Long> stats) {
        this.gameID = gameID;
        this.stats = stats;
    }

    public int getGameID() {
        return gameID;
    }

    public Map<String, Long> getStats() {
        return new HashMap<>(stats);
    }

    public void addStat(String key, long amount) {
        if (stats.containsKey(key)) {
            stats.put(key, stats.get(key) + amount);
        } else {
            stats.put(key, amount);
        }
    }

    public long getStat(String key) {
        if (!this.stats.containsKey(key)) {
            return 0;
        }
        return this.stats.get(key);
    }
}
