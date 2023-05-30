/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.party;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPartyChat extends ProxyCommand {
    public CommandPartyChat() {
        super("chat", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (player.getParty() != null) {
            if (args.size() > 0) {
                if (player.getActiveMutes().size() > 0) {
                    //They have at least 1 active mute, disallow the chat message.
                    Punishment punishment = player.getActiveMutes().get(0);
                    PunishmentLength length;
                    if (punishment.getExpire() == -1) {
                        length = new PunishmentLength(-1);
                    } else {
                        length = new PunishmentLength((punishment.getExpire() - System.currentTimeMillis())/3600000d);
                    }
                    switch (punishment.getStatus()) {
                        case 2:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s [Awaiting Approval]**\n" +
                                    "Punishment Code: **%s**\n\n" +
                                    "Your punishment has been applied by a Trial Moderator, and is severe enough to need approval from a Staff Management member to ensure that the punishment applied was correct. When it is approved, the full punishment length will automatically apply to you. If this punishment is denied for being false, **it will automatically be removed**, and the punishment will automatically be removed from your Punishment History.", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                        case 3:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s [SM Approved]**\n" +
                                    "Punishment Code: **%s**", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                        default:
                            player.sendMessage(TextFormatter.pluginMessage("Punishments", String.format("You are muted for **%s**.\n" +
                                    "Reason: **%s - %s**\n" +
                                    "Punishment Code: **%s**", length.getFormatted(), (AuroraMCAPI.getRules().getRule(punishment.getRuleID())).getRuleName(), punishment.getExtraNotes(), punishment.getPunishmentCode())));
                            break;
                    }
                    return;
                }
                if (AuroraMCAPI.getFilter() == null) {
                    player.sendMessage(TextFormatter.pluginMessage("party", "Our chat filter is currently being updated. Please try again in a few seconds!"));
                    return;
                }
                String filteredMessage = AuroraMCAPI.getFilter().filter(String.join(" ", args));
                player.getParty().partyChat(player.getPartyPlayer(), AuroraMCAPI.getFilter().processEmotes(player, filteredMessage), filteredMessage);
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Party", "You didn't provide a message to send!"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Party", "You must be in a party in order to send a party chat."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
