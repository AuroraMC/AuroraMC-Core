/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.api.utils;

import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.Iterator;

public class ServerChatUtils {


    public static MentionMessage processMentions(AuroraMCServerPlayer recipient, TextComponent message) {
        TextComponent component = new TextComponent("");
        boolean mentionFound = false;

        if (message.getText().equals(" ")) {
            component.setText(" ");
        } else {
            String[] args = message.getText().split(" ");

            Iterator<String> iterator = Arrays.stream(args).iterator();
            while (iterator.hasNext()) {
                String arg = iterator.next();
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
        }
        if (message.getExtra() != null && message.getExtra().size() > 0) {
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

        if (message.getText().equals(" ")) {
            component.setText(" ");
        } else {
            String[] args = message.getText().split(" ");

                Iterator<String> iterator = Arrays.stream(args).iterator();
                while (iterator.hasNext()) {
                    String arg = iterator.next();
                    boolean found = false;
                    for (AuroraMCServerPlayer player : ServerAPI.getPlayers()) {
                        if (player.isVanished()) {
                            continue;
                        }
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
                            found = true;
                            break;
                        }
                    }
                    if (!found && !arg.equals("")) {
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

        if (message.getExtra() != null && message.getExtra().size() > 0) {
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
