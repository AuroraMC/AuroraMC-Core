/*
 * Copyright (c) 2023-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.smp.executors.deatheffects;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.CosmeticExecutor;
import net.auroramc.api.player.AuroraMCPlayer;

import java.util.Random;

public class ConfettiExecutor extends CosmeticExecutor {

    private final Random random = new Random();

    public ConfettiExecutor() {
        super(AuroraMCAPI.getCosmetics().get(702));
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
