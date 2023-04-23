/*
 * Copyright (c) 2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy.commands.staff;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandPanel extends ProxyCommand {


    public CommandPanel() {
        super("panel", Arrays.asList("panelcode", "panelverify", "code"), Collections.singletonList(Permission.PANEL), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                if (uuid != null) {
                    Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
                    if (!rank.hasPermission("panel")) {
                        List<SubRank> subRanks = AuroraMCAPI.getDbManager().getSubRanks(uuid);
                        boolean panel = false;
                        for (SubRank subrank : subRanks) {
                            if (subrank.hasPermission("panel")) {
                                panel = true;
                                break;
                            }
                        }
                        if (!panel) {
                            player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "That user does not have permission to access the panel, so a code was not generated."));
                            return;
                        }
                    }

                    String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

                    TextComponent component = new TextComponent("");
                    component.addExtra(TextFormatter.pluginMessage("Panel Manager", "Your randomly generated verification code for user **" + args.get(0) + "** is: "));
                    TextComponent codeComponent = new TextComponent(code);
                    codeComponent.setColor(ChatColor.AQUA);
                    codeComponent.setBold(true);
                    codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));
                    codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to copy to clipboard.").color(ChatColor.GREEN).create()));
                    component.addExtra(codeComponent);
                    component.addExtra(". It expires in 60 seconds.");
                    player.sendMessage(component);
                    AuroraMCAPI.getDbManager().setPanelCode(uuid, code);
                } else {
                    player.sendMessage(TextFormatter.pluginMessage("Panel Manager", "That user does not exist."));
                }
            });
        } else {
            String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

            TextComponent component = new TextComponent("");
            component.addExtra(TextFormatter.pluginMessage("Panel Manager", "Your randomly generated verification code is: "));
            TextComponent codeComponent = new TextComponent(code);
            codeComponent.setColor(ChatColor.AQUA);
            codeComponent.setBold(true);
            codeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));
            codeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("Click to copy to clipboard.").color(ChatColor.GREEN).create()));
            component.addExtra(codeComponent);
            component.addExtra(". It expires in 60 seconds.");
            player.sendMessage(component);
            ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                AuroraMCAPI.getDbManager().setPanelCode(player.getUniqueId(), code);
            });
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
