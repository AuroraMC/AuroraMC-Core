/*
 * Copyright (c) 2023-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.abstraction.AbstractedMethods;
import net.auroramc.api.cosmetics.*;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.player.AuroraMCPlayer;
import net.auroramc.api.player.Disguise;
import net.auroramc.common.cosmetics.crates.DiamondCrate;
import net.auroramc.common.cosmetics.crates.EmeraldCrate;
import net.auroramc.common.cosmetics.crates.GoldCrate;
import net.auroramc.common.cosmetics.crates.IronCrate;
import net.auroramc.proxy.api.player.ProxyDisguise;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;

public class ProxyAbstractedMethods extends AbstractedMethods {


    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, Rank rank) {
        return new ProxyDisguise(player, name, skin, rank);
    }

    @Override
    public Disguise newDisguise(AuroraMCPlayer player, String name, String skin, String signature, Rank rank) {
        return new ProxyDisguise(player, name, skin, signature, rank);
    }

    @Override
    public Disguise newDisguise(String name, String skin, String signature, Rank rank) {
        return new ProxyDisguise(name, skin, signature, rank);
    }

    @Override
    public void scheduleAsyncTask(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), runnable);
    }

    @Override
    public Object scheduleAsyncTaskLater(Runnable runnable, long delay) {
        return ProxyServer.getInstance().getScheduler().schedule(ProxyAPI.getCore(), runnable, delay*50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void scheduleSyncTask(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ProxyAPI.getCore(), runnable);
    }

    @Override
    public Crate generateIronCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new IronCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateGoldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new GoldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateDiamondCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new DiamondCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public Crate generateEmeraldCrate(UUID uuid, int owner, Crate.CrateReward loot, long generated, long opened) {
        return new EmeraldCrate(uuid, owner, loot, generated, opened);
    }

    @Override
    public void onEquipBanner(AuroraMCPlayer player, Banner banner) {
    }

    @Override
    public void onUnequipBanner(AuroraMCPlayer player, Banner banner) {
    }

    @Override
    public void onEquipHat(AuroraMCPlayer player, Hat hat) {
    }

    @Override
    public void onUnequipHat(AuroraMCPlayer player, Hat hat) {
    }

    @Override
    public void onEquipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
    }

    @Override
    public void onUnequipSymbol(AuroraMCPlayer player, PlusSymbol symbol) {
    }

    @Override
    public void onEquipGadget(AuroraMCPlayer player, Gadget gadget) {
    }

    @Override
    public void onUnequipGadget(AuroraMCPlayer player, Gadget gadget) {
    }

    @Override
    public void broadcastModerationMessage(BaseComponent message, AuroraMCPlayer issuer) {
    }

    @Override
    public void broadcastNovaMessage(BaseComponent message) {
    }

    @Override
    public void firePreferenceEvent(AuroraMCPlayer player) {}

    @Override
    public JSONArray getPluginData() {
        JSONArray array = new JSONArray();
        String buildNumber;
        String gitCommit;
        String branch;
        try {
            Enumeration<URL> resources = ProxyAPI.getCore().getClass().getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                // check that this is your manifest and do what you need or get the next one
                Attributes attributes = manifest.getMainAttributes();

                buildNumber = attributes.getValue("Jenkins-Build-Number");
                gitCommit = attributes.getValue("Git-Commit");
                branch = attributes.getValue("Branch");
                if (buildNumber == null || gitCommit == null) {
                    continue;
                }
                JSONObject object = new JSONObject();
                object.put("name", attributes.getValue("Module-Name"));
                object.put("build", buildNumber);
                object.put("commit", gitCommit);
                object.put("branch", (branch == null || branch.equals("null")?"master":branch));
                array.put(object);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }
}
