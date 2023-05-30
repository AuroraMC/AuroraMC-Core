/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player;

import java.util.List;

public class Mentor {

    private final int id;
    private final String name;
    private final List<Mentee> mentees;

    public Mentor(int id, String name, List<Mentee> mentees) {
        this.id = id;
        this.name = name;
        this.mentees = mentees;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Mentee> getMentees() {
        return mentees;
    }
}
