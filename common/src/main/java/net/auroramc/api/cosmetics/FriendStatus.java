/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.permissions.Permission;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FriendStatus extends Cosmetic {

    private final static Map<String, FriendStatus> friendStatuses;

    static {
        friendStatuses = new HashMap<>();
    }

    private final String title;
    private final char colour;

    public FriendStatus(int id, String name, String displayName, UnlockMode unlockMode, int currency, List<Permission> permissions, List<Rank> ranks, String unlockMessage, String title, char colour, boolean showInGUI, Rarity rarity) {
        super(id, CosmeticType.FRIEND_STATUS, name, displayName, "", unlockMode, currency, permissions, ranks, unlockMessage, showInGUI, "NAME_TAG", (short) 0, rarity);
        this.title = title;
        this.colour = colour;
        friendStatuses.put(displayName, this);
    }

    public static Map<String, FriendStatus> getFriendStatuses() {
        return friendStatuses;
    }

    public static FriendStatus getByCode(String code) {
        return friendStatuses.get(code);
    }

    public String getTitle() {
        return title;
    }

    public char getColour() {
        return colour;
    }

    @Override
    public void onEquip(AuroraMCPlayer player) {
    }

    @Override
    public void onUnequip(AuroraMCPlayer player) {
    }
}
