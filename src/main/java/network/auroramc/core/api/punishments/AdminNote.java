package network.auroramc.core.api.punishments;

public class AdminNote {

    private final int id;
    private final int userId;
    private final String addedBy;
    private final long timestamp;
    private final String note;

    public AdminNote(int id, int userId, String addedBy, long timestamp, String note) {
        this.addedBy = addedBy;
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public String getNote() {
        return note;
    }
}
