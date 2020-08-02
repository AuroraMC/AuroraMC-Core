package network.auroramc.core.api.utils;

public class LevelUtils {

    public static long xpForLevel(int level) {
        if (level == 251) {
            return -1;
        }
        return Math.round(((25 * Math.pow(level, 2)) + (100*level) + (1000)));
    }

}
