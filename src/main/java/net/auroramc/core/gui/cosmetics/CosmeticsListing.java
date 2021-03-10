package net.auroramc.core.gui.cosmetics;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CosmeticsListing extends GUI {

    private AuroraMCPlayer player;
    private List<Cosmetic> cosmetics;

    public CosmeticsListing(AuroraMCPlayer player, Cosmetic.CosmeticType type, ItemStack item) {
        super(String.format("&3&l%s", type.getName()), 5, true);

        this.player = player;

        border(String.format("&3&l%s", type.getName()), "");
        this.setItem(0, 4, new GUIItem(item));
        this.setItem(0, 0, new GUIItem(Material.ARROW, "&3&lBack"));

        cosmetics = AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getType() == type).filter(cosmetic -> cosmetic.hasUnlocked(player) || cosmetic.showIfNotUnlocked()).collect(Collectors.toList());
        if (cosmetics.size() > 28) {
            this.setItem(5, 7, new GUIItem(Material.ARROW, "&3&lNext Page"));
        }

        int column = 1;
        int row = 1;
        for (Cosmetic cosmetic : cosmetics) {
            this.setItem(row, column, new GUIItem(cosmetic.getItem(player)));
            column++;
            if (column == 8) {
                row++;
                column = 1;
                if (row == 5) {
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}
