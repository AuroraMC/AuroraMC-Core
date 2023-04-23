/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ServerChatUtils {


    public static MentionMessage processMentions(AuroraMCServerPlayer recipient, TextComponent message) {
        TextComponent component = new TextComponent("");
        boolean mentionFound = false;

        String[] args = message.getText().split(" ");

        for (String arg : args) {
            if (arg.equalsIgnoreCase(recipient.getByDisguiseName()) && !recipient.isVanished()) {
                if (recipient.isDisguised() && recipient.getPreferences().isHideDisguiseNameEnabled()) {
                    arg = recipient.getName();
                } else {
                    arg = recipient.getByDisguiseName();
                }
                TextComponent component1 = new TextComponent(arg);
                component1.setBold(message.isBold());
                component1.setColor(ChatColor.AQUA);
                component1.setItalic(message.isItalic());
                component1.setObfuscated(message.isObfuscated());
                component1.setStrikethrough(message.isStrikethrough());
                component1.setUnderlined(message.isUnderlined());
                component.addExtra(component1);
                mentionFound = true;
            } else if (!arg.equals("")) {
                TextComponent component1 = new TextComponent(arg);
                component1.setBold(message.isBold());
                component1.setColor(message.getColor());
                component1.setItalic(message.isItalic());
                component1.setObfuscated(message.isObfuscated());
                component1.setStrikethrough(message.isStrikethrough());
                component1.setUnderlined(message.isUnderlined());
                component.addExtra(component1);
            }
        }

        if (message.getExtra().size() > 0) {
            for (BaseComponent component1 : message.getExtra()) {
                MentionMessage mentionMessage = processMentions(recipient, (TextComponent) component1);
                mentionFound = mentionFound || mentionMessage.isMentionFound();
                component.addExtra(mentionMessage.getFormattedText());
            }
        }
        return new MentionMessage(component, mentionFound);
    }

    public static BaseComponent processMentions(TextComponent message) {
        TextComponent component = new TextComponent("");

        String[] args = message.getText().split(" ");

        for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
            if (player.isVanished()) {
                continue;
            }
            for (String arg : args) {
                if (arg.equalsIgnoreCase(player.getByDisguiseName()) && !player.isVanished()) {
                    if (player.isDisguised() && player.getPreferences().isHideDisguiseNameEnabled()) {
                        arg = player.getName();
                    } else {
                        arg = player.getByDisguiseName();
                    }
                    TextComponent component1 = new TextComponent(arg);
                    component1.setBold(message.isBold());
                    component1.setColor(ChatColor.AQUA);
                    component1.setItalic(message.isItalic());
                    component1.setObfuscated(message.isObfuscated());
                    component1.setStrikethrough(message.isStrikethrough());
                    component1.setUnderlined(message.isUnderlined());
                    component.addExtra(component1);
                } else if (!arg.equals("")) {
                    TextComponent component1 = new TextComponent(arg);
                    component1.setBold(message.isBold());
                    component1.setColor(message.getColor());
                    component1.setItalic(message.isItalic());
                    component1.setObfuscated(message.isObfuscated());
                    component1.setStrikethrough(message.isStrikethrough());
                    component1.setUnderlined(message.isUnderlined());
                    component.addExtra(component1);
                }
            }
        }

        if (message.getExtra().size() > 0) {
            for (BaseComponent component1 : message.getExtra()) {
                component.addExtra(processMentions((TextComponent) component1));
            }
        }
        return component;
    }

    public static class MentionMessage {
        private final BaseComponent formattedText;
        private final boolean mentionFound;

        public MentionMessage(BaseComponent formattedText, boolean mentionFound) {
            this.formattedText = formattedText;
            this.mentionFound = mentionFound;
        }

        public boolean isMentionFound() {
            return mentionFound;
        }

        public BaseComponent getFormattedText() {
            return formattedText;
        }
    }

}
