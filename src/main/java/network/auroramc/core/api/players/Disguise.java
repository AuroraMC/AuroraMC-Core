package network.auroramc.core.api.players;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.utils.DisguiseUtil;
import network.auroramc.core.api.utils.Skin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Disguise {

    private AuroraMCPlayer player;
    private String name;
    private UUID uuid;
    private String skin;
    private String signature;
    private Property originalTexture;
    private Rank rank;

    public Disguise(AuroraMCPlayer player, String name, String uuid, Rank rank) {
        this.name = name;
        this.player = player;
        this.rank = rank;
        this.uuid = UUID.fromString(uuid);

        for (Property property : ((CraftPlayer) player.getPlayer()).getProfile().getProperties().removeAll("textures")) {
            this.originalTexture = property;
            break;
        }

        Skin skin1 = DisguiseUtil.getSkin(UUID.fromString(uuid));
        if (skin1 == null) {
            return;
        }
        skin = skin1.getValue();
        signature = skin1.getSignature();

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
                return DisguiseUtil.disguise(player.getPlayer(), name, skin);
            } else {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalTexture.getValue(), this.originalTexture.getSignature());
                }
                if (signature != null) {
                    return DisguiseUtil.changeSkin(player.getPlayer(), skin, signature, true);
                }
                return DisguiseUtil.changeSkin(player.getPlayer(), skin, true);
            }
        }
    }

    public boolean undisguise() {
        return DisguiseUtil.disguise(player.getPlayer(), player.getName(), this.originalTexture.getValue(), this.originalTexture.getSignature());
    }
}
