/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils;

import org.bukkit.Material;
import org.json.JSONObject;

import java.util.UUID;

public class GameLog {

    private final UUID uuid;
    private final long timestamp;
    private final GameType game;
    private final String server;
    private final JSONObject data;

    public GameLog(UUID uuid, long timestamp, String game, String server, JSONObject data) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.game = GameType.valueOf(game);

        this.server = server;
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public GameType getGame() {
        return game;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServer() {
        return server;
    }

    public enum GameType {
        CRYSTAL_QUEST("&b&lCrystal Quest", Material.NETHER_STAR, (short)0),
        FFA("&c&lFFA", Material.IRON_AXE, (short)0),
        HOTPOTATO("&c&lHotPotato", Material.BAKED_POTATO, (short)0),
        PAINTBALL("&a&lPaintball", Material.SNOW_BALL, (short)0),
        RUN("&e&lRun", Material.STAINED_CLAY, (short)14),
        TAG("&c&lTag", Material.LEASH, (short)0),
        SPLEEF("&b&lSpleef", Material.IRON_SPADE, (short)0);

        private final String name;
        private final Material item;
        private final short data;

        GameType(String name, Material item, short data) {
            this.name = name;
            this.item = item;
            this.data = data;
        }

        public Material getItem() {
            return item;
        }

        public String getName() {
            return name;
        }

        public short getData() {
            return data;
        }
    }
}
