package network.auroramc.core.api.players;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.utils.DisguiseUtil;

public class Disguise {

    private AuroraMCPlayer player;
    private String name;
    private String skin;
    private String originalSkin;
    private String originalSignature;
    private Rank rank;

    public Disguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        this.name = name;
        this.player = player;
        this.skin = skin;
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

    public String getSkin() {
        return skin;
    }

    public boolean apply() {
        if (skin == null) {
            if (name != null) {
                return DisguiseUtil.changeName(player.getPlayer(), name, true, AuroraMC.get());
            }
            return true;
        } else {
            if (name != null) {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalSkin, this.originalSignature, AuroraMC.get());
                }
                return DisguiseUtil.disguise(player.getPlayer(), name, skin, AuroraMC.get());
            } else {
                if (skin.equals(player.getName())) {
                    return DisguiseUtil.disguise(player.getPlayer(), name, this.originalSkin, this.originalSignature, AuroraMC.get());
                }
                return DisguiseUtil.changeSkin(player.getPlayer(), skin, true, AuroraMC.get());
            }
        }
    }

    public boolean undisguise() {
        return DisguiseUtil.disguise(player.getPlayer(), player.getName(), originalSkin, originalSignature, AuroraMC.get());
    }
}
