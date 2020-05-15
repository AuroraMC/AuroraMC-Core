package network.auroramc.core.api.players;

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
