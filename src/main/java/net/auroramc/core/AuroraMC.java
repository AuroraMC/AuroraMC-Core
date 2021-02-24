package net.auroramc.core;

import net.auroramc.core.achievements.experience.*;
import net.auroramc.core.achievements.friends.*;
import net.auroramc.core.achievements.general.*;
import net.auroramc.core.achievements.loyalty.HappyBirthday;
import net.auroramc.core.achievements.party.*;
import net.auroramc.core.achievements.time.*;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.commands.admin.*;
import net.auroramc.core.commands.general.*;
import net.auroramc.core.commands.general.ignore.CommandIgnore;
import net.auroramc.core.commands.moderation.*;
import net.auroramc.core.commands.moderation.qualityassurance.CommandAppeal;
import net.auroramc.core.commands.moderation.report.CommandReportClose;
import net.auroramc.core.commands.moderation.report.CommandReportHandle;
import net.auroramc.core.commands.moderation.staffmanagement.CommandRecruitmentLookup;
import net.auroramc.core.listeners.ChatListener;
import net.auroramc.core.listeners.JoinListener;
import net.auroramc.core.listeners.LeaveListener;
import net.auroramc.core.listeners.PluginMessageRecievedListener;
import net.auroramc.core.managers.CommandManager;
import net.auroramc.core.managers.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AuroraMC extends JavaPlugin {


    @Override
    public void onEnable() {
        reloadConfig();
        getLogger().info("Loading AuroraMC-Core...");

        AuroraMCAPI.init(this);


        AuroraMCAPI.loadRules();
        AuroraMCAPI.loadFilter();
        //Register Commands with the API.
        AuroraMCAPI.registerCommand(new CommandSetRank());
        AuroraMCAPI.registerCommand(new CommandLink());
        AuroraMCAPI.registerCommand(new CommandPunish());
        AuroraMCAPI.registerCommand(new CommandPunishHistory());
        AuroraMCAPI.registerCommand(new CommandEvidence());
        AuroraMCAPI.registerCommand(new CommandSM());
        AuroraMCAPI.registerCommand(new CommandDisguise());
        AuroraMCAPI.registerCommand(new CommandVanish());
        AuroraMCAPI.registerCommand(new CommandLookup());
        AuroraMCAPI.registerCommand(new CommandUndisguise());
        AuroraMCAPI.registerCommand(new CommandStaffChat());
        AuroraMCAPI.registerCommand(new CommandLag());
        AuroraMCAPI.registerCommand(new CommandAchievements());
        AuroraMCAPI.registerCommand(new CommandStats());
        AuroraMCAPI.registerCommand(new CommandBankServer());
        AuroraMCAPI.registerCommand(new CommandPlus());
        AuroraMCAPI.registerCommand(new CommandLevel());
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

        //Register achievements with the API
        AuroraMCAPI.registerAchievement(new Welcome());
        AuroraMCAPI.registerAchievement(new OG());
        AuroraMCAPI.registerAchievement(new net.auroramc.core.achievements.general.Elite());
        AuroraMCAPI.registerAchievement(new net.auroramc.core.achievements.general.Master());
        AuroraMCAPI.registerAchievement(new GettingAnUpgrade());
        AuroraMCAPI.registerAchievement(new HeyStopThat());
        AuroraMCAPI.registerAchievement(new ChatterBox());
        AuroraMCAPI.registerAchievement(new WhereYaGoinBuddy());
        AuroraMCAPI.registerAchievement(new GoodGame());
        AuroraMCAPI.registerAchievement(new GettingTheHangOfThis());
        AuroraMCAPI.registerAchievement(new Helper());
        AuroraMCAPI.registerAchievement(new HubExplorer());
        AuroraMCAPI.registerAchievement(new Toxic());
        AuroraMCAPI.registerAchievement(new LetsGoShopping());
        AuroraMCAPI.registerAchievement(new ModModMod());
        AuroraMCAPI.registerAchievement(new Wumpus());
        AuroraMCAPI.registerAchievement(new BoredNow());
        AuroraMCAPI.registerAchievement(new AmIFamousYet());
        AuroraMCAPI.registerAchievement(new BasicallyStaff());
        AuroraMCAPI.registerAchievement(new JumperMcJumperson());
        AuroraMCAPI.registerAchievement(new Welp());
        AuroraMCAPI.registerAchievement(new WinnerWinnerChickenDinner());
        AuroraMCAPI.registerAchievement(new Murderer());
        AuroraMCAPI.registerAchievement(new BadConnection());
        AuroraMCAPI.registerAchievement(new Mod());
        AuroraMCAPI.registerAchievement(new Builderererrrrr());
        AuroraMCAPI.registerAchievement(new FollowingTheLeader());
        AuroraMCAPI.registerAchievement(new UmWhatNow());
        AuroraMCAPI.registerAchievement(new Hackusator());
        AuroraMCAPI.registerAchievement(new OooohFancy());
        AuroraMCAPI.registerAchievement(new NotALoner());
        AuroraMCAPI.registerAchievement(new ThankYouNext());
        AuroraMCAPI.registerAchievement(new WhoAreYou());
        AuroraMCAPI.registerAchievement(new BFF());
        AuroraMCAPI.registerAchievement(new Stalker());
        AuroraMCAPI.registerAchievement(new LetsGetThisPartyStarted());
        AuroraMCAPI.registerAchievement(new ImComingToo());
        AuroraMCAPI.registerAchievement(new YoureComingWithMe());
        AuroraMCAPI.registerAchievement(new Late());
        AuroraMCAPI.registerAchievement(new PartyAnimal());
        AuroraMCAPI.registerAchievement(new PartyAllNight());
        AuroraMCAPI.registerAchievement(new Awkward());
        AuroraMCAPI.registerAchievement(new HappyWithMyOwnCompany());
        AuroraMCAPI.registerAchievement(new GettingUsedToThis());
        AuroraMCAPI.registerAchievement(new IThinkILikeThis());
        AuroraMCAPI.registerAchievement(new MyNewHome());
        AuroraMCAPI.registerAchievement(new ADayOfMyLife());
        AuroraMCAPI.registerAchievement(new BestWeekendOfMyLife());
        AuroraMCAPI.registerAchievement(new WoahAWeek());
        AuroraMCAPI.registerAchievement(new ThatsFarTooLong());
        AuroraMCAPI.registerAchievement(new HappyBirthday());
        AuroraMCAPI.registerAchievement(new FirstFootOnTheLadder());
        AuroraMCAPI.registerAchievement(new LearningTheRopes());
        AuroraMCAPI.registerAchievement(new ClimbingTheLadder());
        AuroraMCAPI.registerAchievement(new GoingUpInTheWorld());
        AuroraMCAPI.registerAchievement(new YoureQuiteGoodAtThis());
        AuroraMCAPI.registerAchievement(new Fancy());
        AuroraMCAPI.registerAchievement(new Almighty());
        AuroraMCAPI.registerAchievement(new Unholy());
        AuroraMCAPI.registerAchievement(new Beast());
        AuroraMCAPI.registerAchievement(new God());


        //Registering default Event Listeners
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandManager(), this);
        Bukkit.getPluginManager().registerEvents(new GUIManager(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);

        //Register the BungeeCord plugin message channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "auroramc:server", new PluginMessageRecievedListener());

        getLogger().info("AuroraMC-Core loaded and ready to accept connections.");
    }

    @Override
    public void onDisable() {

    }
}
