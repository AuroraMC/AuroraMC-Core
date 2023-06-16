/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
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
