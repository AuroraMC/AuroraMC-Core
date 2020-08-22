package network.auroramc.core.api.utils;

public class LevelUtils {

    public static long xpForLevel(long level) {
        if (level == 251 || level < 1) {
            return -1;
        }
        return Math.round(((25 * Math.pow(level, 2)) + (100*level) + (1000)));
    }

    public static LevelCalculation calculateLevel(int beginningLevel, long initialXp, long xpGained) {
        int newLevel = beginningLevel;
        long newXp = initialXp + xpGained;
        while (newXp >= xpForLevel(newLevel + 1)) {
            newLevel++;
            newXp -= xpForLevel(newLevel);
        }
        return new LevelCalculation(newLevel, newXp);
    }

    public static class LevelCalculation {

        private final int level;
        private final long remainingXp;

        private LevelCalculation(int level, long remainingXp) {
            this.level = level;
            this.remainingXp = remainingXp;
        }

        public int getLevel() {
            return level;
        }

        public long getRemainingXp() {
            return remainingXp;
        }
    }

}
