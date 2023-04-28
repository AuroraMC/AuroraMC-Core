/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyWarp extends ProxyCommand {


    public CommandPartyWarp() {
        super("warp", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (player.getParty() != null) {
            if (player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(37))) {
                    player.getStats().achievementGained(AuroraMCAPI.getAchievement(37), 1, true);
                }
                player.getParty().warp(player.getServer());
                player.sendMessage(TextFormatter.pluginMessage("Party", "You warped everyone to your server!"));
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You must be leader of the party in order to warp people to your server."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to warp people to your server."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
