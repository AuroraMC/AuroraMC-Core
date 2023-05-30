/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.listeners;

import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.utils.MaintenanceMode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;

public class ProxyPingListener implements Listener {

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        PendingConnection c = event.getConnection();
        ServerPing response = event.getResponse();
        if (ProxyAPI.isMaintenance()) {
            if (ProxyAPI.getProxySettings().getMode() == MaintenanceMode.NOT_OPEN) {
                String string = "Opens in ";
                long difference = 1659283200000L - System.currentTimeMillis();
                int seconds = (int) (difference / 1000) % 60 ;
                int minutes = (int) ((difference / (1000*60)) % 60);
                int hours   = (int) ((difference / (1000*60*60)) % 24);
                int days   = (int) (difference / (1000*60*60*24));

                if (days > 0) {
                    string += days + "d ";
                }
                if (hours > 0) {
                    string += hours + "h ";
                }
                if (minutes > 0) {
                    string += minutes + "m ";
                }
                if (seconds > 0) {
                    string += seconds + "s ";
                }
                response.setVersion(new net.md_5.bungee.api.ServerPing.Protocol(string, 4));
            } else {
                response.setVersion(new net.md_5.bungee.api.ServerPing.Protocol("Maintenance", 4));
            }
            ServerPing.Players p = response.getPlayers();
            p.setOnline(ProxyAPI.getPlayerCount());
            p.setMax(p.getOnline() + 1);
            response.setPlayers(p);
            TextComponent t;
            if (ProxyAPI.getProxySettings().getMode() == MaintenanceMode.NOT_OPEN) {
                t = new TextComponent("§4§lAuroraMC §cis almost ready for launch!");
                t.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"\n\"}")));
                t.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"§cJoin our discord now at discord.auroramc.net!\"}")));
            } else {
                t = new TextComponent("                       §4§l« AURORAMC »");
                t.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"\n\"}")));
                StringBuilder motd = new StringBuilder(ProxyAPI.getProxySettings().getMaintenanceMotd());
                int spaces = Math.round((((43 - ChatColor.stripColor(motd.toString()).length())/2f)*1.3f));
                if (!motd.toString().equalsIgnoreCase("§cAuroraMC is currently in Maintenance. Try again later.")) {
                    for (int i = 0;i < spaces;i++) {
                        motd.insert(0, " ");
                    }
                }
                t.addExtra(motd.toString());
            }
            response.setDescriptionComponent(t);

            File file = new File(ProxyAPI.getCore().getDataFolder(), "maintenance.png");
            try {
                response.setFavicon(Favicon.create(ImageIO.read(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ServerPing.Players p = response.getPlayers();
            p.setOnline(ProxyAPI.getPlayerCount());
            p.setMax(p.getOnline() + 1);
            response.setPlayers(p);
            TextComponent t = new TextComponent("                       §3§l« AURORAMC »");
            t.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"\n\"}")));
            StringBuilder motd = new StringBuilder(ProxyAPI.getProxySettings().getMotd());
            int spaces = Math.round((((43 - ChatColor.stripColor(motd.toString()).length())/2f)*1.2f));
            if (spaces > 0) {
                for (int i = 0;i < spaces;i++) {
                    motd.insert(0, " ");
                }
            }
            t.addExtra(motd.toString());
            response.setDescriptionComponent(t);

            ServerPing.Protocol protocol = response.getVersion();
            protocol.setName("AuroraMC 1.8-1.19");
            File file = new File(ProxyAPI.getCore().getDataFolder(), "normal.png");
            try {
                response.setFavicon(Favicon.create(ImageIO.read(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
