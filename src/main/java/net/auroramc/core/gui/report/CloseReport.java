package net.auroramc.core.gui.report;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerReport;
import net.auroramc.core.api.punishments.Rule;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CloseReport extends GUI {

    private final AuroraMCPlayer player;

    public CloseReport(AuroraMCPlayer player) {
        super("&3&lClose Report", 2, true);

        this.player = player;

        for (int i = 0; i <= 8; i++) {
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, "&r ", 1, "", (short) 7));
            this.setItem(1, i, new GUIItem(Material.STAINED_GLASS_PANE, "&r ", 1, "", (short) 7));
            this.setItem(2, i, new GUIItem(Material.STAINED_GLASS_PANE, "&r ", 1, "", (short) 7));
        }

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s Report", WordUtils.capitalizeFully(player.getActiveReport().getType().name())), 1, String.format(";&rUser reported: **%s**;&rReason: **%s**", player.getActiveReport().getSuspectName(), player.getActiveReport().getReason().getName()), (short)3, false, player.getActiveReport().getSuspectName()));

        int column = 2;
        if (player.getActiveReport().getReason().getAltRule() != null) {
            column--;
            this.setItem(1, 7, new GUIItem(Material.STAINED_CLAY, String.format("&a&lAccept as %s", AuroraMCAPI.getRules().getRule(player.getActiveReport().getReason().getAltRule()).getRuleName()), 1, ";&rClick here to &aaccept&r this report!", (short)13));
        }

        Rule rule = AuroraMCAPI.getRules().getRule(player.getActiveReport().getReason().getDefaultRule());

        this.setItem(1, column, new GUIItem(Material.STAINED_CLAY, "&c&lReject", 1, ";&rClick here to &creject&r this report!", (short)14));
        this.setItem(1, column + 2, new GUIItem(Material.STAINED_CLAY, "&6&lAccept under Different Rule", 1, ";&rClick here to &aaccept&r this report;&rbut change the rule type.", (short)1));
        this.setItem(1, column + 4, new GUIItem(Material.STAINED_CLAY, String.format("&a&lAccept as %s", rule.getRuleName()), 1, ";&rClick here to &aaccept&r this report!", (short)13));

        this.setItem(2, 3, new GUIItem(Material.SKULL_ITEM, "&c&lForward to Leadership", 1, ";&rForward this report to the;&rLeadership team for handling.", (short)3, false, "MHF_ArrowUp"));
        this.setItem(2, 5, new GUIItem(Material.BARRIER, "&c&lAbort", 1, ";&rAbort this report for another;&rstaff member to handle."));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() == Material.STAINED_GLASS_PANE) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.SKULL_ITEM && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Forward to Leadership")) {
            player.getActiveReport().forwardToLeadership(player);
            player.setActiveReport(null);
            player.getPlayer().closeInventory();
            return;
        } else if (item.getType() == Material.SKULL_ITEM) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        if (item.getType() == Material.BARRIER) {
            player.getActiveReport().abort(player);
            player.setActiveReport(null);
            player.getPlayer().closeInventory();
            return;
        }

        switch (column) {
            case 1:
            case 2: {
                player.getActiveReport().handle(player, PlayerReport.ReportOutcome.DENIED, null, false);
                player.setActiveReport(null);
                player.getPlayer().closeInventory();
                break;
            }
            case 3:
            case 4: {
                ChangeReportReasonListing listing = new ChangeReportReasonListing(player, player.getActiveReport().getType());
                listing.open(player);
                AuroraMCAPI.openGUI(player, listing);
                break;
            }
            case 5:
            case 6: {
                player.getActiveReport().handle(player, PlayerReport.ReportOutcome.ACCEPTED, player.getActiveReport().getReason(), false);
                player.setActiveReport(null);
                player.getPlayer().closeInventory();
                break;
            }
            case 7: {
                player.getActiveReport().handle(player, PlayerReport.ReportOutcome.ACCEPTED, player.getActiveReport().getReason(), true);
                player.setActiveReport(null);
                player.getPlayer().closeInventory();
                break;
            }
        }
    }
}
