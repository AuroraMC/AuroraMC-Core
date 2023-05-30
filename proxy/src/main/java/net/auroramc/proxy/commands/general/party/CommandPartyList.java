/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.PartyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyList extends ProxyCommand {


    public CommandPartyList() {
        super("warp", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (player.getParty() != null) {
            player.sendMessage(TextFormatter.pluginMessage("Party", String.format("You are currently in **%s**'s party!", player.getParty().getLeader().getName())));
            List<String> members = new ArrayList<>();
            for (PartyPlayer pl : player.getParty().getPlayers()) {
                members.add(pl.getName());
            }
            player.sendMessage(TextFormatter.pluginMessage("Party", String.format("**Members:** %s", String.join(", ", members))));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to list the people in it."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
