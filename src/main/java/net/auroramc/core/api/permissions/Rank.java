package net.auroramc.core.api.permissions;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Rank {

    PLAYER(0, "Player", null, null, null, null, 'f', '3', 'f', false, Collections.emptyList(), Collections.singletonList(Permission.PLAYER), RankCategory.PLAYER, Color.fromRGB(255, 255, 255)),
    ELITE(1, "Elite", "Elite", "&b«ELITE»\n \n" +
            "&fThis rank is very rare, and only\n" +
            "&fthe most daring of players will\n" +
            "&fventure into its unknown value!\n\n", "http://store.auroramc.net/", 'b', 'f', 'b', 'f', false, Collections.singletonList(PLAYER), Collections.singletonList(Permission.ELITE), RankCategory.PLAYER, Color.fromRGB(85, 255, 255)),
    MASTER(2, "Master", "Master", "&d«MASTER»\n \n" +
            "&fThis rank is uncommon, and is\n" +
            "&fonly found in the Realm of the\n" +
            "Unknown! It's value is incomparable!\n\n", "http://store.auroramc.net/", 'd', 'f', 'd', 'f', false, Collections.singletonList(ELITE), Collections.singletonList(Permission.MASTER), RankCategory.PLAYER, Color.fromRGB(255, 85, 255)),
    YOUTUBE(5, "YouTube", "YouTube", "&6«YOUTUBE»\n\n" +
            "&fThis rank is given to YouTube\n" +
            "&fcontent creators on AuroraMC.\n \n" +
            "&aClick to view rank requirements.", "https://auroramc.net/threads/content-creator-ranks-information.49/", '6', 'f', 'e', 'f', false, Collections.singletonList(MASTER), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL), Rank.RankCategory.SOCIAL_MEDIA, Color.fromRGB(255, 170, 0)),
    TWITCH(6, "Twitch", "Twitch", "&5«TWITCH»\n\n" +
            "&fThis rank is given to Twitch\n" +
            "&fstreamers on AuroraMC.\n \n" +
            "&aClick to view rank requirements.", "https://auroramc.net/threads/content-creator-ranks-information.49/", '5', 'f', 'd', 'f', false, Collections.singletonList(MASTER), Arrays.asList(Permission.DISGUISE, Permission.SOCIAL), RankCategory.SOCIAL_MEDIA, Color.fromRGB(170, 0, 170)),
    BUILDER(7, "Builder", "Builder", "&a«BUILDER»\n \n" +
            "&fBuilders create and fix all of the\n" +
            "&fmaps you can find on the network!", null, 'a', 'f', 'a', 'f', false, Collections.singletonList(MASTER), Collections.singletonList(Permission.BUILD), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85)),
    BUILD_TEAM_MANAGEMENT(8, "Build Team Management", "BTM", "&a«BTM»\n \n" +
            "&fBTM members manage and maintain the\n" +
            "&fbuilders, build server and maps!", null, 'a', 'f', 'a', 'f', false, Collections.singletonList(BUILDER), Collections.singletonList(Permission.BUILD_TEAM_MANAGEMENT), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85)),
    JUNIOR_MODERATOR(9, "Junior Moderator", "Jr.Mod", "&9«JUNIOR MOD»\n \n" +
            "&fJunior Mods answer any questions or\n" +
            "&fqueries players have, as well as\n" +
            "&fmoderate the network. Junior Mods have\n" +
            "&fless permissions than Moderators.", null, '9', 'f', '9', 'f', false, Collections.singletonList(MASTER), Collections.singletonList(Permission.MODERATION), RankCategory.MODERATION, Color.fromRGB(85, 85, 255)),
    MODERATOR(10, "Moderator", "Mod", "&9«MOD»\n \n" +
            "&rMods answer any questions or\n" +
            "&fqueries players have, as well as\n" +
            "&fmoderate the network. ", null, '9', 'f', '9', 'f', false, Collections.singletonList(JUNIOR_MODERATOR), Collections.singletonList(Permission.BYPASS_APPROVAL), RankCategory.MODERATION, Color.fromRGB(85, 85, 255)),
    ADMIN(11, "Administrator", "Admin", "&c«ADMIN»\n \n" +
            "&fAdmins monitor their specific teams,\n" +
            "&fmaking sure all of the staff inside those\n" +
            "&fteams are working efficiently and to the\n" +
            "&fbest of their ability.", null, 'c', 'f', 'c', 'f', true, Collections.singletonList(MODERATOR), Arrays.asList(Permission.ADMIN, Permission.DISGUISE, Permission.SOCIAL, Permission.DEBUG_INFO), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0)),
    DEVELOPER(12, "Developer", "Dev", "&a«DEV»\n \n" +
            "&fDevelopers create the content that\n" +
            "&fyou see on all our servers! They work\n" +
            "&fbehind the scenes coding the games you\n" +
            "&flove to play!", null, 'a', 'f', 'a', 'f', true, Collections.singletonList(MASTER), Collections.singletonList(Permission.DEBUG_INFO), RankCategory.CONTENT_CREATOR, Color.fromRGB(85, 255, 85)),
    OWNER(9001, "Owner", "Owner", "&c«OWNER»\n \n" +
            "&fOwners manage all aspects of\n" +
            "&fthe network, keeping an eye on\n" +
            "&fthe staff team and managing all\n" +
            "&fcontent that is published.", null, 'c', 'f', 'c', 'f', true, Collections.singletonList(ADMIN), Collections.singletonList(Permission.ALL), RankCategory.LEADERSHIP, Color.fromRGB(170, 0, 0));

    public enum RankCategory {PLAYER, SOCIAL_MEDIA, CONTENT_CREATOR, MODERATION, LEADERSHIP}

    private final int id;
    private final String name;
    private final String prefixAppearance;
    private final String prefixHoverText;
    private final String prefixHoverURL;
    private final Character prefixColor;
    private final char nameColor;
    private final char connectorColor;
    private final char defaultChatColor;
    private final boolean canUseColorCodes;
    private final List<Permission> permissions;
    private final List<Rank> inheritance;
    private final RankCategory category;
    private final Color color;

    Rank(@NotNull int id, @NotNull String name, @Nullable String prefixAppearance, @Nullable String prefixHoverText, @Nullable String prefixHoverURL, @Nullable Character prefixColor, @NotNull char nameColor, @NotNull char connectorColor, @NotNull char defaultChatColor, @NotNull boolean canUseColorCodes, @NotNull List<Rank> inherit, @NotNull List<Permission> permissions, @NotNull RankCategory category, @NotNull Color guiColor) {
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
        this.color = guiColor;
    }

    public final String getName() {
        return name;
    }

    public final boolean canUseColorCodes() {
        return canUseColorCodes;
    }

    public final char getConnectorColor() {
        return connectorColor;
    }

    public final char getDefaultChatColor() {
        return defaultChatColor;
    }

    public final char getNameColor() {
        return nameColor;
    }

    public final Character getPrefixColor() {
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

    public final String getPrefixHoverText() {
        return prefixHoverText;
    }

    public final String getPrefixHoverURL() {
        return prefixHoverURL;
    }

    public final RankCategory getCategory() {
        return category;
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

    public Color getColor() {
        return color;
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

