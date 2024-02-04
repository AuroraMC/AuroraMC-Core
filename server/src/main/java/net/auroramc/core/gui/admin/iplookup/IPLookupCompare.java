/*
 * Copyright (c) 2022-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.core.gui.admin.iplookup;

import net.auroramc.api.punishments.ipprofiles.ProfileComparison;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class IPLookupCompare extends GUI {

    public IPLookupCompare(ProfileComparison playerProfile) {
        super("&3&l IP Lookup Comparison", 4, true);
        border("&3&l IP Lookup Comparison", "");

        this.setItem(1, 3, new GUIItem(Material.SKULL_ITEM, playerProfile.getFirstUser(), 1, "", (short)3, false, playerProfile.getFirstUser()));
        this.setItem(1, 4, new GUIItem(Material.REDSTONE_COMPARATOR, "&3&lComparing IP Profiles"));
        this.setItem(1, 5, new GUIItem(Material.SKULL_ITEM, playerProfile.getSecondUser(), 1, "", (short)3, false, playerProfile.getSecondUser()));

        this.setItem(2, 3, new GUIItem(Material.BOOK_AND_QUILL, "&3&lAlternate Accounts", 1, String.format(";&r&fThese users have;&r&f**%s** alt accounts in common.;;&r&fMost recent common alts:;**%s**", playerProfile.getAmountOfCommonAlts(), String.join("**;**", playerProfile.getCommonAlts()))));
        this.setItem(2, 4, new GUIItem(Material.BOOKSHELF, "&3&lCommon IP Profiles", 1, String.format("&r&fThese user have;**%s** IP Profiles in common.;;&r&fMost recent common profiles:;**IP Profile #%s**", playerProfile.getAmountOfCommonProfiles(), String.join("**;**IP Profile #", playerProfile.getCommonProfiles()))));
        this.setItem(2, 5, new GUIItem(Material.EMERALD_BLOCK, "&3&lPunishment Information", 1, String.format(";&r&fOf all known alts:;**%s** are currently muted.;**%s** are currently banned.", playerProfile.getMutes(), playerProfile.getBans())));
        this.setItem(3, 4, new GUIItem(Material.BOOK, "&3&lLatest Information", 1, String.format("&r&fLatest IP Profile for **%s**: **%s**;&r&fLatest IP Profile for **%s**: **%s**", playerProfile.getFirstUser(), playerProfile.getFirstProfile(), playerProfile.getSecondUser(), playerProfile.getSecondProfile())));

    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
    }

}
