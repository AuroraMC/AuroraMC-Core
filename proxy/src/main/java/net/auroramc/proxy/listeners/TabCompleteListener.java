/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.listeners;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.command.Command;
import net.auroramc.api.permissions.Permission;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.event.TabCompleteResponseEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;
import java.util.stream.Collectors;

public class TabCompleteListener implements Listener {

    private Map<AuroraMCProxyPlayer, List<String>> completions;

    public TabCompleteListener() {
        completions = new HashMap<>();
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (e.getCursor().startsWith("/")) {
            AuroraMCProxyPlayer player = ProxyAPI.getPlayer((ProxiedPlayer) e.getSender());
            String message = e.getCursor();
            //This is a command, tab complete it.
            if (message.contains(" ")) {
                String commandLabel = message.split(" ")[0].replace("/","");
                ProxyCommand command = (ProxyCommand) AuroraMCAPI.getCommand(commandLabel);
                if (command != null) {
                    //This is a command that is recognised and they are tab completing a subcommand, check if they have permissions to use the command.
                    for (Permission permission : command.getPermission()) {
                        if (player.hasPermission(permission.getId())) {
                            ArrayList<String> args = new ArrayList<>(Arrays.asList(message.split(" ")));
                            args.remove(0);
                            List<String> finalCompletions = command.onTabComplete(player, commandLabel, args, ((message.endsWith(" ")) ? "" : args.get(args.size() - 1)), ((message.endsWith(" ")) ? args.size() + 1 : args.size()));
                            completions.put(player, finalCompletions);
                        }
                    }
                }
            } else {
                List<String> completions = AuroraMCAPI.getCommands().stream().filter((command) -> command.startsWith(message.split(" ")[0].replace("/","").toLowerCase())).collect(Collectors.toList());
                List<String> finalCompletions = new ArrayList<>();
                completionLoop:
                for (String commandLabel : completions) {
                    ProxyCommand command = (ProxyCommand) AuroraMCAPI.getCommand(commandLabel);
                    for (Permission permission : command.getPermission()) {
                        if (player.hasPermission(permission.getId())) {
                            if (player.getPendingConnection().getVersion() > 340) {
                                finalCompletions.add(commandLabel);
                            } else {
                                finalCompletions.add("/" + commandLabel);
                            }
                            continue completionLoop;
                        }
                    }
                }
                this.completions.put(player, finalCompletions);
            }

        }
    }

    @EventHandler
    public void onTabCompleteResponse(TabCompleteResponseEvent e) {
        AuroraMCProxyPlayer proxyPlayer = ProxyAPI.getPlayer((ProxiedPlayer) e.getReceiver());
        if (completions.containsKey(proxyPlayer)) {
            e.getSuggestions().addAll(completions.remove(proxyPlayer));
            Collections.sort(e.getSuggestions());
        }
    }
}
