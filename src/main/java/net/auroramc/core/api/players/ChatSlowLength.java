package net.auroramc.core.api.players;

public class ChatSlowLength {

    private final static String[] SUFFIXES = new String[]{"seconds", "minutes"};

    private final double value;
    private final int suffix;

    public ChatSlowLength(double seconds) {
            if (seconds >= 60) {
                value = (seconds / 60);
                suffix = 2;
            } else {
                value = seconds;
                suffix = 1;
            }
    }

    public String getFormatted() {
        double finalValue = (Math.round(value * 10))/10.0;
        return finalValue + " " + SUFFIXES[suffix - 1];
    }

    public double getMsValue() {
        if (value == -1) {
            return -1d;
        }

        double ms = value*3600000d;

        if (suffix == 2) {
            ms *= 24d;
        }

        return ms;
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
