package net.auroramc.core.gui.misc;

import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.lookup.IPLookup;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.punishments.PunishmentLength;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class RecruitmentLookup extends GUI {


    public RecruitmentLookup(String name, PunishmentHistory history, PlayerStatistics statistics, IPLookup ipprofile) {
        super("&3&lRecruitment Lookup", 2, true);

        for (int i = 0; i <= 8; i++) {
            if (i < 3) {
                this.setItem(i, 0, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 7));
                this.setItem(i, 8, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 7));
            }
            this.setItem(0, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 7));
            this.setItem(2, i, new GUIItem(Material.STAINED_GLASS_PANE, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 7));
        }
        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&l%s's Lookup Information", name), 1, "", (short) 3, false, name));

        this.setItem(1, 2, new GUIItem(Material.WATCH, "&3&lIn-Game Time", 1, String.format(";&rRequirement met? %s;&rTotal In-game Time: **%s**", ((statistics.getLobbyTimeMs() + statistics.getGameTimeMs() >= 36000000)?"&a✔":"&c✘"), (new PunishmentLength(statistics.getLobbyTimeMs() + statistics.getGameTimeMs() / 3600000d)).toString())));
        long light = -1, medium = -1, heavy = -1, severe = -1, extreme = -1;
        for (Punishment punishment : history.getPunishments()) {
            switch (AuroraMCAPI.getRules().getRule(punishment.getRuleID()).getWeight()) {
                case 1:
                    if (light == -1 && (punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                        light = punishment.getIssued();
                    }
                    break;
                case 2:
                    if (medium == -1 && ((punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve")) || (punishment.getStatus() == 3 && punishment.getRemover() == null))) {
                        medium = punishment.getIssued();
                    }
                    break;
                case 3:
                    if (heavy == -1 && !(punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                        heavy = punishment.getIssued();
                    }
                    break;
                case 4:
                    if (severe == -1 && !(punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                        severe = punishment.getIssued();
                    }
                    break;
                case 5:
                    if (extreme == -1 && !(punishment.getRemover().equals("AuroraMCAppeals") && punishment.getRemovalReason().equals("Reprieve"))) {
                        extreme = punishment.getIssued();
                    }
                    break;


            }
        }
        this.setItem(1, 4, new GUIItem(Material.DIAMOND_SWORD, "&3&lRecent Punishment", 1, ";&rRequirement met? %s;&rLast Punishments:;&rLight: **%s**;&rMedium: **%s**;&rHeavy: **%s**;&rSevere: **%s**;&rExtreme: **%s**"));
        this.setItem(1, 6, new GUIItem(Material.PAPER, "&3&lIP Profile", 1, String.format(";This player has **%s** known alts.;Of those, **%s** have an active punishment.", ipprofile.getNames().size(), ipprofile.getNames().stream().filter(lookupUser -> lookupUser.isBanned() || lookupUser.isMuted()).count())));
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}
