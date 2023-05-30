/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
