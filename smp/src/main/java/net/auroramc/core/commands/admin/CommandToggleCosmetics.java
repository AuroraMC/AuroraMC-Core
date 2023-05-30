/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
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
                player1.sendMessage(TextFormatter.pluginMessage("Player Manager", "Cosmetics have been §cdisabled §rby an admin! All of your active cosmetics were disabled."));
                player1.getActiveCosmetics().values().stream().filter((cosmetic -> !cosmetic.shouldBypassDisabled())).forEach(cosmetic -> cosmetic.onUnequip(player1));

            }
        } else {
            AuroraMCAPI.setCosmeticsEnabled(true);
            for (AuroraMCServerPlayer player1 : ServerAPI.getPlayers()) {
                player1.sendMessage(TextFormatter.pluginMessage("Player Manager", "Cosmetics have been §aenabled §rby an admin!"));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
