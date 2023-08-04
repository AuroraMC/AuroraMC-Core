/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.admin.debug;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.punishments.PunishmentLength;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;

public class CommandLag extends ProxyCommand {

    public CommandLag() {
        super("plag", Collections.singletonList("proxylag"), Collections.singletonList(Permission.DEBUG_INFO), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        String buildNumber = null;
        String gitCommit = null;
        String branch = null;
        try {
            Enumeration<URL> resources = ProxyAPI.getCore().getClass().getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                // check that this is your manifest and do what you need or get the next one
                Attributes attributes = manifest.getMainAttributes();

                buildNumber = attributes.getValue("Jenkins-Build-Number");
                gitCommit = attributes.getValue("Git-Commit");
                branch = attributes.getValue("Branch");
                if (buildNumber != null && gitCommit != null) {
                    if (branch == null || branch.equals("null")) {
                        branch = "master";
                    }
                    break;
                }
            }
        } catch (IOException e) {
            AuroraMCAPI.getLogger().log(Level.WARNING, "An exception has occurred. Stack trace: ", e);
        }
        MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
        player.sendMessage(TextFormatter.pluginMessage("Resource Monitor", String.format("Proxy lag statistics for proxy **%s**:\n" +
                "Uptime: **%s**\n" +
                "Used RAM: **%sMB**\n" +
                "Total RAM: **%sMB**\n" +
                "Git Commit: **%s**\n" +
                "Build Number: **%s**\n" +
                "Branch: **%s**\n" +
                "Stored values:\n" +
                " - Players: **%s**\n" +
                " - Parties: **%s**", AuroraMCAPI.getInfo().getName(),new PunishmentLength((System.currentTimeMillis() - AuroraMCAPI.getStartTime())/3600000d), bean.getHeapMemoryUsage().getUsed()/1048576,Runtime.getRuntime().maxMemory()/1048576, gitCommit, buildNumber, branch, ProxyAPI.getPlayers().size(), ProxyAPI.getParties().size())));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
