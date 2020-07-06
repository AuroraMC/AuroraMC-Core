package network.auroramc.core.gui.punish;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.UUIDUtil;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
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
                    AuroraMCAPI.getDbManager().issuePunishment(code, id, 22, extraDetails, -1, System.currentTimeMillis(), -1, 1, UUIDUtil.getUUID(name).toString());
                    List<String> strings = AuroraMCAPI.getDbManager().globalAccountSuspend(code, id, player.getId(), System.currentTimeMillis(), extraDetails);
                    for (String id : strings) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("KickPlayer");
                        out.writeUTF(id);
                        out.writeUTF(AuroraMCAPI.getFormatter().pluginMessage("Punishments", String.format("" +
                                "You have been Globally Account Suspended.\n" +
                                "\n" +
                                "&rThe administration team has decided that due to your previous actions,\n" +
                                "&rwe are no longer going to allow your continued use of our services.\n" +
                                "\n" +
                                "&rThis type of punishment does not expire, and means you are no longer\n" +
                                "&rallowed to join the network, use the forums or communicate in Discord.\n" +
                                "\n" +
                                "&rAll logged IPs are now banned, and any account used to connect to the network\n" +
                                "&rthrough any logged IP will result in a permanent ban being issued.\n" +
                                "\n" +
                                "Reason: **%s**", extraDetails)));
                        player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                    }
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punishments", "Successfully applied Global Account Suspension."));
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
        }
    }
}
