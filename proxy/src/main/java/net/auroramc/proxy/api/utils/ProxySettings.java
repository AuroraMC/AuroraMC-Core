/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.utils;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.Info;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;

public class ProxySettings {

    private boolean maintenance;
    private String maintenanceMotd;
    private String motd;
    private MaintenanceMode mode;

    public ProxySettings(boolean maintenance, String maintenanceMotd, String motd, MaintenanceMode mode) {
        this.maintenance = maintenance;
        this.maintenanceMotd = maintenanceMotd;
        this.motd = motd;
        this.mode = mode;
        if (AuroraMCAPI.getInfo().getNetwork() == Info.Network.TEST) {
            this.mode = MaintenanceMode.TEST;
        }
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public String getMaintenanceMotd() {
        if (maintenanceMotd == null) {
            if (mode == MaintenanceMode.TEST) {
                return "§cAuroraMC Test Network";
            }
            return "§cAuroraMC is currently in Maintenance. Try again later.";
        }
        return ChatColor.translateAlternateColorCodes('&', maintenanceMotd);
    }

    public String getMotd() {
        if (motd == null) {
            return "§b§lBungee Network";
        }
        return ChatColor.translateAlternateColorCodes('&', motd);
    }

    public void enableMaintenance(MaintenanceMode mode) {
        maintenance = true;
        this.mode = mode;

        for (AuroraMCProxyPlayer player : new ArrayList<>(ProxyAPI.getPlayers())) {
            boolean allowed = false;
            for (Permission permission : mode.getAllowance()) {
                if (player.hasPermission(permission.getNode())) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                player.disconnect(TextFormatter.pluginMessage("Maintenance", "Uh oh! The network has gone into maintenance!\n\n" +
                        "In order to prevent issues, you will be unable to connect to\n" +
                        "the network while this maintenance is in progress! Please feel free\n" +
                        "to keep an eye out on the forums as we will keep you updated on\n" +
                        "this maintenance and an ETA on when the network will be open again!\n\n" +
                        "We apologise for any inconveniences caused.\n" +
                        "**~The AuroraMC Network Leadership Team**"));
            }
        }

        for (AuroraMCProxyPlayer player : new ArrayList<>(ProxyAPI.getPlayers())) {
            Title title = ProxyServer.getInstance().createTitle();
            title.fadeIn(10);
            title.stay(100);
            title.fadeOut(10);
            title.title(new ComponentBuilder("MAINTENANCE").color(ChatColor.DARK_RED).bold(true).create());
            title.subTitle(new TextComponent("Maintenance mode has been toggled on!"));
            player.sendTitle(title);
            player.sendMessage(TextFormatter.pluginMessage("Maintenance", "Maintenance mode has been toggled on!"));
        }
    }

    public void disableMaintenance() {
        maintenance = false;
        this.mode = null;
    }

    public void setMaintenanceMotd(String maintenanceMotd) {
        this.maintenanceMotd = maintenanceMotd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public MaintenanceMode getMode() {
        return mode;
    }

    public void setMode(MaintenanceMode mode) {
        this.mode = mode;

        for (AuroraMCProxyPlayer player : new ArrayList<>(ProxyAPI.getPlayers())) {
            boolean allowed = false;
            for (Permission permission : mode.getAllowance()) {
                if (player.hasPermission(permission.getNode())) {
                    allowed = true;
                    break;
                }
            }

            if (!allowed) {
                player.disconnect(TextFormatter.pluginMessage("Maintenance", "The network's maintenance mode whitelist has been increased, so you can no longer access the network.\n\n" +
                        "We will keep you updated in the staff channels as to what's going\n" +
                        "to happen with this maintenance and when to expect the network to\n" +
                        "be open again."));
            }
        }
    }
}
