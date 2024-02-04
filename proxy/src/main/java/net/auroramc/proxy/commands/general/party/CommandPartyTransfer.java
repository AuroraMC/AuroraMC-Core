/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
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

public class CommandPartyTransfer extends ProxyCommand {

    public CommandPartyTransfer() {
        super("transfer", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getParty() != null) {
                if (player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                    PartyPlayer target = null;

                    if (args.get(0).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(TextFormatter.pluginMessage("Party", "You cannot transfer the party to yourself!"));
                        return;
                    }

                    for (PartyPlayer mcPlayer : player.getParty().getPlayers()) {
                        if (mcPlayer.getName().equalsIgnoreCase(args.get(0))) {
                            target = mcPlayer;
                            break;
                        }
                    }

                    if (target == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Party", "That player is not in your party."));
                        return;
                    }

                    player.getParty().transfer(target);
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Party", "You must be the leader of the party in order to transfer it."));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to transfer a party."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party transfer <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
