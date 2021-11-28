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
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("Block2Block whacked Heliology with a sparkler"));
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("whacked ") + "§bHeliology " + AuroraMCAPI.getFormatter().rainbow("with a sparkler"));
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("Block2Block shot a party popper at Heliology"));
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("shot a parter popper at ") + "§bHeliology");
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("The party didn't stop until Block2Block fell into the void"));
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("The party didn't stop until") + " §bBlock2Block" + AuroraMCAPI.getFormatter().rainbow(" fell into the void"));
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("Block2Block got too excited and fell off a cliff. What a donkey!"));
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("got too excited and fell off a cliff. What a donkey!"));
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("Block2Block was tied to a firework and exploded by Heliology"));
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("was tied to a firework and exploded by ") + "§bHeliology");
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«KILL» " + AuroraMCAPI.getFormatter().rainbow("Block2Block went too close to the piñata"));
        player.getPlayer().sendMessage("§3§l«KILL» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("went too close to the piñata"));
        player.getPlayer().sendMessage("");
        player.getPlayer().sendMessage("§3§l«JOIN» " + AuroraMCAPI.getFormatter().rainbow("Block2Block is ready to party!"));
        player.getPlayer().sendMessage("§3§l«JOIN» §bBlock2Block " + AuroraMCAPI.getFormatter().rainbow("is ready to party!"));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
