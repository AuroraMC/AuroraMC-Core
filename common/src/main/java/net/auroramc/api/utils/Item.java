/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.utils;

import java.util.List;

public class Item {

    private final String material;
    private final String name;
    private final int amount;
    private final String lore;
    private final short data;
    private final boolean glowing;
    private final String skullOwner;
    private final int r, g, b;
    private List<Pattern> patterns;
    private String baseColour;
    private String base64;


    public Item(String material, String name, int amount, String lore, short data, boolean glowing, String skullOwner) {
        this.material = material;
        this.name = name;
        this.amount = amount;
        this.lore = lore;
        this.data = data;
        this.glowing = glowing;
        this.skullOwner = skullOwner;
        r = g = b = -1;
        patterns = null;
        base64 = null;
        baseColour = null;
    }

    public Item(String material, String name, int amount, String lore, short data, boolean glowing, int r, int g, int b) {
        this.material = material;
        this.name = name;
        this.amount = amount;
        this.lore = lore;
        this.data = data;
        this.glowing = glowing;
        this.r = r;
        this.b = b;
        this.g = g;
        skullOwner = null;
        patterns = null;
        base64 = null;
        baseColour = null;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public int getAmount() {
        return amount;
    }

    public short getData() {
        return data;
    }

    public String getLore() {
        return lore;
    }

    public String getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getSkullOwner() {
        return skullOwner;
    }

    public void setBaseColour(String baseColour) {
        this.baseColour = baseColour;
    }

    public String getBaseColour() {
        return baseColour;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public static class Pattern {

        private final String patternType;
        private final String dye;
        public Pattern(String dye, String patternType) {
            this.patternType = patternType;
            this.dye = dye;
        }

        public String getDye() {
            return dye;
        }

        public String getPatternType() {
            return patternType;
        }
    }
}
