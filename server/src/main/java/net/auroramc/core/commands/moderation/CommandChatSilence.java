/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandChatSilence extends ServerCommand {


    public CommandChatSilence() {
        super("silence", Collections.singletonList("chatsilence"), Collections.singletonList(Permission.ADMIN), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.sendMessage(TextFormatter.pluginMessage("Silence", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            ServerAPI.enableChatSilence(amount, true);
        } else {
            if (AuroraMCAPI.getChatSilenceEnd() != -2) {
                ServerAPI.disableSilence();
            } else {
                ServerAPI.enableChatSilence((short) -1, true);
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
