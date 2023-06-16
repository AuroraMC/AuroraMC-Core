/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.api.utils;

import com.mojang.authlib.properties.Property;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.Disguise;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.utils.DisguiseUtil;

public class ServerDisguise extends Disguise {

    private Property originalTexture;

    public ServerDisguise(AuroraMCServerPlayer player, String name, String skin, Rank rank) {
        super(player, name, skin, rank);
        for (Property property : player.getCraft().getProfile().getProperties().removeAll("textures")) {
            this.originalTexture = property;
            break;
        }
    }

    public ServerDisguise(AuroraMCServerPlayer player, String name, String skin, String signature, Rank rank) {
        super(player, name, skin, signature, rank);
        for (Property property : player.getCraft().getProfile().getProperties().removeAll("textures")) {
            this.originalTexture = property;
            break;
        }
    }

    public ServerDisguise(String name, String skin, String signature, Rank rank) {
        super(name, skin, signature, rank);
    }

    @Override
    public boolean apply(boolean update) {
        if (this.getSkin() == null) {
            if (getName() != null) {
                return DisguiseUtil.changeName((AuroraMCServerPlayer) this.getPlayer(), getName(), update);
            }
            return true;
        } else {
            if (getName() != null) {
                if (this.getSkin().equals(getPlayer().getName())) {
                    return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), update, false);
                }
                if (getSkinName() == null) {
                    return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getName(), getSkin(), getSignature(), update, false);
                }
                return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getName(), getSkin(), this, update);
            } else {
                if (this.getSkin().equals(getPlayer().getName())) {
                    return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), update, false);
                }
                if (getSkinName() == null) {
                    return DisguiseUtil.changeSkin((AuroraMCServerPlayer) this.getPlayer(), getSkin(), getSignature(), update, false);
                }
                DisguiseUtil.changeSkin((AuroraMCServerPlayer) this.getPlayer(), getSkin(), update, this);
                return true;
            }
        }
    }

    @Override
    public boolean switchDisguise() {
        return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getPlayer().getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), false, true);
    }

    @Override
    public boolean undisguise() {
        return DisguiseUtil.disguise((AuroraMCServerPlayer) this.getPlayer(), getPlayer().getName(), this.originalTexture.getValue(), this.originalTexture.getSignature(), true, true);
    }
}
