/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.admin;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class CommandBuildVersions extends Command {


    public CommandBuildVersions() {
        super("bversions", Arrays.asList("buildversions", "bv"), Arrays.asList(Permission.ALL, Permission.DEBUG_INFO), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        StringBuilder sb = new StringBuilder();
        String buildNumber = null;
        String gitCommit = null;

        try {

            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                Enumeration<URL> resources = plugin.getClass().getClassLoader()
                        .getResources("META-INF/MANIFEST.MF");
                while (resources.hasMoreElements()) {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    // check that this is your manifest and do what you need or get the next one
                    Attributes attributes = manifest.getMainAttributes();

                    buildNumber = attributes.getValue("Jenkins-Build-Number");
                    gitCommit = attributes.getValue("Git-Commit");
                    if (buildNumber != null && gitCommit != null) {
                        sb.append("\n \n&3&l");
                        sb.append(attributes.getValue("Module-Name"));
                        sb.append("&r:\n" +
                                "Build Number: **");
                        sb.append(buildNumber);
                        sb.append("**\n" +
                                "Git Commit: **");
                        sb.append(gitCommit);
                        sb.append("**");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Server Manager", "Current Server Build Numbers:" +
                sb));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
