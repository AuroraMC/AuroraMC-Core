/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.listeners;

import net.auroramc.api.AuroraMCAPI;
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
import java.util.logging.Level;

public class ProxyPingListener implements Listener {

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        PendingConnection c = event.getConnection();
        ServerPing response = event.getResponse();
        if (ProxyAPI.isMaintenance()) {
            if (ProxyAPI.getProxySettings().getMode() != MaintenanceMode.TEST) {
                response.setVersion(new net.md_5.bungee.api.ServerPing.Protocol("Maintenance", 4));
            }
            ServerPing.Players p = response.getPlayers();
            p.setOnline(ProxyAPI.getPlayerCount());
            p.setMax(p.getOnline() + 1);
            response.setPlayers(p);
            TextComponent t;

            t = new TextComponent("                       §4§l« AURORAMC »");
            t.addExtra(new TextComponent(ComponentSerializer.parse("{text: \"\n\"}")));
            StringBuilder motd = new StringBuilder(ProxyAPI.getProxySettings().getMaintenanceMotd());
            int spaces = Math.round((((43 - ChatColor.stripColor(motd.toString()).length()) / 2f) * 1.3f));
            if (!motd.toString().equalsIgnoreCase("§cAuroraMC is currently in Maintenance. Try again later.")) {
                for (int i = 0; i < spaces; i++) {
                    motd.insert(0, " ");
                }
            }
            t.addExtra(motd.toString());

            response.setDescriptionComponent(t);

            ServerPing.Protocol protocol = response.getVersion();
            protocol.setName("AuroraMC 1.8-1.20");
            File file = new File(ProxyAPI.getCore().getDataFolder(), "maintenance.png");
            try {
                response.setFavicon(Favicon.create(ImageIO.read(file)));
            } catch (Exception e) {
                AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
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
            protocol.setName("AuroraMC 1.8-1.20");
            File file = new File(ProxyAPI.getCore().getDataFolder(), "normal.png");
            try {
                response.setFavicon(Favicon.create(ImageIO.read(file)));
            } catch (Exception e) {
                AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
            }
        }

    }

}
