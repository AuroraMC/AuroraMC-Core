/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.commands.admin.*;
import net.auroramc.core.commands.admin.cosmetic.CommandCosmetic;
import net.auroramc.core.commands.admin.debug.CommandKillMessageTest;
import net.auroramc.core.commands.admin.iplookup.CommandIPLookup;
import net.auroramc.core.commands.general.*;
import net.auroramc.core.commands.general.chest.CommandChest;
import net.auroramc.core.commands.general.ignore.CommandIgnore;
import net.auroramc.core.commands.general.team.CommandTeam;
import net.auroramc.core.commands.moderation.*;
import net.auroramc.core.commands.moderation.qualityassurance.CommandAppeal;
import net.auroramc.core.commands.moderation.report.CommandReportClose;
import net.auroramc.core.commands.moderation.report.CommandReportHandle;
import net.auroramc.core.commands.moderation.report.CommandReportInfo;
import net.auroramc.core.commands.moderation.staffmanagement.CommandRecruitmentLookup;
import net.auroramc.core.executors.deatheffects.ConfettiExecutor;
import net.auroramc.core.executors.deatheffects.FireworkExecutor;
import net.auroramc.core.executors.deatheffects.LayAnEggExecutor;
import net.auroramc.core.executors.gadgets.FireworkGadgetExecutor;
import net.auroramc.core.executors.gadgets.GrapplingHookExecutor;
import net.auroramc.core.executors.particleeffects.BloodSwirlExecutor;
import net.auroramc.core.executors.particleeffects.EmeraldExecutor;
import net.auroramc.core.executors.particleeffects.PurpleExecutor;
import net.auroramc.core.executors.projectiletrails.EmeraldTrailExecutor;
import net.auroramc.core.executors.projectiletrails.FireworkTrailExecutor;
import net.auroramc.core.executors.projectiletrails.PurpleTrailExecutor;
import net.auroramc.core.executors.wineffects.ConfettiCannonExecutor;
import net.auroramc.core.executors.wineffects.EggsplosionExecutor;
import net.auroramc.core.executors.wineffects.FireworksWinExecutor;
import net.auroramc.core.executors.wineffects.TwerkApocalypseExecutor;
import net.auroramc.core.listeners.*;
import net.auroramc.core.managers.CommandManager;
import net.auroramc.core.managers.EventManager;
import net.auroramc.core.managers.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class AuroraMC extends JavaPlugin {


    private static FileConfiguration internal;
    private static File internalFile;


    @Override
    public void onEnable() {
        reloadConfig();
        getLogger().info("Loading AuroraMC-SMP...");

        ServerAPI.init(this);
        AuroraMCAPI.setCosmeticsEnabled(false);

        //Register Commands with the API.
        AuroraMCAPI.registerCommand(new CommandSetRank());
        AuroraMCAPI.registerCommand(new CommandLink());
        AuroraMCAPI.registerCommand(new CommandPunish());
        AuroraMCAPI.registerCommand(new CommandPunishHistory());
        AuroraMCAPI.registerCommand(new CommandEvidence());
        AuroraMCAPI.registerCommand(new CommandSM());
        AuroraMCAPI.registerCommand(new CommandDisguise());
        AuroraMCAPI.registerCommand(new CommandVanish());
        AuroraMCAPI.registerCommand(new CommandUndisguise());
        AuroraMCAPI.registerCommand(new CommandStaffChat());
        AuroraMCAPI.registerCommand(new CommandLag());
        AuroraMCAPI.registerCommand(new CommandAchievements());
        AuroraMCAPI.registerCommand(new CommandStats());
        AuroraMCAPI.registerCommand(new CommandBankServer());
        AuroraMCAPI.registerCommand(new CommandPlus());
        AuroraMCAPI.registerCommand(new CommandLevel());
        AuroraMCAPI.registerCommand(new CommandSymbol());
        AuroraMCAPI.registerCommand(new CommandPrefs());
        AuroraMCAPI.registerCommand(new CommandRecruitmentLookup());
        AuroraMCAPI.registerCommand(new CommandAppeal());
        AuroraMCAPI.registerCommand(new CommandPunishmentLookup());
        AuroraMCAPI.registerCommand(new CommandReport());
        AuroraMCAPI.registerCommand(new CommandReportHandle());
        AuroraMCAPI.registerCommand(new CommandReportClose());
        AuroraMCAPI.registerCommand(new CommandViewReports());
        AuroraMCAPI.registerCommand(new CommandIgnore());
        AuroraMCAPI.registerCommand(new CommandChatSlow());
        AuroraMCAPI.registerCommand(new CommandChatSilence());
        AuroraMCAPI.registerCommand(new CommandCosmetic());
        AuroraMCAPI.registerCommand(new CommandStaffMessage());
        AuroraMCAPI.registerCommand(new CommandStaffReply());
        AuroraMCAPI.registerCommand(new CommandNotes());
        AuroraMCAPI.registerCommand(new CommandHelp());
        AuroraMCAPI.registerCommand(new CommandKillMessageTest());
        AuroraMCAPI.registerCommand(new CommandIPLookup());
        AuroraMCAPI.registerCommand(new CommandPayments());
        AuroraMCAPI.registerCommand(new CommandAddRandomSkin());
        AuroraMCAPI.registerCommand(new CommandWhoIs());
        AuroraMCAPI.registerCommand(new CommandBuildVersions());
        AuroraMCAPI.registerCommand(new CommandRestart());
        AuroraMCAPI.registerCommand(new CommandToggleCosmetics());
        AuroraMCAPI.registerCommand(new CommandTestMode());
        AuroraMCAPI.registerCommand(new CommandCrateLookup());
        AuroraMCAPI.registerCommand(new CommandViewCrates());
        AuroraMCAPI.registerCommand(new CommandAmIDisguised());
        AuroraMCAPI.registerCommand(new CommandPlugins());
        AuroraMCAPI.registerCommand(new CommandEmotes());
        AuroraMCAPI.registerCommand(new CommandViewGames());
        AuroraMCAPI.registerCommand(new CommandReportInfo());
        AuroraMCAPI.registerCommand(new CommandHub());
        AuroraMCAPI.registerCommand(new CommandTeam());
        AuroraMCAPI.registerCommand(new CommandChest());




        //Register Executors
        AuroraMCAPI.registerCosmeticExecutor(new ConfettiCannonExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new EggsplosionExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new FireworksWinExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new TwerkApocalypseExecutor());

        AuroraMCAPI.registerCosmeticExecutor(new EmeraldTrailExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new FireworkTrailExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new PurpleTrailExecutor());

        AuroraMCAPI.registerCosmeticExecutor(new BloodSwirlExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new EmeraldExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new PurpleExecutor());

        AuroraMCAPI.registerCosmeticExecutor(new FireworkGadgetExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new GrapplingHookExecutor());

        AuroraMCAPI.registerCosmeticExecutor(new LayAnEggExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new FireworkExecutor());
        AuroraMCAPI.registerCosmeticExecutor(new ConfettiExecutor());

        AuroraMCAPI.loadRules();
        AuroraMCAPI.loadFilter();


        //Registering default Event Listeners
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandManager(), this);
        Bukkit.getPluginManager().registerEvents(new GUIManager(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProtocolMessageReceivedListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new NightSkipListener(), this);
        Bukkit.getPluginManager().registerEvents(new LockChestListener(), this);


        //Register the BungeeCord plugin message channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "auroramc:server", new PluginMessageRecievedListener());

        internalFile = new File(getDataFolder(), "internal.yml");
        if (!internalFile.exists()) {
            internalFile.getParentFile().mkdirs();
            copy(getResource("internal.yml"), internalFile);
        }

        internal = new YamlConfiguration();
        try {
            internal.load(internalFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        internal.options().copyHeader(true);


    }

    @Override
    public void onDisable() {
        if (!CommunicationUtils.isShutdown()) {
            if (!AuroraMCAPI.isShuttingDown()) {
                ProtocolMessage message = new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", "forced", AuroraMCAPI.getInfo().getName(), AuroraMCAPI.getInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            }
            CommunicationUtils.shutdown();
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getInternal() {
        return internal;
    }

    public static File getInternalFile() {
        return internalFile;
    }
}



