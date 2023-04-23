/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.proxy;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.info.ServerInfo;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.commands.admin.*;
import net.auroramc.proxy.commands.admin.blacklist.CommandBlacklist;
import net.auroramc.proxy.commands.admin.debug.CommandJoinMessage;
import net.auroramc.proxy.commands.admin.debug.CommandLag;
import net.auroramc.proxy.commands.admin.debug.CommandReloadServers;
import net.auroramc.proxy.commands.admin.economy.CommandBankBungee;
import net.auroramc.proxy.commands.admin.economy.CommandCrown;
import net.auroramc.proxy.commands.admin.economy.CommandTicket;
import net.auroramc.proxy.commands.admin.maintenance.CommandMaintenance;
import net.auroramc.proxy.commands.admin.stats.CommandStatsAdmin;
import net.auroramc.proxy.commands.general.CommandChat;
import net.auroramc.proxy.commands.general.CommandMessage;
import net.auroramc.proxy.commands.general.CommandReply;
import net.auroramc.proxy.commands.general.CommandServer;
import net.auroramc.proxy.commands.general.friends.CommandFriend;
import net.auroramc.proxy.commands.general.party.CommandParty;
import net.auroramc.proxy.commands.staff.CommandPanel;
import net.auroramc.proxy.commands.staff.CommandPanelRegister;
import net.auroramc.proxy.commands.staff.CommandPlusLookup;
import net.auroramc.proxy.commands.staff.management.CommandJuniorModeratorList;
import net.auroramc.proxy.commands.staff.management.CommandModeratorList;
import net.auroramc.proxy.listeners.*;
import net.auroramc.proxy.managers.CommandManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuroraMC extends Plugin {

    Configuration configuration;

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxyAPI.init(this);



        AuroraMCAPI.registerCommand(new CommandMaintenance());
        AuroraMCAPI.registerCommand(new CommandServer());
        AuroraMCAPI.registerCommand(new CommandSend());
        AuroraMCAPI.registerCommand(new CommandFind());
        AuroraMCAPI.registerCommand(new CommandAnnounce());
        AuroraMCAPI.registerCommand(new CommandMessage());
        AuroraMCAPI.registerCommand(new CommandReply());
        AuroraMCAPI.registerCommand(new CommandMotd());
        AuroraMCAPI.registerCommand(new CommandBlacklist());
        AuroraMCAPI.registerCommand(new CommandStatsAdmin());
        AuroraMCAPI.registerCommand(new CommandCrown());
        AuroraMCAPI.registerCommand(new CommandTicket());
        AuroraMCAPI.registerCommand(new CommandBankBungee());
        AuroraMCAPI.registerCommand(new CommandAddPlusDays());
        AuroraMCAPI.registerCommand(new CommandFriend());
        AuroraMCAPI.registerCommand(new CommandChat());
        AuroraMCAPI.registerCommand(new CommandParty());
        AuroraMCAPI.registerCommand(new CommandJuniorModeratorList());
        AuroraMCAPI.registerCommand(new CommandModeratorList());
        AuroraMCAPI.registerCommand(new CommandGlobalChatSlow());
        AuroraMCAPI.registerCommand(new CommandGlobalChatSilence());
        AuroraMCAPI.registerCommand(new CommandPlusLookup());
        AuroraMCAPI.registerCommand(new CommandLag());
        AuroraMCAPI.registerCommand(new CommandPanel());
        AuroraMCAPI.registerCommand(new CommandPanelRegister());
        AuroraMCAPI.registerCommand(new CommandJoinMessage());
        AuroraMCAPI.registerCommand(new CommandReloadServers());



        AuroraMCAPI.loadRules();
        AuroraMCAPI.loadFilter();

        //Registering default Event Listeners
        getProxy().getPluginManager().registerListener(this, new CommandManager());
        getProxy().getPluginManager().registerListener(this, new JoinListener());
        getProxy().getPluginManager().registerListener(this, new LeaveListener());
        getProxy().getPluginManager().registerListener(this, new ProxyPingListener());
        getProxy().getPluginManager().registerListener(this, new RecievePluginMessage());
        getProxy().getPluginManager().registerListener(this, new TabCompleteListener());
        getProxy().getPluginManager().registerListener(this, new ProtocolMessageReceivedListener());

        getProxy().registerChannel("auroramc:server");

        getLogger().info("Connection node is now online and ready to accept connections, letting Mission Control know...");
        ProtocolMessage message = new ProtocolMessage(Protocol.PROXY_ONLINE, "Mission Control", "", AuroraMCAPI.getInfo().getName(),  AuroraMCAPI.getInfo().getNetwork().name());
        CommunicationUtils.sendMessage(message);

        //Proxy/Server updater
        ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), () -> {
            for (AuroraMCProxyPlayer player : ProxyAPI.getPlayers()) {
                if (!player.isOnline()) {
                    ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                        ProxyAPI.playerLeave(player);
                    });
                    return;
                }
                AuroraMCAPI.getDbManager().setProxyUUID(player.getUniqueId(), UUID.fromString(AuroraMCAPI.getInfo().getName()));
                AuroraMCAPI.getDbManager().updateServer(player.getUniqueId(), player.getServer().getName());
            }
        }, 1, 1, TimeUnit.MINUTES);

        //Lobby server player count updater
        ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), () -> {
            for (ServerInfo info : ProxyAPI.getLobbyServers()) {
                info.fetchData();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        if (!CommunicationUtils.isShutdown() && !ProxyAPI.isScheduledForShutdown()) {
            ProtocolMessage message = new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", "forced", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
            CommunicationUtils.sendMessage(message);
        }
    }

    public Configuration getConfig() {
        return configuration;
    }

}
