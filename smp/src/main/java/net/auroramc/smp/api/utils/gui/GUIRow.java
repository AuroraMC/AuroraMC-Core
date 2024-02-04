/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.api.utils.gui;

import net.auroramc.smp.api.exception.InvalidColumnException;
import net.auroramc.smp.api.exception.InvalidRowException;

import java.util.HashMap;
import java.util.Map;

public class GUIRow {

    private final Map<Integer, GUIItem> row;
    private final int rowNo;

    public GUIRow(int rowNo) {
        row = new HashMap<>();
        if (rowNo > 5 || rowNo < 0) {
            throw new InvalidRowException();
        }
        this.rowNo = rowNo;
    }

    public void setItem(int i, GUIItem item) {
        if (i > 8 || i < 0) {
            throw new InvalidColumnException();
        }

        row.put(i, item);
    }

    public int getRowNo() {
        return rowNo;
    }

    public Map<Integer, GUIItem> getRow() {
        return new HashMap<>(row);
    }

    public GUIItem getItem(int column) {
        return row.get(column);
    }
}
