/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players.lookup;

import java.util.List;

public class IPLookup {

    private final String ip;
    private final int profileId;
    private final List<LookupUser> names;
    private final int banNumbers;
    private final int muteNumbers;

    public IPLookup(String ip, int profileId, List<LookupUser> names, int bans, int mutes) {
        this.ip = ip;
        this.profileId = profileId;
        this.names = names;
        this.banNumbers = bans;
        this.muteNumbers = mutes;
    }

    public String getIp() {
        return ip;
    }

    public int getBanNumbers() {
        return banNumbers;
    }

    public int getMuteNumbers() {
        return muteNumbers;
    }

    public int getProfileId() {
        return profileId;
    }

    public List<LookupUser> getNames() {
        return names;
    }
}
