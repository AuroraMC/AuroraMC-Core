/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.commands.general.friends;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.FriendStatus;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.player.friends.Friend;
import net.auroramc.api.player.friends.FriendsList;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandFriend extends ProxyCommand {
    public CommandFriend() {
        super("friend", Arrays.asList("f", "friends"), Collections.singletonList(Permission.PLAYER), true, null);
        this.registerSubcommand("status", Collections.emptyList(), new CommandFriendStatus());
        this.registerSubcommand("remove", Collections.emptyList(), new CommandFriendRemove());
        this.registerSubcommand("favourite", Collections.emptyList(), new CommandFriendFavourite());
        this.registerSubcommand("visibility", Collections.emptyList(), new CommandFriendVisibility());
        this.registerSubcommand("accept", Collections.emptyList(), new CommandFriendAccept());
        this.registerSubcommand("deny", Collections.emptyList(), new CommandFriendDeny());
        this.registerSubcommand("cancel", Collections.emptyList(), new CommandFriendCancel());
        this.registerSubcommand("help", Collections.emptyList(), new CommandFriendHelp());
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("OpenFriendGUI");
            out.writeUTF(player.getName());
            player.sendPluginMessage(out.toByteArray());
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "status":
            case "remove":
            case "favourite":
            case "visibility":
            case "accept":
            case "deny":
            case "cancel":
            case "help":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                if (args.size() == 1) {
                    if (args.get(0).matches("[A-Za-z0-9_]{3,16}")) {
                        int limit = 250;
                        if (player.hasPermission("elite")) {
                            limit += 50;
                        }
                        if (player.hasPermission("master")) {
                            limit += 50;
                        }
                        if (player.hasPermission("plus")) {
                            limit += 50;
                        }
                        if (player.getFriendsList().getFriends().values().size() + (int) player.getFriendsList().getPendingFriendRequests().values().stream().filter((friend) -> friend.getType() == Friend.FriendType.PENDING_OUTGOING).count() >= limit) {
                            player.sendMessage(TextFormatter.pluginMessage("Friends", "You have already reached your Friends limit! Cancel pending outgoing friend requests or delete friends in order to add more! You can get more friend slots by purchasing a rank on our store!"));
                            return;
                        }
                        ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                            int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                            if (amcId > 0) {
                                UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                                if (player.getFriendsList().getPendingFriendRequests().containsKey(uuid)) {
                                    if (player.getFriendsList().getPendingFriendRequests().get(uuid).getType() == Friend.FriendType.PENDING_INCOMING) {
                                        //Friend request accepted
                                        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(uuid);
                                        Friend friend = player.getFriendsList().getPendingFriendRequests().get(uuid);
                                        if (pl != null) {
                                            AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(pl);
                                            FriendsList.VisibilityMode visibilityMode = auroraMCPlayer.getFriendsList().getVisibilityMode();
                                            player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:auroraMCPlayer.getServer().getName()):null), auroraMCPlayer.getFriendsList().getCurrentStatus(), true);
                                            visibilityMode = player.getFriendsList().getVisibilityMode();
                                            auroraMCPlayer.getFriendsList().friendRequestAccepted(player.getUniqueId(), true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:player.getServer().getName()):null), player.getFriendsList().getCurrentStatus(), true);
                                            pl.sendMessage(TextFormatter.pluginMessage("Friends", String.format("Your friend request to **%s** was accepted!", player.getName())));
                                        } else {
                                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                                UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "accepted", player.getUniqueId().toString(), uuid.toString());
                                                CommunicationUtils.sendMessage(message);
                                                FriendsList.VisibilityMode visibilityMode = AuroraMCAPI.getDbManager().getVisibilityMode(uuid);
                                                FriendStatus status = AuroraMCAPI.getDbManager().getFriendStatus(uuid);
                                                String server = AuroraMCAPI.getDbManager().getCurrentServer(uuid);
                                                player.getFriendsList().friendRequestAccepted(uuid, true, ((visibilityMode != FriendsList.VisibilityMode.NOBODY)?((visibilityMode == FriendsList.VisibilityMode.FAVOURITE_FRIENDS_ONLY)?null:server):null), status, true);
                                            } else {
                                                player.getFriendsList().friendRequestAccepted(uuid, false, null, (FriendStatus) AuroraMCAPI.getCosmetics().get(101), true);
                                            }
                                        }

                                        player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("You accepted a friend request from **%s**", friend.getName())));

                                        if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(30))) {
                                            player.getStats().achievementGained(AuroraMCAPI.getAchievement(30), 1, true);
                                        }

                                    } else {
                                        player.sendMessage(TextFormatter.pluginMessage("Friends", "You already have an outgoing friend request to that person."));
                                    }
                                    return;
                                } else {
                                    if (player.getFriendsList().getFriends().containsKey(uuid)) {
                                        player.sendMessage(TextFormatter.pluginMessage("Friends", "You already have that player friended!"));
                                        return;
                                    }
                                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                    if (target != null) {
                                        AuroraMCProxyPlayer auroraMCPlayer = ProxyAPI.getPlayer(target);
                                        if (!auroraMCPlayer.getPreferences().isFriendRequestsEnabled()) {
                                            player.sendMessage(TextFormatter.pluginMessage("Friends", "That player has friend requests disabled!"));
                                            return;
                                        }
                                        auroraMCPlayer.getFriendsList().newFriendRequest(player.getUniqueId(), player.getName(), player.getId(), false, true);
                                        TextComponent textComponent = new TextComponent("");

                                        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                                        lines.setBold(true);
                                        lines.setColor(ChatColor.DARK_AQUA);

                                        textComponent.addExtra(lines);
                                        textComponent.addExtra("\n\n");
                                        textComponent.addExtra(TextFormatter.highlight(String.format("Incoming Friend Request from **%s**", player.getName())));
                                        textComponent.addExtra("\n\n");

                                        TextComponent accept = new TextComponent("ACCEPT");
                                        accept.setColor(ChatColor.GREEN);
                                        accept.setBold(true);

                                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to accept the friend request!").color(ChatColor.GREEN).create()));
                                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/friend accept %s", player.getName())));

                                        textComponent.addExtra(accept);
                                        textComponent.addExtra(" ");

                                        TextComponent deny = new TextComponent("DENY");
                                        deny.setColor(ChatColor.RED);
                                        deny.setBold(true);

                                        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to deny the friend request!").color(ChatColor.RED).create()));
                                        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/friend deny %s", player.getName())));

                                        textComponent.addExtra(deny);
                                        textComponent.addExtra("\n\n");
                                        textComponent.addExtra(lines);

                                        auroraMCPlayer.sendMessage(textComponent);
                                    } else {
                                        if (!AuroraMCAPI.getDbManager().getPlayerPreferences(uuid).isFriendRequestsEnabled()) {
                                            player.sendMessage(TextFormatter.pluginMessage("Friends", "That player has friend requests disabled!"));
                                            return;
                                        }
                                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                            UUID proxy = AuroraMCAPI.getDbManager().getProxy(uuid);
                                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_FRIENDS, proxy.toString(), "incoming", player.getUniqueId().toString(), uuid.toString());
                                            CommunicationUtils.sendMessage(message);
                                        }
                                    }

                                    player.getFriendsList().newFriendRequest(uuid, args.get(0), amcId, true, true);

                                    TextComponent textComponent = new TextComponent("");

                                    TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                                    lines.setBold(true);
                                    lines.setColor(ChatColor.DARK_AQUA);

                                    textComponent.addExtra(lines);
                                    textComponent.addExtra("\n\n");
                                    textComponent.addExtra(TextFormatter.highlight(String.format("Friend request sent to **%s**", args.get(0))));
                                    textComponent.addExtra("\n\n");

                                    TextComponent deny = new TextComponent("CANCEL");
                                    deny.setColor(ChatColor.RED);
                                    deny.setBold(true);

                                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to cancel your friend request!").color(ChatColor.RED).create()));
                                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/friend cancel %s", args.get(0))));

                                    textComponent.addExtra(deny);
                                    textComponent.addExtra("\n\n");
                                    textComponent.addExtra(lines);
                                    player.sendMessage(textComponent);
                                    return;
                                }
                            }
                            player.sendMessage(TextFormatter.pluginMessage("Friends", String.format("No results found for **%s**. If they have recently changed their name, try searching their old name!", args.get(0))));
                        });
                        return;
                    }
                }
                player.sendMessage(TextFormatter.pluginMessage("Friends", "That sub-command is unrecognised. For a full list of sub-commands, please type **/friend help**."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> types = new ArrayList<>(Arrays.asList("status", "remove", "favourite", "visibility", "help", "accept", "deny", "cancel"));
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (String type : types) {
                if (type.toLowerCase().startsWith(lastToken.toLowerCase())) {
                    completions.add(type);
                }
            }
        } else if (numberArguments >= 2) {
            switch (args.get(0).toLowerCase()) {
                case "status":
                case "remove":
                case "favourite":
                case "visibility":
                case "accept":
                case "deny":
                case "cancel":
                case "help":
                    aliasUsed = args.remove(0);
                    return subcommands.get(aliasUsed).onTabComplete(player, aliasUsed, args, ((args.size() >= 1)?args.get(0):""), numberArguments - 1);
                default:
                    return new ArrayList<>();
            }
        }

        return completions;
    }
}
