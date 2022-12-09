/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.disguise.DisguiseUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandAddRandomSkin extends Command {


    public CommandAddRandomSkin() {
        super("addrandomskin", Collections.singletonList("ars"), Collections.singletonList(Permission.ALL), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().pushSkin(Objects.requireNonNull(DisguiseUtil.getSkin(UUID.fromString("4d169877-529f-4877-ad19-a5006f3940b0"))));
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Added random disguise skin."));
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
