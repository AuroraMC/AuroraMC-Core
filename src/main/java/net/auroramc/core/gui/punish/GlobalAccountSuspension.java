/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.punish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GlobalAccountSuspension extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;

    public GlobalAccountSuspension(AuroraMCPlayer player, String name, int id, String extraDetails) {
        super(String.format("&4&lGlobal Ban %s", name), 2, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;

        this.setItem(1, 4, new GUIItem(Material.REDSTONE_BLOCK, "&4&lAre you sure?", 1, "&rClicking on this block will;&rtaken as confirmation that you;&rwish to suspend this user."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.REDSTONE_BLOCK) {
            String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
            player.getPlayer().closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    AuroraMCAPI.getDbManager().issuePunishment(code, id, 22, extraDetails, -1, System.currentTimeMillis(), -1, 1, AuroraMCAPI.getDbManager().getUUIDFromName(name).toString());
                    AuroraMCAPI.getDbManager().globalAccountSuspend(code, id, player.getId(), System.currentTimeMillis(), extraDetails);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("GlobalAccountSuspend");
                    out.writeUTF(code);
                    out.writeUTF(extraDetails);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punishments", "Successfully applied Global Account Suspension."));
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
