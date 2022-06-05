/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandToggleCosmetics extends Command {


    public CommandToggleCosmetics() {
        super("togglecosmetics", Collections.singletonList("tc"), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (AuroraMCAPI.isCosmeticsEnabled()) {
            AuroraMCAPI.setCosmeticsEnabled(false);
            for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Manager", "Cosmetics have been &cdisabled &rby an admin! All of your active cosmetics were disabled."));
                for (Cosmetic cosmetic : player1.getActiveCosmetics().values()) {
                    cosmetic.onUnequip(player1);
                }
            }
        } else {
            AuroraMCAPI.setCosmeticsEnabled(true);
            for (AuroraMCPlayer player1 : AuroraMCAPI.getPlayers()) {
                player1.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Player Manager", "Cosmetics have been &aenabled &rby an admin!"));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
