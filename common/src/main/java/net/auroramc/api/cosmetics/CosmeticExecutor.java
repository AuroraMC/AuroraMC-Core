/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.cosmetics;

import net.auroramc.api.player.AuroraMCPlayer;

public abstract class CosmeticExecutor {

    private Cosmetic cosmetic;

    public CosmeticExecutor(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }

    public abstract void execute(AuroraMCPlayer player);

    public abstract void execute(AuroraMCPlayer player, double x, double y, double z);

    public abstract void execute(Object entity);

    public abstract void cancel(AuroraMCPlayer player);
}
