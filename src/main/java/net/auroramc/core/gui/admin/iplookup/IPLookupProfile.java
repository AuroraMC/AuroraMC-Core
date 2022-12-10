/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.admin.iplookup;

import net.auroramc.core.api.punishments.ipprofiles.IPProfile;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class IPLookupProfile extends GUI {


    public IPLookupProfile(IPProfile playerProfile) {
        super("&3&l IP Lookup for #" + playerProfile.getId(), 2, true);
        border("&3&l IP Lookup for Profile #" + playerProfile.getId(), "");

        this.setItem(0, 4, new GUIItem(Material.PAPER, String.format("&3&lIP Lookup for Profile #%s", playerProfile.getId())));

        this.setItem(1, 1, new GUIItem(Material.EMERALD_BLOCK, "&3&lPunishment Information", 1, String.format(";&r&fOf known accounts:;**%s** are currently muted.;**%s** are currently banned.", playerProfile.getMutes(), playerProfile.getBans())));
        this.setItem(1, 3, new GUIItem(Material.BOOK, "&3&lAccounts", 1, String.format(";&r&fThis IP Profile is;&r&fshared with **%s** accounts.;;&r&fMost recent alts:;**%s**", playerProfile.getNumberOfAccounts(), String.join("**;**", playerProfile.getAccounts()))));
        this.setItem(1, 5, new GUIItem(Material.BARRIER, "&3&lLatest Information", 1, String.format("&r&fThis IP was last used by:;**%s**;;&r&fDate:;**%s**", playerProfile.getLastUsedBy(), new Date(playerProfile.getLastUsedAt()))));
        this.setItem(1, 7, new GUIItem(Material.REDSTONE_BLOCK, "&3&lGlobal Account Suspension Status", 1, String.format(";&r&fStatus: %s%s", ((playerProfile.isGlobalAccountSuspended())?"&cSuspended":"&aNot Suspended"), ((playerProfile.isGlobalAccountSuspended())?";&r&fReason: **" + playerProfile.getGlobalAccountSuspensionReason() + "**":""))));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
    }
}
