/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandChatSilence extends Command {


    public CommandChatSilence() {
        super("silence", Collections.singletonList("chatsilence"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            AuroraMCAPI.enableChatSilence(amount, true);
        } else {
            if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                AuroraMCAPI.disableSilence();
            } else {
                AuroraMCAPI.enableChatSilence((short) -1, true);
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
