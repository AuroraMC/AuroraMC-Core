/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.players;

import com.mojang.authlib.properties.Property;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.utils.disguise.DisguiseUtil;
import net.auroramc.core.api.utils.disguise.Skin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class Disguise {

    private static final String[] ADJECTIVES = {"Elegant","Blue","Tidy","Forgetful","Awesome","Gentle","Kind","Flawless"};
    private static final String[] NOUNS = {"Wheat","Phone","Honey","Tractor","Apple","Bird","Bread","Cookie"};
    private static final String[] NAMES = {"Ethan","Brandon","Ellis","Paige","Ruth","Mary","Grace","Rob"};
    private static final String[] COLOURS = {"White","Black","Green","Blue","Purple","Pink","Orange","Yellow"};

    private final AuroraMCPlayer player;
    private String name;
    private UUID uuid;
    private String skinName;
    private String skin;
    private String signature;
    private Property originalTexture;
    private Rank rank;

    public Disguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        this.name = name;
        this.player = player;
        this.rank = rank;
        this.skin = skin;
        this.skinName = skin;

        for (Property property : ((CraftPlayer) player.getPlayer()).getProfile().getProperties().removeAll("textures")) {
            this.originalTexture = property;
            break;
        }

    }

    public Disguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        this.name = name;
        this.player = player;
        this.skin = skin;
        this.signature = signature;
        this.rank = rank;

        for (Property property : ((CraftPlayer) player.getPlayer()).getProfile().getProperties().removeAll("textures")) {
            this.originalTexture = property;
            break;
        }
    }

    public Disguise(String name, String skin, String signature, Rank rank) {
        this.name = name;
        this.player = null;
        this.skin = skin;
        this.signature = signature;
        this.rank = rank;

    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRank(Rank rank) {
        this.rank = rank;
    }

    public AuroraMCPlayer getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public void updateSkin(String skin) {
        this.skin = skin;
    }

    public void updateSkin(Skin skin) {
        this.skin = skin.getValue();
        this.signature = skin.getSignature();
    }

    public String getSkin() {
        return skin;
    }

    public String getSignature() {
        return signature;
    }

    public Property getOriginalTexture() {
        return originalTexture;
    }

    public boolean apply(boolean update) {
        if (skin == null) {
            if (name != null) {
                return DisguiseUtil.changeName(player.getPlayer(), name, update);
            }
            return true;
        } else {
            if (name != null) {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalTexture.getValue(), this.originalTexture.getSignature(), update, this.player, false);
                }
                if (skinName == null) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, skin, signature, update, this.player, false);
                }
                return DisguiseUtil.disguise(player.getPlayer(), name, skin, this, update, this.player);
            } else {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalTexture.getValue(), this.originalTexture.getSignature(), update, this.player, false);
                }
                if (skinName == null) {
                    return DisguiseUtil.changeSkin(player.getPlayer(), skin, signature, update, this.player, false);
                }
                DisguiseUtil.changeSkin(player.getPlayer(), skin, update, this, this.player);
                return true;
            }
        }
    }

    public boolean switchDisguise() {
        return DisguiseUtil.disguise(player.getPlayer(), player.getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), false, this.player, true);
    }

    public boolean undisguise() {
        return DisguiseUtil.disguise(player.getPlayer(), player.getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), true, this.player, true);
    }

    public static Disguise randomDisguise(AuroraMCPlayer player) {
        Random random = new Random();
        String name;
        switch (random.nextInt(5)) {
            case 1: {
                //[colour][noun][number]
                String colour = COLOURS[random.nextInt(COLOURS.length)];
                String noun = NOUNS[random.nextInt(NOUNS.length)];
                int number = random.nextInt(1000);

                while (colour.length() + noun.length() > 16) {
                    colour = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
                    noun = NOUNS[random.nextInt(NOUNS.length)];
                }
                name = colour + noun + number;

                if (name.length() > 16) {
                    name = colour + noun;
                }
                break;
            }
            case 2: {
                //The[name][number]
                String name1 = NAMES[random.nextInt(NAMES.length)];
                int number = random.nextInt(1000);

                while (name1.length() > 13) {
                    name1 = NAMES[random.nextInt(NAMES.length)];
                }

                if (random.nextBoolean()) {
                    name1 = name1.replace("i", "1");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("a", "4");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("e", "3");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("o", "0");
                }

                name = "The" + name1 + number;

                if (name.length() > 16) {
                    name = "The" + name1;
                }
                break;
            }
            case 3: {
                //[underscores][name][underscores]
                String name1 = NAMES[random.nextInt(NAMES.length)];
                int amountBefore = random.nextInt(4);
                int amountAfter = random.nextInt(4);

                while (name1.length() > 16 - amountBefore - amountAfter) {
                    name1 = NAMES[random.nextInt(NAMES.length)];
                }

                if (random.nextBoolean()) {
                    name1 = name1.replace("i", "1");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("a", "4");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("e", "3");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("o", "0");
                }

                StringBuilder builder = new StringBuilder();

                for (int i = 0;i<amountBefore;i++) {
                    builder.append("_");
                }

                builder.append(name1);

                for (int i = 0;i<amountAfter;i++) {
                    builder.append("_");
                }

                name = builder.toString();
                break;
            }
            case 4: {
                //[noun][name]
                String noun = NOUNS[random.nextInt(NOUNS.length)];
                String name1 = NAMES[random.nextInt(NAMES.length)];
                int number = random.nextInt(1000);

                while (name1.length() + noun.length() > 16) {
                    name1 = NAMES[random.nextInt(NAMES.length)];
                    noun = NOUNS[random.nextInt(NOUNS.length)];
                }

                if (random.nextBoolean()) {
                    name1 = name1.replace("i", "1");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("a", "4");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("e", "3");
                }
                if (random.nextBoolean()) {
                    name1 = name1.replace("o", "0");
                }

                name = name1 + noun + number;

                if (name.length() > 16) {
                    name = name1 + noun;
                }
                break;
            }
            default: {
                String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
                String noun = NOUNS[random.nextInt(NOUNS.length)];
                int number = random.nextInt(1000);

                while (adjective.length() + noun.length() > 16) {
                    adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
                    noun = NOUNS[random.nextInt(NOUNS.length)];
                }
                name = adjective + noun + number;

                if (name.length() > 16) {
                    name = adjective + noun;
                }
                break;
            }
        }

        UUID uuid = AuroraMCAPI.getDbManager().getUUID(name);
        if (uuid != null) {
            Rank rank = AuroraMCAPI.getDbManager().getRank(uuid);
            if (rank != null) {
                if (rank.getCategory() != Rank.RankCategory.PLAYER) {
                    return randomDisguise(player);
                }
            }
            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                return randomDisguise(player);
            }
        }

        if (AuroraMCAPI.getDbManager().isUsernameBanned(name)) {
            return randomDisguise(player);
        }
        if (AuroraMCAPI.getFilter() == null) {
            return randomDisguise(player);
        }
        if (!AuroraMCAPI.getFilter().filter(name).equals(name)) {
            return randomDisguise(player);
        }

        if (AuroraMCAPI.getDbManager().isAlreadyDisguise(name)) {
            return randomDisguise(player);
        }
        String[] skin = AuroraMCAPI.getDbManager().getRandomSkin().split(";");

        String skinTotal = "{\n" +
                "  \"timestamp\" : " + System.currentTimeMillis() + ",\n" +
                "  \"profileId\" : \"" + player.getPlayer().getUniqueId().toString().replace("-","") + "\",\n" +
                "  \"profileName\" : \"" + name + "\",\n" +
                "  \"textures\" : {\n" +
                "    \"SKIN\" : {\n" +
                "      \"url\" : \"" + skin[0] + "\"" +
                ((skin.length > 1 && skin[1].equalsIgnoreCase("slim"))?
                        ",\n" +
                "      \"metadata\" : {\n" +
                "        \"model\" : \"slim\"\n" +
                "      }":"") + "\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Rank rank = Rank.getByID(random.nextInt(3));
        return new Disguise(player, name, new String(Base64.getEncoder().encode(skinTotal.getBytes())), null, rank);
    }
}
