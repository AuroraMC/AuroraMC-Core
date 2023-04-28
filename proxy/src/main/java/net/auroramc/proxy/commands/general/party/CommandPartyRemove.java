/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
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

public class CommandPartyRemove extends ProxyCommand {


    public CommandPartyRemove() {
        super("remove", Collections.singletonList("kick"), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getParty() != null) {
                if (player.getParty().getLeader().getUuid().equals(player.getUniqueId())) {
                    PartyPlayer target = null;
                    for (PartyPlayer pl : player.getParty().getPlayers()) {
                        if (pl.getName().equalsIgnoreCase(args.get(0))) {
                            target = pl;
                            break;
                        }
                    }

                    if (target == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Party", "That player is not in your party."));
                        return;
                    }

                    player.getParty().remove(target, true);
                    if (target.getPlayer() != null) {
                        target.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", "You were removed from the party."));
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Party", String.format("You removed **%s** from the party!", args.get(0))));
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Party", "You must be leader of the party in order to remove someone from it."));
                }
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to remove someone from it."));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "Invalid syntax. Correct syntax: **/party remove <player>**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
