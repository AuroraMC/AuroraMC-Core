/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.player;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public interface Team {

    int getId();

    ChatColor getTeamColor();

    String getName();

    List<AuroraMCPlayer> getPlayers();

}
