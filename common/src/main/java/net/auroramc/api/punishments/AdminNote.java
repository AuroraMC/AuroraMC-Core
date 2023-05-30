/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.punishments;

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
