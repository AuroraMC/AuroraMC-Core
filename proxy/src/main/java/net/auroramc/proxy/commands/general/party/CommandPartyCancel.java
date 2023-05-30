/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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

public class CommandPartyCancel extends ProxyCommand {

    public CommandPartyCancel() {
        super("cancel", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getParty() != null) {
                if (player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                    PartyInvite partyInvite = null;
                    for (PartyInvite invite : player.getParty().getPartyInvites()) {
                        if (invite.getPlayer().getName().equalsIgnoreCase(args.get(0))) {
                            partyInvite = invite;
                            break;
                        }
                    }

                    if (partyInvite == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Party", "You have no outgoing pending party from to player."));
                        return;
                    }


                    partyInvite.getParty().requestDenied(partyInvite);
                    player.sendMessage(TextFormatter.pluginMessage("Party", "Party invite cancelled."));
                    if (partyInvite.getPlayer().getPlayer() != null) {
                        partyInvite.getPlayer().getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite from **%s** was cancelled.", player.getName())));
                    }
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Party", "You must be party leader to cancel an outgoing invite."));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party to cancel a pending invite."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party cancel <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
