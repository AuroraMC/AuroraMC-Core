/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyDisband extends ProxyCommand {


    public CommandPartyDisband() {
        super("warp", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (player.getParty() != null) {
            if (player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                player.getParty().disband();
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You must be leader of the party in order to disband it."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to disband it."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
