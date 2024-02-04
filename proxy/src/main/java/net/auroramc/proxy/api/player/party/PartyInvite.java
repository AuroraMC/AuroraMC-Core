/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.api.player.party;

import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class PartyInvite {

    private Party party;
    private final PartyPlayer player;
    private ScheduledTask task;
    private long expires;

    public PartyInvite(Party party, PartyPlayer player) {
        this.party = party;
        this.player = player;
        this.expires = System.currentTimeMillis() + 120000L;

        if (player.getPlayer() != null) {
            player.getPlayer().newInvite(this);
            TextComponent textComponent = new TextComponent("");

            TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
            lines.setBold(true);
            lines.setColor(ChatColor.DARK_AQUA);

            textComponent.addExtra(lines);
            textComponent.addExtra("\n\n");
            textComponent.addExtra(TextFormatter.highlight(String.format("You have been invited to **%s**'s party!", party.getLeader().getName())));
            textComponent.addExtra("\n\n");

            TextComponent accept = new TextComponent("ACCEPT");
            accept.setColor(ChatColor.GREEN);
            accept.setBold(true);

            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to accept the party request!").color(ChatColor.GREEN).create()));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party accept %s", party.getLeader().getName())));

            textComponent.addExtra(accept);
            textComponent.addExtra(" ");

            TextComponent deny = new TextComponent("DENY");
            deny.setColor(ChatColor.RED);
            deny.setBold(true);

            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to deny the party request!").color(ChatColor.RED).create()));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party deny %s", party.getLeader().getName())));

            textComponent.addExtra(deny);
            textComponent.addExtra("\n");
            TextComponent comp = new TextComponent("WARNING:");
            comp.setColor(ChatColor.RED);
            textComponent.addExtra(comp);
            textComponent.addExtra(" This invite will expire in two minutes.\n\n");
            textComponent.addExtra(lines);


            this.player.getPlayer().sendMessage(textComponent);
        }

        TextComponent textComponent = new TextComponent("");

        TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
        lines.setBold(true);
        lines.setColor(ChatColor.DARK_AQUA);

        textComponent.addExtra(lines);
        textComponent.addExtra("\n\n");
        textComponent.addExtra(TextFormatter.highlight(String.format("You have invited **%s** to your party!", player.getName())));
        textComponent.addExtra("\n\n");

        TextComponent deny = new TextComponent("CANCEL");
        deny.setColor(ChatColor.RED);
        deny.setBold(true);

        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to cancel your party request!").color(ChatColor.RED).create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party cancel %s", player.getName())));

        textComponent.addExtra(deny);
        textComponent.addExtra("\n\n");
        textComponent.addExtra(lines);
        party.getLeader().getPlayer().sendMessage(textComponent);
        task = ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), this::expired, 2, TimeUnit.MINUTES);
    }

    public PartyInvite(Party party, PartyPlayer player, long expires) {
        this.party = party;
        this.player = player;
        this.expires = expires;

        if (player.getPlayer() != null) {
            player.getPlayer().newInvite(this);
            TextComponent textComponent = new TextComponent("");

            TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
            lines.setBold(true);
            lines.setColor(ChatColor.DARK_AQUA);

            textComponent.addExtra(lines);
            textComponent.addExtra("\n\n");
            textComponent.addExtra(TextFormatter.highlight(String.format("You have been invited to **%s**'s party!", party.getLeader().getName())));
            textComponent.addExtra("\n\n");

            TextComponent accept = new TextComponent("ACCEPT");
            accept.setColor(ChatColor.GREEN);
            accept.setBold(true);

            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to accept the party request!").color(ChatColor.GREEN).create()));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party accept %s", party.getLeader().getName())));

            textComponent.addExtra(accept);
            textComponent.addExtra(" ");

            TextComponent deny = new TextComponent("DENY");
            deny.setColor(ChatColor.RED);
            deny.setBold(true);

            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to deny the party request!").color(ChatColor.RED).create()));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party deny %s", party.getLeader().getName())));

            textComponent.addExtra(deny);
            textComponent.addExtra("\n");
            TextComponent comp = new TextComponent("WARNING:");
            comp.setColor(ChatColor.RED);
            textComponent.addExtra(comp);
            textComponent.addExtra(" This invite will expire in two minutes.\n\n");
            textComponent.addExtra(lines);


            this.player.getPlayer().sendMessage(textComponent);
        }

        if (party != null) {
            if (party.getLeader().getPlayer() != null) {
                TextComponent textComponent = new TextComponent("");

                TextComponent lines = new TextComponent("▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆▆");
                lines.setBold(true);
                lines.setColor(ChatColor.DARK_AQUA);

                textComponent.addExtra(lines);
                textComponent.addExtra("\n\n");
                textComponent.addExtra(TextFormatter.highlight(String.format("You have invited **%s** to your party!", player.getName())));
                textComponent.addExtra("\n\n");

                TextComponent deny = new TextComponent("CANCEL");
                deny.setColor(ChatColor.RED);
                deny.setBold(true);

                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to cancel your party request!").color(ChatColor.RED).create()));
                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/party cancel %s", player.getName())));

                textComponent.addExtra(deny);
                textComponent.addExtra("\n\n");
                textComponent.addExtra(lines);
                party.getLeader().getPlayer().sendMessage(textComponent);
            }
        }
        task = ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), this::expired, expires - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public void expired() {
        if (player.getPlayer() != null) {
            if (player.getPlayer().isOnline()) {
                player.getPlayer().sendMessage(TextFormatter.pluginMessage("Party", String.format("Your party invite from **%s** expired.", party.getLeader().getName())));
            }
            player.getPlayer().inviteDeclined(this);
        }
        party.expired(this);
        task.cancel();
    }

    public void responded(boolean accepted) {
        this.task.cancel();
        if (player.getPlayer() != null) {
            if (accepted) {
                player.getPlayer().inviteAccepted(this);
            } else {
                player.getPlayer().inviteDeclined(this);
            }
        }
    }

    public Party getParty() {
        return party;
    }

    public PartyPlayer getPlayer() {
        return player;
    }

    public long getExpires() {
        return expires;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
