package network.auroramc.core.gui.punish;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.punishments.*;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RuleListing extends GUI {

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final int type;
    private final String extraDetails;
    private final PunishmentHistory history;

    public RuleListing(AuroraMCPlayer player, String name, int id, int type, String extraDetails, List<Punishment> punishmentHistory) {
        super(String.format("&3&lPunish %s - %s", name, Type.TYPES[type - 1]), 5, true);

        this.player = player;
        this.name = name;
        this.id = id;
        this.type = type;
        this.extraDetails = extraDetails;
        this.history = new PunishmentHistory(id);

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lPunish %s", name), 1, "&rPlease choose a rule.", (short)3, false, name));

        for (Punishment punishment : punishmentHistory) {
            history.registerPunishment(punishment);
        }


        int row = 2;
        int column = 0;
        for (int i = 1;i <= 5;i++) {
            //For each weight
            row = 2;
            column = (i-1)*2;
            this.setItem(1, (i-1)*2, new GUIItem(Material.WOOL, Weight.WEIGHTS[i-1], 1, "&rPunishment Length: &b" + history.getType(type).generateLength(i).getFormatted(), Weight.WEIGHT_ICON_DATA[i-1]));
            for (Rule rule : AuroraMCAPI.getRules().getType(type).getWeight(i).getRules()) {
                this.setItem(row, column, new GUIItem(Material.BOOK, String.format("&3&l%s", rule.getRuleName()), 1, String.format("&r%s;;&rID: &b%s", rule.getRuleDescription(), rule.getRuleID())));
                row++;
                if (row > 5) {
                    column++;
                    row = 2;
                }
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item) {

    }
}
