/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.executors.wineffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Random;

public class ConfettiCannonExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public ConfettiCannonExecutor() {
        super(AuroraMCAPI.getCosmetics().get(603));
    }

    @Override
    public void execute(AuroraMCPlayer player) {

    }

    @Override
    public void execute(AuroraMCPlayer player, double x, double y, double z) {
    }

    @Override
    public void execute(Object entity) {
    }

    @Override
    public void cancel(AuroraMCPlayer player) {

    }
}
