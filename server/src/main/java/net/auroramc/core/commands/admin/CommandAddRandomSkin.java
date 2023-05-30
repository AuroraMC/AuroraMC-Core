/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.utils.disguise.DisguiseUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandAddRandomSkin extends ServerCommand {


    public CommandAddRandomSkin() {
        super("addrandomskin", Collections.singletonList("ars"), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().pushSkin(Objects.requireNonNull(DisguiseUtil.getSkin(UUID.fromString("4d169877-529f-4877-ad19-a5006f3940b0"))));
                player.sendMessage(TextFormatter.pluginMessage("Disguise", "Added random disguise skin."));
            }
        }.runTaskAsynchronously(ServerAPI.getCore());
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
