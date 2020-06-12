package network.auroramc.core.api.players;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.utils.DisguiseUtil;
import network.auroramc.core.api.utils.Skin;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Disguise {

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
            Bukkit.getLogger().info("Original value: " + this.originalTexture.getValue());
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

    public boolean apply() {
        if (skin == null) {
            if (name != null) {
                return DisguiseUtil.changeName(player.getPlayer(), name, true);
            }
            return true;
        } else {
            if (name != null) {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalTexture.getValue(), this.originalTexture.getSignature());
                }
                if (signature != null) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, skin, signature);
                }
                return DisguiseUtil.disguise(player.getPlayer(), name, skin, this);
            } else {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalTexture.getValue(), this.originalTexture.getSignature());
                }
                if (signature != null) {
                    return DisguiseUtil.changeSkin(player.getPlayer(), skin, signature, true);
                }
                DisguiseUtil.changeSkin(player.getPlayer(), skin, true, this);
                return true;
            }
        }
    }

    public boolean undisguise() {
        return DisguiseUtil.disguise(player.getPlayer(), player.getName(), this.originalTexture.getValue(), this.originalTexture.getSignature());
    }
}
