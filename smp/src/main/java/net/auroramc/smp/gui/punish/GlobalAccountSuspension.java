/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.gui.punish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalAccountSuspension extends GUI {

    private final AuroraMCServerPlayer player;
    private final String name;
    private final int id;
    private final String extraDetails;

    public GlobalAccountSuspension(AuroraMCServerPlayer player, String name, int id, String extraDetails) {
        super(String.format("&4&lGlobal Ban %s", name), 2, true);

        this.extraDetails = extraDetails;
        this.name = name;
        this.id = id;
        this.player = player;

        this.setItem(1, 4, new GUIItem(Material.REDSTONE_BLOCK, "&4&lAre you sure?", 1, "&r&fClicking on this block will;&r&ftaken as confirmation that you;&r&fwish to suspend this user."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.REDSTONE_BLOCK) {
            String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
            player.closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!AuroraMCAPI.isTestServer()) {
                        AuroraMCAPI.getDbManager().issuePunishment(code, id, 22, extraDetails, -1, System.currentTimeMillis(), -1, 1, AuroraMCAPI.getDbManager().getUUIDFromName(name).toString());
                        AuroraMCAPI.getDbManager().globalAccountSuspend(code, id, player.getId(), System.currentTimeMillis(), extraDetails);
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("GlobalAccountSuspend");
                        out.writeUTF(code);
                        out.writeUTF(extraDetails);
                        player.sendPluginMessage(out.toByteArray());
                    }

                    player.sendMessage(TextFormatter.pluginMessage("Punishments", "Successfully applied Global Account Suspension."));
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
        }
    }
}
