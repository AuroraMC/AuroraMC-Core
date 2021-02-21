package net.auroramc.core.gui.cosmetics;

import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.utils.gui.GUI;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class CosmeticsListing extends GUI {


    public CosmeticsListing(AuroraMCPlayer player, Cosmetic.CosmeticType type, ItemStack item) {
        super(String.format("&3&l%s", type.getName()), 5, true);



    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {

    }
}
