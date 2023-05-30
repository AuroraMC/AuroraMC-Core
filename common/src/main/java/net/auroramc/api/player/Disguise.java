/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.player;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.DisguiseFactory;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.utils.disguise.Skin;

import java.util.Random;
import java.util.UUID;

public abstract class Disguise {

    private static final String[] ADJECTIVES = {"Elegant","Blue","Tidy","Forgetful","Awesome","Gentle","Kind","Flawless","Difficult","Amazing","Reliable","Sensible","Happy","Clever","Missing","Tiny","Little","Mighty","Cute","Feeble","Silly","Sweet","Horrible","Privilaged","Strange","Lucky","Cheerful","Precious","Great","Delicate","Strong","Handsome","Simple","Heavy","Useful"};
    private static final String[] NOUNS = {"Wheat","Phone","Honey","Tractor","Apple","Bird","Bread","Cookie","Mixer","Button","Donkey","Horse","Mouse","Candy","Avacado","Goose","Kitty","Cat","Dog","Doggy","Biscuit","Gravy","Potato","Console","Mole","Rabbit","Bun","Bunny","Plum","Chocolate","Apricot","Cheddar","Highway","Clothes","Freedom","Bird","Extend","Data","Amethyst","Speech","Angle","Bath","Chapter","Failure","Heart","News","Month","Lab","Salad","Library","Airport","Basis","Error","Null","Lizard","Basket"};
    private static final String[] NAMES = {"Ethan","Brandon","Ellis","Paige","Ruth","Mary","Grace","Rob","Phil","Claire","Luke","Haley","Alex","Cam","Jay","Gloria","Mitchell","Amy","Jake","Rosa","Adrian","Raymond","Kevin","Amy","Jonah","Dina","Garrett","Mateo","Cheyenne","Glenn","Sandra","Bo","Sal","Carol","Myrtle","Emma","Adam","Marcus","Elias","Jeff","Justine","Isaac","Jerry","Janet","Kelly","Jerusha","Laurie","Earl","Sayid","Terry","Charles","Gina","Michael","Norm","Mlepnos","Doug","Sharon","Vivian","Teddy","Madeline","Maddy","Sophia","Lynn","Marcus","Geoffrey","Mark","Jess","Jeff","Bob","Karen","Lily","Joe","Dylan","David","Fred","Rob","Barry","Andrea","Steve","Adam","Beatrice","Catherine"};
    private static final String[] COLOURS = {"White","Black","Green","Blue","Purple","Pink","Orange","Yellow","Turquoise","Violet","Amber","Red","Bronze","Silver","Gold","Magenta","Teal","Sapphire"};

    private AuroraMCPlayer player;
    private String name;
    private UUID uuid;
    private String skinName;
    private String skin;
    private String signature;
    private Rank rank;

    public Disguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        this.name = name;
        this.player = player;
        this.rank = rank;
        this.skin = skin;
        this.skinName = skin;

    }

    public Disguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        this.name = name;
        this.player = player;
        this.skin = skin;
        this.signature = signature;
        this.rank = rank;
        this.skinName = null;
    }

    public Disguise(String name, String skin, String signature, Rank rank) {
        this.name = name;
        this.player = null;
        this.skin = skin;
        this.signature = signature;
        this.rank = rank;
        this.skinName = null;
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

    public String getSkinName() {
        return skinName;
    }

    public abstract boolean apply(boolean update);

    public abstract boolean switchDisguise();

    public abstract boolean undisguise();

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

                while (amountAfter == 0 && amountBefore == 0) {
                    amountBefore = random.nextInt(4);
                    amountAfter = random.nextInt(4);
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

                if (random.nextBoolean()) {
                    name = noun + name1 + number;
                } else {
                    name = noun + name1;
                }

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

        Rank rank = Rank.getByID(random.nextInt(3));
        return DisguiseFactory.newDisguise(player, name, skin[0], skin[1], rank);
    }

    public void setPlayer(AuroraMCPlayer player) {
        this.player = player;
    }
}
