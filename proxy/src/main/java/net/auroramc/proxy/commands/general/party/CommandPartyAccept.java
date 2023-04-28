/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.player.party.PartyInvite;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyAccept extends ProxyCommand {


    public CommandPartyAccept() {
        super("accept", Collections.emptyList(), Collections.emptyList(), false, null);
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

            partyInvite.getParty().requestAccepted(partyInvite);
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party accept <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
