/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

import java.util.List;

public interface Team {

    int getId();

    char getTeamColor();

    String getName();

    List<AuroraMCPlayer> getPlayers();

}
