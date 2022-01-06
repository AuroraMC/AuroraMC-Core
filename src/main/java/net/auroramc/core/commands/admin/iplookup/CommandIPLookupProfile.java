/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin.iplookup;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.ipprofiles.IPProfile;
import net.auroramc.core.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.core.gui.admin.iplookup.IPLookupPlayer;
import net.auroramc.core.gui.admin.iplookup.IPLookupProfile;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandIPLookupProfile extends Command {

    public CommandIPLookupProfile() {
        super("profile", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id;
                    try {
                        id = Integer.parseInt(args.get(0));
                    } catch (NumberFormatException e) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup profile [profile ID]**"));
                        return;
                    }
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Performing IP Lookup for profile **%s**...", args.get(0))));
                    IPProfile playerProfile = AuroraMCAPI.getDbManager().ipLookup(id);
                    if (playerProfile == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP Lookup failed for profile **%s**.", args.get(0))));
                        return;
                    }
                    IPLookupProfile ipLookupPlayer = new IPLookupProfile(playerProfile);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ipLookupPlayer.open(player);
                            AuroraMCAPI.openGUI(player, ipLookupPlayer);
                        }
                    }.runTask(AuroraMCAPI.getCore());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup profile [profile ID]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }


}
