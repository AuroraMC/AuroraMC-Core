/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class ChatEmote extends Cosmetic {

    private final ChatColor color;
    private final boolean bold;

    public ChatEmote(int id, String name, String code, String emote, ChatColor color, boolean bold, UnlockMode unlockMode, List<Permission> permissions, List<Rank> ranks, Rarity rarity) {
        super(id, CosmeticType.CHAT_EMOTE, name, code, emote, unlockMode, -1, permissions, ranks, "", false, "PAPER", (short)0, rarity);
        this.color = color;
        this.bold = bold;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
    }

    public boolean isBold() {
        return bold;
    }

    public ChatColor getColor() {
        return color;
    }
}
