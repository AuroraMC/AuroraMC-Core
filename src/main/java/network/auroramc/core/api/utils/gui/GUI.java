package network.auroramc.core.api.utils.gui;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.exception.InvalidColumnException;
import network.auroramc.core.api.exception.InvalidRowException;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class GUI {

    private final int rows;
    private final String name;
    private final Map<Integer, GUIRow> inventory;
    private final boolean cancelEvent;

    public GUI(String name, int rows, boolean cancelEvent) {
        this.name = name;
        this.cancelEvent = cancelEvent;
        if (rows > 5 || rows < 0) {
            throw new InvalidRowException();
        }

        this.rows = rows;
        inventory = new HashMap<>();
        for (int i = 0;i <= rows;i++) {
            inventory.put(i, new GUIRow(i));
        }
    }

    public void setItem(int row, int column, GUIItem item) {
        if (row > 5 || row < 0) {
            throw new InvalidRowException();
        }

        if (column > 8 || column < 0) {
            throw new InvalidColumnException();
        }

        inventory.get(row).setItem(column, item);
    }

    public void open(AuroraMCPlayer player) {
        Inventory inventory = Bukkit.createInventory(null, (rows + 1) * 9, AuroraMCAPI.getFormatter().convert(name));
        for (int row = 0;row <= rows;row++) {
            for (int column = 0;column <=8;column++) {
                if (this.inventory.get(row).getItem(column) != null) {
                    inventory.setItem((row * 9) + column, this.inventory.get(row).getItem(column).getItem());
                }
            }
        }
        player.getPlayer().openInventory(inventory);
    }

    public int getSize() {
        return rows * 9;
    }

    public abstract void onClick(int row, int column, ItemStack item);

    public boolean cancelEvent() {
        return cancelEvent;
    }
}
