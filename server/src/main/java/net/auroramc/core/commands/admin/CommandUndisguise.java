/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUndisguise extends ServerCommand {


    public CommandUndisguise() {
        super("undisguise", Collections.singletonList("ud"), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
            //Undisguise.
            if (player.getActiveDisguise() != null) {
                player.undisguise(true);
                player.sendMessage(TextFormatter.pluginMessage("Disguise", "You have been undisguised."));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("UnDisguise");
                out.writeUTF(player.getName());
                player.sendPluginMessage(out.toByteArray());
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Disguise", "You cannot undisguise when you are not disguised."));
            }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
