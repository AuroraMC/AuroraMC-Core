package network.auroramc.core;

import network.auroramc.core.achievements.experience.*;
import network.auroramc.core.achievements.friends.*;
import network.auroramc.core.achievements.general.*;
import network.auroramc.core.achievements.loyalty.*;
import network.auroramc.core.achievements.party.*;
import network.auroramc.core.achievements.time.*;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.commands.admin.*;
import network.auroramc.core.commands.general.*;
import network.auroramc.core.commands.moderation.*;
import network.auroramc.core.listeners.*;
import network.auroramc.core.managers.CommandManager;
import network.auroramc.core.managers.GUIManager;
import network.auroramc.core.permissions.permissions.*;
import network.auroramc.core.permissions.ranks.Admin;
import network.auroramc.core.permissions.ranks.BuildTeamManagement;
import network.auroramc.core.permissions.ranks.Elite;
import network.auroramc.core.permissions.ranks.Master;
import network.auroramc.core.permissions.ranks.Player;
import network.auroramc.core.permissions.ranks.*;
import network.auroramc.core.permissions.subranks.JrQA;
import network.auroramc.core.permissions.subranks.SrDev;
import network.auroramc.core.permissions.subranks.SrQA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AuroraMC extends JavaPlugin {

    private CoreCache cache;
    private static AuroraMC i;

    @Override
    public void onEnable() {
        reloadConfig();
        getLogger().info("Loading AuroraMC-Core...");

        new AuroraMCAPI(this);
        cache = new CoreCache(this);
        AuroraMCAPI.registerCache(this, cache);

        AuroraMCAPI.loadRules();
        AuroraMCAPI.loadFilter();

        //Register Permissions with the API.
        AuroraMCAPI.registerPermission(new network.auroramc.core.permissions.permissions.Admin());
        AuroraMCAPI.registerPermission(new All());
        AuroraMCAPI.registerPermission(new network.auroramc.core.permissions.permissions.BuildTeamManagement());
        AuroraMCAPI.registerPermission(new BypassApproval());
        AuroraMCAPI.registerPermission(new DebugAction());
        AuroraMCAPI.registerPermission(new DebugInfo());
        AuroraMCAPI.registerPermission(new Disguise());
        AuroraMCAPI.registerPermission(new network.auroramc.core.permissions.permissions.Elite());
        AuroraMCAPI.registerPermission(new network.auroramc.core.permissions.permissions.Master());
        AuroraMCAPI.registerPermission(new Moderation());
        AuroraMCAPI.registerPermission(new network.auroramc.core.permissions.permissions.Player());
        AuroraMCAPI.registerPermission(new SocialMedia());
        AuroraMCAPI.registerPermission(new StaffManagement());
        AuroraMCAPI.registerPermission(new Support());
        AuroraMCAPI.registerPermission(new Plus());
        AuroraMCAPI.registerPermission(new Build());
        AuroraMCAPI.registerPermission(new Recruitment());

        //Register Ranks with the API.
        AuroraMCAPI.registerRank(new Player());
        AuroraMCAPI.registerRank(new Elite());
        AuroraMCAPI.registerRank(new Master());
        AuroraMCAPI.registerRank(new YouTube());
        AuroraMCAPI.registerRank(new Twitch());
        AuroraMCAPI.registerRank(new Developer());
        AuroraMCAPI.registerRank(new Builder());
        AuroraMCAPI.registerRank(new JuniorModerator());
        AuroraMCAPI.registerRank(new BuildTeamManagement());
        AuroraMCAPI.registerRank(new Moderator());
        AuroraMCAPI.registerRank(new Admin());
        AuroraMCAPI.registerRank(new Owner());


        //Register Subranks with the API.
        AuroraMCAPI.registerSubRank(new JrQA());
        AuroraMCAPI.registerSubRank(new SrQA());
        AuroraMCAPI.registerSubRank(new SrDev());
        AuroraMCAPI.registerSubRank(new network.auroramc.core.permissions.subranks.StaffManagement());
        AuroraMCAPI.registerSubRank(new network.auroramc.core.permissions.subranks.Support());
        AuroraMCAPI.registerSubRank(new network.auroramc.core.permissions.subranks.Recruitment());

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

        //Register achievements with the API
        AuroraMCAPI.registerAchievement(new Welcome());
        AuroraMCAPI.registerAchievement(new OG());
        AuroraMCAPI.registerAchievement(new network.auroramc.core.achievements.general.Elite());
        AuroraMCAPI.registerAchievement(new network.auroramc.core.achievements.general.Master());
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
        Bukkit.getPluginManager().registerEvents(new TempChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new TempJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandManager(), this);
        Bukkit.getPluginManager().registerEvents(new GUIManager(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);

        //Register the BungeeCord plugin message channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "Server", new PluginMessageRecievedListener());
    }

    public static AuroraMC get() {
        return i;
    }

    @Override
    public void onDisable() {

    }
}
