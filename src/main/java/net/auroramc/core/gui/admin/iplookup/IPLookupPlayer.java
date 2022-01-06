/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.admin.iplookup;

import net.auroramc.core.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class IPLookupPlayer extends GUI {


    public IPLookupPlayer(PlayerProfile playerProfile) {
        super("&3&l IP Lookup for " + playerProfile.getPlayerName(), 3, true);
        border("&3&l IP Lookup for " + playerProfile.getPlayerName(), "");

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lIP Lookup for %s", playerProfile.getPlayerName()), 1, "", (short)3, false, playerProfile.getPlayerName()));

        this.setItem(1, 2, new GUIItem(Material.BOOKSHELF, "&3&lIP Profiles", 1, String.format("&rThis user has **%s** IP Profiles.;;&rMost recent profiles:;**IP Profile #%s**", playerProfile.getNumberOfProfiles(), String.join("**;**IP Profile #", playerProfile.getProfiles()))));
        this.setItem(1, 4, new GUIItem(Material.BOOK, "&3&lLatest Information", 1, String.format("&rLatest IP Profile: **%s**;;&rThe last person to join on this IP was:;**%s**", playerProfile.getLatestProfile(), playerProfile.getLastUsedBy())));
        this.setItem(1, 6, new GUIItem(Material.BOOK_AND_QUILL, "&3&lAlternate Accounts", 1, String.format(";&rTheir IP Profiles are;&rshared with **%s** other accounts.;;&rMost recent alts:;**%s**", playerProfile.getSharedAccounts(), String.join("**;**", playerProfile.getMostRecentAlts()))));
        this.setItem(2, 3, new GUIItem(Material.EMERALD_BLOCK, "&3&lPunishment Information", 1, String.format(";&rOf all known alts:;**%s** are currently muted.;**%s** are currently banned.", playerProfile.getMutes(), playerProfile.getBans())));
        this.setItem(2, 5, new GUIItem(Material.REDSTONE_BLOCK, "&3&lGlobal Account Suspension Status", 1, String.format(";&rStatus: %s%s", ((playerProfile.isGlobalAccountSuspended())?"&cSuspended":"&aNot Suspended"), ((playerProfile.isGlobalAccountSuspended())?";&rReason: **" + playerProfile.getGlobalAccountSuspensionReason() + "**":""))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
    }
}
