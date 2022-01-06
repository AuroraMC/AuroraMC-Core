/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.core.api.punishments.Rule;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommandPunishmentLookup extends Command {

    public CommandPunishmentLookup() {
        super("punishmentlookup", Collections.singletonList("plookup"),  Arrays.asList(Permission.MODERATION,Permission.ADMIN), false, null);
    }

    private static final String[] statuses = {"&aActive","&6Pending Approval","&aSM Approved","&cSM Denied","&eExpired","&eExpired (Approved)","&7Warning"};
    private static final String[] weights = {"&2Light", "&aMedium", "&eHeavy", "&6Severe", "&4Extreme"};

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if(args.size() == 1) {
            if(args.get(0).matches("[A-Z0-9]{8}")) {
                String code = args.remove(0);
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("Performing punishment lookup for [**%s**]...", code)));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if(punishment != null) {
                            String name = AuroraMCAPI.getDbManager().getNameFromID(punishment.getPunished());
                            Rule rule = AuroraMCAPI.getRules().getRule(punishment.getRuleID());
                            TextComponent component = new TextComponent(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("Punishment Lookup for punishment **%s**:\n" +
                                    "Punished User: **%s**\n" +
                                    "Type: **%s**\n" +
                                    "Reason: **%s**\n" +
                                    "Weight: **%s**\n" +
                                    "Issued at: **%s**\n" +
                                    "Length: **%s**\n" +
                                    "Issued By: **%s**\n" +
                                    "Status: %s&r%s", code, name, ((punishment.getStatus() == 7)?"Warning":((rule.getType() == 1)?"Chat Offence":((rule.getType() == 2)?"Game Offence":"Misc Offence"))), rule.getRuleName() + " - " + punishment.getExtraNotes(), weights[rule.getWeight()-1], new Date(punishment.getIssued()), ((punishment.getExpire() == -1)?new PunishmentLength(-1):new PunishmentLength((punishment.getExpire() - punishment.getIssued()) / 3600000d)), punishment.getPunisherName(), statuses[punishment.getStatus()-1], ((punishment.getRemovalReason() == null)?"":String.format("\n \nRemoval Reason: **%s**\nRemoval Timestamp: **%s**\nRemoved By: **%s**", punishment.getRemovalReason(), new Date(punishment.getRemovalTimestamp()), punishment.getRemover())))));

                            if (player.hasPermission("debug.info")) {
                                TextComponent extra = new TextComponent("\n\n[Remove: Reprieve]");
                                extra.setColor(ChatColor.GREEN);
                                extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AuroraMCAPI.getFormatter().convert("&aClick here to remove punishment as a reprieve.")).create()));
                                extra.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/appeal " + code + " Reprieve"));
                                component.addExtra(extra);

                                extra = new TextComponent("\n\n[Remove: Compromised]");
                                extra.setColor(ChatColor.GOLD);
                                extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AuroraMCAPI.getFormatter().convert("&6Click here to remove punishment as Compromised.")).create()));
                                extra.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/appeal " + code + " Compromised"));
                                component.addExtra(extra);

                                extra = new TextComponent(" [Remove: False]");
                                extra.setColor(ChatColor.RED);
                                extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AuroraMCAPI.getFormatter().convert("&cClick here to remove punishment as false.")).create()));
                                extra.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/appeal " + code + " False"));
                                component.addExtra(extra);
                            }

                            player.getPlayer().spigot().sendMessage(component);
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());

        } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", "Invalid syntax. Correct syntax: **/punishmentlookup [Punishment Code]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", "Invalid syntax. Correct syntax: **/punishmentlookup [Punishment Code]**"));
        }

    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
