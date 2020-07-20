package network.auroramc.core.api.stats;

import java.util.List;
import java.util.Map;

public class PlayerStatistics {

    private long totalXpEarned;
    private long xpIntoLevel;
    private long level;
    private List<Achievement> achievementsGained;
    private Map<Integer, GameStatistics> stats;
    private long lobbyTimeMs;
    private long gameTimeMs;


}
