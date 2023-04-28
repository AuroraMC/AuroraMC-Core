/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.PartyInvite;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyDeny extends ProxyCommand {


    public CommandPartyDeny() {
        super("deny", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            PartyInvite partyInvite = null;
            for (PartyInvite invite : player.getPartyInvites()) {
                if (invite.getParty().getLeader().getName().equalsIgnoreCase(args.get(0))) {
                    partyInvite = invite;
                    break;
                }
            }

            if (partyInvite == null) {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You have no incoming pending party from that player."));
                return;
            }

            if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(42))) {
                player.getStats().achievementGained(AuroraMCAPI.getAchievement(42), 1, true);
            }
            partyInvite.getParty().requestDenied(partyInvite);
            player.sendMessage(TextFormatter.pluginMessage("Party", "Party invite denied."));
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party deny <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
