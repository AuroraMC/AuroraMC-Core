/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.debug;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandKillMessageTest extends Command {

    public CommandKillMessageTest() {
        super("KillMessageTest", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.ALL), false, null);
    }


    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block" + AuroraMCAPI.getFormatter().rainbow(" was tied to a firework and exploded by ") + "§bHeliology");
        player.getPlayer().sendMessage("§3§l«KILL» §b_Brandon" + AuroraMCAPI.getFormatter().rainbow(" was tied to a firework and exploded by ") + "§bBunni");
        player.getPlayer().sendMessage("§3§l«KILL» §bLoofii" + AuroraMCAPI.getFormatter().rainbow(" was tied to a firework and exploded by ") + "§bBerend");
        player.getPlayer().sendMessage("§3§l«KILL» §bjcild" + AuroraMCAPI.getFormatter().rainbow(" was tied to a firework and exploded by ") + "§bBlock2Block");
        player.getPlayer().sendMessage("§3§l«KILL» §bAlexTheCoder" + AuroraMCAPI.getFormatter().rainbow(" was tied to a firework and exploded by ") + "§bBlock2Block");
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
