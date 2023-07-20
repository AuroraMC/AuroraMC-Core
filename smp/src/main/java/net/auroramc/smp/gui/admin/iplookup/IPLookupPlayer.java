/*
 * Copyright (c) 2022-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.admin.iplookup;

import net.auroramc.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class IPLookupPlayer extends GUI {


    public IPLookupPlayer(PlayerProfile playerProfile) {
        super("&3&l IP Lookup for " + playerProfile.getPlayerName(), 3, true);
        border("&3&l IP Lookup for " + playerProfile.getPlayerName(), "");

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&lIP Lookup for %s", playerProfile.getPlayerName()), 1, "", (short)0, false, playerProfile.getPlayerName()));

        this.setItem(1, 2, new GUIItem(Material.BOOKSHELF, "&3&lIP Profiles", 1, String.format("&r&fThis user has **%s** IP Profiles.;;&r&fMost recent profiles:;**IP Profile #%s**", playerProfile.getNumberOfProfiles(), String.join("**;**IP Profile #", playerProfile.getProfiles()))));
        this.setItem(1, 4, new GUIItem(Material.BOOK, "&3&lLatest Information", 1, String.format("&r&fLatest IP Profile: **%s**;;&r&fThe last person to join on this IP was:;**%s**", playerProfile.getLatestProfile(), playerProfile.getLastUsedBy())));
        this.setItem(1, 6, new GUIItem(Material.WRITABLE_BOOK, "&3&lAlternate Accounts", 1, String.format(";&r&fTheir IP Profiles are;&r&fshared with **%s** other accounts.;;&r&fMost recent alts:;**%s**", playerProfile.getSharedAccounts(), String.join("**;**", playerProfile.getMostRecentAlts()))));
        this.setItem(2, 3, new GUIItem(Material.EMERALD_BLOCK, "&3&lPunishment Information", 1, String.format(";&r&fOf all known alts:;**%s** are currently muted.;**%s** are currently banned.", playerProfile.getMutes(), playerProfile.getBans())));
        this.setItem(2, 5, new GUIItem(Material.REDSTONE_BLOCK, "&3&lGlobal Account Suspension Status", 1, String.format(";&r&fStatus: %s%s", ((playerProfile.isGlobalAccountSuspended())?"&cSuspended":"&aNot Suspended"), ((playerProfile.isGlobalAccountSuspended())?";&r&fReason: **" + playerProfile.getGlobalAccountSuspensionReason() + "**":""))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
    }
}
