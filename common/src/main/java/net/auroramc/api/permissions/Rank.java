/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.api.permissions;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Rank {

    PLAYER(0, "Player", null, null, null, null, ChatColor.WHITE, ChatColor.DARK_AQUA, ChatColor.WHITE, false, Collections.emptyList(), Collections.singletonList(Permission.PLAYER), RankCategory.PLAYER, 255, 255, 255),
    ELITE(1, "Elite", "Elite", new TextComponent("§b§l«ELITE»\n \n" +
            "§fThis rank is very rare, and only\n" +
            "§fthe most daring of players will\n" +
            "§fventure into its unknown value!\n\n"), "http://store.auroramc.net/", ChatColor.AQUA, ChatColor.WHITE, ChatColor.AQUA, ChatColor.WHITE, false, Collections.singletonList(PLAYER), Collections.singletonList(Permission.ELITE), RankCategory.PLAYER, 85, 255, 255),
    MASTER(2, "Master", "Master", new TextComponent("§d§l«MASTER»\n \n" +
            "§fThis rank is uncommon, and is\n" +
            "§fonly found in the Realm of the\n" +
            "Unknown! It's value is incomparable!\n\n"), "http://store.auroramc.net/", ChatColor.LIGHT_PURPLE, ChatColor.WHITE, ChatColor.LIGHT_PURPLE, ChatColor.WHITE, false, Collections.singletonList(ELITE), Collections.singletonList(Permission.MASTER), RankCategory.PLAYER, 255, 85, 255),
    YOUTUBE(5, "YouTube", "YouTube", new TextComponent("§6§l«YOUTUBE»\n\n" +
            "§fThis rank is given to YouTube\n" +
            "§fcontent creators on AuroraMC.\n \n" +
            "§aClick to view rank requirements."), "https://auroramc.net/threads/content-creator-ranks-information.49/", ChatColor.GOLD, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.WHITE, false, Collections.singletonList(MASTER), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL, Permission.CUSTOM_DISGUISE), Rank.RankCategory.SOCIAL_MEDIA, 255, 170, 0),
    STREAMER(6, "Streamer", "Stream", new TextComponent("§5§l«STREAMER»\n\n" +
            "§fThis rank is given to Twitch\n" +
            "§fstreamers on AuroraMC.\n \n" +
            "§aClick to view rank requirements."), "https://auroramc.net/threads/content-creator-ranks-information.49/", ChatColor.DARK_PURPLE, ChatColor.WHITE, ChatColor.LIGHT_PURPLE, ChatColor.WHITE, false, Collections.singletonList(MASTER), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL, Permission.CUSTOM_DISGUISE), RankCategory.SOCIAL_MEDIA, 170, 0, 170),
    BUILDER(7, "Builder", "Builder", new TextComponent("§a§l«BUILDER»\n \n" +
            "§fBuilders create and fix all of the\n" +
            "§fmaps you can find on the network!"), null, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GREEN, ChatColor.WHITE, false, Collections.singletonList(MASTER), Collections.singletonList(Permission.BUILD), RankCategory.CONTENT_CREATOR, 85, 255, 85),
    BUILD_TEAM_MANAGEMENT(8, "Build Team Management", "BTM", new TextComponent("§a§l«BTM»\n \n" +
            "§fBTM members manage and maintain the\n" +
            "§fbuilders, build server and maps!"), null, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GREEN, ChatColor.WHITE, false, Collections.singletonList(BUILDER), Collections.singletonList(Permission.BUILD_TEAM_MANAGEMENT), RankCategory.CONTENT_CREATOR, 85, 255, 85),
    JUNIOR_MODERATOR(9, "Junior Moderator", "Jr.Mod", new TextComponent("§9«JUNIOR MOD»\n \n" +
            "§fJunior Mods answer any questions or\n" +
            "§fqueries players have, as well as\n" +
            "§fmoderate the network. Junior Mods have\n" +
            "§fless permissions than Moderators."), null, ChatColor.BLUE, ChatColor.WHITE, ChatColor.BLUE, ChatColor.WHITE, false, Collections.singletonList(MASTER), Collections.singletonList(Permission.MODERATION), RankCategory.MODERATION, 85, 85, 255),
    MODERATOR(10, "Moderator", "Mod", new TextComponent("§9§l«MOD»\n \n" +
            "§rMods answer any questions or\n" +
            "§fqueries players have, as well as\n" +
            "§fmoderate the network. "), null, ChatColor.BLUE, ChatColor.WHITE, ChatColor.BLUE, ChatColor.WHITE, false, Collections.singletonList(JUNIOR_MODERATOR), Arrays.asList(Permission.BYPASS_APPROVAL, Permission.DISGUISE), RankCategory.MODERATION, 85, 85, 255),
    ROBOT(8001, "Robot", "Robot", new TextComponent("§4§l«ROBOT»\n \n" +
            "§fThis account is a bot account which is\n" +
            "§fused and managed by the AuroraMC\n" +
            "§fManagement Team for miscellaneous\n" +
            "§ftasks and duties."), null, ChatColor.DARK_RED, ChatColor.WHITE, ChatColor.RED, ChatColor.WHITE, false, Collections.singletonList(MASTER), Arrays.asList(Permission.MODERATION, Permission.BUILD), RankCategory.MODERATION, 85, 85, 255),
    ADMIN(11, "Administrator", "Admin", new TextComponent("§4§l«ADMIN»\n \n" +
            "§fAdmins monitor their specific teams,\n" +
            "§fmaking sure all of the staff inside those\n" +
            "§fteams are working efficiently and to the\n" +
            "§fbest of their ability."), null, ChatColor.DARK_RED, ChatColor.WHITE, ChatColor.RED, ChatColor.WHITE, true, Collections.singletonList(MODERATOR), Arrays.asList(Permission.ADMIN, Permission.DISGUISE, Permission.CUSTOM_DISGUISE, Permission.SOCIAL, Permission.DEBUG_INFO, Permission.PANEL, Permission.SOCIAL_MEDIA, Permission.STAFF_MANAGEMENT, Permission.RECRUITMENT, Permission.DEBUG_ACTION), RankCategory.LEADERSHIP, 170, 0, 0),
    DEVELOPER(12, "Developer", "Dev", new TextComponent("§a«DEV»\n \n" +
            "§fDevelopers create the content that\n" +
            "§fyou see on all our servers! They work\n" +
            "§fbehind the scenes coding the games you\n" +
            "§flove to play!"), null, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GREEN, ChatColor.WHITE, true, Collections.singletonList(MASTER), Arrays.asList(Permission.DEBUG_INFO, Permission.PANEL), RankCategory.CONTENT_CREATOR, 85, 255, 85),
    SENIOR_DEVELOPER(13, "Senior Developer", "Sr.Dev", new TextComponent("§4§l«SR.DEV»\n \n" +
            "§fSenior Developers oversee the development\n" +
            "§fand everyday maintenance of the network.\n" +
            "§fThey help give out developer assignments\n" +
            "§fand help our design team plan upcoming\n" +
            "§fupdates!"), null, ChatColor.DARK_RED, ChatColor.WHITE, ChatColor.RED, ChatColor.WHITE, true, Arrays.asList(DEVELOPER, JUNIOR_MODERATOR), Arrays.asList(Permission.ADMIN, Permission.DEBUG_ACTION, Permission.PANEL, Permission.DISGUISE, Permission.CUSTOM_DISGUISE), RankCategory.LEADERSHIP, 170, 0, 0),
    PARTNER(14, "Partner", "Partner", new TextComponent("§c§l«PARTNER»\n\n" +
            "§fThis is given to content creators\n" +
            "that are a part of the AuroraMC Affiliate\n" +
            "Program! They have special creator codes\n" +
            "that you can use for a small discount on\n" +
            "our store!\n \n" +
            "§fThis rank is invite only!"), null, ChatColor.RED, ChatColor.WHITE, ChatColor.RED, ChatColor.WHITE, false, Collections.singletonList(MASTER), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL, Permission.CUSTOM_DISGUISE), RankCategory.SOCIAL_MEDIA, 255, 170, 170),
    OWNER(9001, "Owner", "Owner", new TextComponent("§4§l«OWNER»\n \n" +
            "§fOwners manage all aspects of\n" +
            "§fthe network, keeping an eye on\n" +
            "§fthe staff team and managing all\n" +
            "§fcontent that is published."), null, ChatColor.DARK_RED, ChatColor.WHITE, ChatColor.RED, ChatColor.WHITE, true, Collections.singletonList(ADMIN), Collections.singletonList(Permission.ALL), RankCategory.LEADERSHIP, 170, 0, 0);

    public enum RankCategory {PLAYER, SOCIAL_MEDIA, CONTENT_CREATOR, MODERATION, LEADERSHIP}

    private final int id;
    private final String name;
    private final String prefixAppearance;
    private final BaseComponent prefixHoverText;
    private final String prefixHoverURL;
    private final ChatColor prefixColor;
    private final ChatColor nameColor;
    private final ChatColor connectorColor;
    private final ChatColor defaultChatColor;
    private final boolean canUseColorCodes;
    private final List<Permission> permissions;
    private final List<Rank> inheritance;
    private final RankCategory category;
    private final int r;
    private final int g;
    private final int b;

    Rank(@NotNull int id, @NotNull String name, @Nullable String prefixAppearance, @Nullable BaseComponent prefixHoverText, @Nullable String prefixHoverURL, @Nullable ChatColor prefixColor, @NotNull ChatColor nameColor, @NotNull ChatColor connectorColor, @NotNull ChatColor defaultChatColor, @NotNull boolean canUseColorCodes, @NotNull List<Rank> inherit, @NotNull List<Permission> permissions, @NotNull RankCategory category, int r, int g, int b) {
        this.id = id;
        this.name = name;
        this.prefixAppearance = prefixAppearance;
        this.prefixHoverText = prefixHoverText;
        this.prefixHoverURL = prefixHoverURL;
        this.prefixColor = prefixColor;
        this.nameColor = nameColor;
        this.connectorColor = connectorColor;
        this.defaultChatColor = defaultChatColor;
        this.canUseColorCodes = canUseColorCodes;
        this.permissions = permissions;
        this.inheritance = inherit;
        this.category = category;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public final String getName() {
        return name;
    }

    public final boolean canUseColorCodes() {
        return canUseColorCodes;
    }

    public final ChatColor getConnectorColor() {
        return connectorColor;
    }

    public final ChatColor getDefaultChatColor() {
        return defaultChatColor;
    }

    public final ChatColor getNameColor() {
        return nameColor;
    }

    public final ChatColor getPrefixColor() {
        return prefixColor;
    }

    public final int getId() {
        return id;
    }

    public final List<Permission> getPermissions() {
        return permissions;
    }

    public final String getPrefixAppearance() {
        return prefixAppearance;
    }

    public final BaseComponent getPrefixHoverText() {
        return prefixHoverText;
    }

    public final String getPrefixHoverURL() {
        return prefixHoverURL;
    }

    public final RankCategory getCategory() {
        return category;
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

    public final boolean hasPermission(String node) {
        for (Permission permission : getPermissions()) {
            if (permission.getNode().equals(node) || permission.getNode().equals("all")) {
                return true;
            }
        }

        for (Rank rank : getInheritance()) {
            if (rank.hasPermission(node)) {
                return true;
            }
        }
        return false;
    }

    public final List<Rank> getInheritance() {
        return inheritance;
    }

    public final boolean hasPermission(int id) {
        for (Permission permission : getPermissions()) {
            if (permission.getId() == id || permission.getId() == -1) {
                return true;
            }
        }

        for (Rank rank : getInheritance()) {
            if (rank.hasPermission(id)) {
                return true;
            }
        }
        return false;
    }

    public static Rank getByID(int id) {
        for (Rank rank : Rank.values()){
            if (rank.getId() == id) {
                return rank;
            }
        }

        return null;
    }

    /**
     * Checks whether the given rank is a parent of this rank.
     * @param rank The parent you want to check.
     * @return Whether rank is a parent or not.
     */
    public boolean isParent(Rank rank) {
        if (rank.equals(this)) {
            return true;
        }

        for (Rank rank2 : inheritance) {
            if (rank2.isParent(rank)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the given rank is a child of this rank.
     * @param rank The child you want to check.
     * @return Whether rank is a child or not.
     */
    public boolean isChild(Rank rank) {
        if (rank.equals(this)) {
            return true;
        }

        for (Rank rank2 : rank.inheritance) {
            if (isChild(rank2)) {
                return true;
            }
        }

        return false;
    }
}

