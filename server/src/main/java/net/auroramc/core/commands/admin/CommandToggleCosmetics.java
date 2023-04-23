/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.permissions.Permission;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandToggleCosmetics extends ServerCommand {


    public CommandToggleCosmetics() {
        super("togglecosmetics", Collections.singletonList("tc"), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (AuroraMCAPI.isCosmeticsEnabled()) {
            AuroraMCAPI.setCosmeticsEnabled(false);
            for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                player1.sendMessage(TextFormatter.pluginMessage("Player Manager", "Cosmetics have been &cdisabled &rby an admin! All of your active cosmetics were disabled."));
                for (Cosmetic cosmetic : player1.getActiveCosmetics().values()) {
                    cosmetic.onUnequip(player1);
                }
            }
        } else {
            AuroraMCAPI.setCosmeticsEnabled(true);
            for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                player1.sendMessage(TextFormatter.pluginMessage("Player Manager", "Cosmetics have been &aenabled &rby an admin!"));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
