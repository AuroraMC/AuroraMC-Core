/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core;

import net.auroramc.core.achievements.experience.*;
import net.auroramc.core.achievements.friends.*;
import net.auroramc.core.achievements.game.crystalquest.*;
import net.auroramc.core.achievements.game.ffa.*;
import net.auroramc.core.achievements.game.hotpotato.*;
import net.auroramc.core.achievements.game.spleef.*;
import net.auroramc.core.achievements.general.Murderer;
import net.auroramc.core.achievements.general.*;
import net.auroramc.core.achievements.lobby.*;
import net.auroramc.core.achievements.loyalty.HappyBirthday;
import net.auroramc.core.achievements.party.*;
import net.auroramc.core.achievements.time.*;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.backend.communication.CommunicationUtils;
import net.auroramc.core.api.backend.communication.Protocol;
import net.auroramc.core.api.backend.communication.ProtocolMessage;
import net.auroramc.core.commands.admin.*;
import net.auroramc.core.commands.admin.cosmetic.CommandCosmetic;
import net.auroramc.core.commands.admin.debug.CommandKillMessageTest;
import net.auroramc.core.commands.admin.iplookup.CommandIPLookup;
import net.auroramc.core.commands.general.*;
import net.auroramc.core.commands.general.ignore.CommandIgnore;
import net.auroramc.core.commands.moderation.*;
import net.auroramc.core.commands.moderation.qualityassurance.CommandAppeal;
import net.auroramc.core.commands.moderation.report.CommandReportClose;
import net.auroramc.core.commands.moderation.report.CommandReportHandle;
import net.auroramc.core.commands.moderation.report.CommandReportInfo;
import net.auroramc.core.commands.moderation.staffmanagement.CommandRecruitmentLookup;
import net.auroramc.core.cosmetics.banners.Belgium;
import net.auroramc.core.cosmetics.banners.Denmark;
import net.auroramc.core.cosmetics.banners.France;
import net.auroramc.core.cosmetics.banners.Germany;
import net.auroramc.core.cosmetics.banners.Japan;
import net.auroramc.core.cosmetics.banners.Poland;
import net.auroramc.core.cosmetics.banners.UnitedStates;
import net.auroramc.core.cosmetics.banners.*;
import net.auroramc.core.cosmetics.deatheffects.Confetti;
import net.auroramc.core.cosmetics.deatheffects.Firework;
import net.auroramc.core.cosmetics.deatheffects.LayAnEgg;
import net.auroramc.core.cosmetics.emotes.*;
import net.auroramc.core.cosmetics.friendstatuses.*;
import net.auroramc.core.cosmetics.gadgets.FireworkGadget;
import net.auroramc.core.cosmetics.gadgets.GrapplingHook;
import net.auroramc.core.cosmetics.hats.*;
import net.auroramc.core.cosmetics.hats.rewards.*;
import net.auroramc.core.cosmetics.killmessages.DeveloperKillPlayer;
import net.auroramc.core.cosmetics.killmessages.HalfWayThere;
import net.auroramc.core.cosmetics.killmessages.KillerGoose;
import net.auroramc.core.cosmetics.killmessages.Rainbow;
import net.auroramc.core.cosmetics.killmessages.levelrewards.Flex;
import net.auroramc.core.cosmetics.particleeffects.BloodSwirl;
import net.auroramc.core.cosmetics.particleeffects.EmeraldSwirl;
import net.auroramc.core.cosmetics.plussymbols.*;
import net.auroramc.core.cosmetics.plussymbols.Heart;
import net.auroramc.core.cosmetics.plussymbols.kitrewards.Castle;
import net.auroramc.core.cosmetics.plussymbols.kitrewards.Pickaxe;
import net.auroramc.core.cosmetics.plussymbols.kitrewards.Sword;
import net.auroramc.core.cosmetics.projectiletrails.EmeraldTrail;
import net.auroramc.core.cosmetics.projectiletrails.FireworkTrail;
import net.auroramc.core.cosmetics.servermessages.*;
import net.auroramc.core.cosmetics.wineffects.ConfettiCannon;
import net.auroramc.core.cosmetics.wineffects.Eggsplosion;
import net.auroramc.core.cosmetics.wineffects.Fireworks;
import net.auroramc.core.cosmetics.wineffects.TwerkApocalypse;
import net.auroramc.core.listeners.*;
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

        //Crystal Quest Achievements
        AuroraMCAPI.registerAchievement(new KillHungry());
        AuroraMCAPI.registerAchievement(new CrystalCrazy());
        AuroraMCAPI.registerAchievement(new Failure());
        AuroraMCAPI.registerAchievement(new Unlucky());
        AuroraMCAPI.registerAchievement(new YouDidIt());
        AuroraMCAPI.registerAchievement(new AhItsGoodToBeHome());
        AuroraMCAPI.registerAchievement(new Assistant());
        AuroraMCAPI.registerAchievement(new CookieMonsterLovesCookies());
        AuroraMCAPI.registerAchievement(new HeyStopThatsYourFriend());
        AuroraMCAPI.registerAchievement(new Hoarder());
        AuroraMCAPI.registerAchievement(new MaximumEfficiency());
        AuroraMCAPI.registerAchievement(new MissleStrike());
        AuroraMCAPI.registerAchievement(new Sniper());
        AuroraMCAPI.registerAchievement(new TeamworkMakesTheDreamWork());
        AuroraMCAPI.registerAchievement(new YouCantSeeMe());


        //Spleef Achievements
        AuroraMCAPI.registerAchievement(new AnArmLikeSnowbodyElse());
        AuroraMCAPI.registerAchievement(new AndHisNameIs());
        AuroraMCAPI.registerAchievement(new AndIOop());
        AuroraMCAPI.registerAchievement(new JustASecond());
        AuroraMCAPI.registerAchievement(new StraightUpBallin());
        AuroraMCAPI.registerAchievement(new TisTheSeason());
        AuroraMCAPI.registerAchievement(new WaitIsThisPrisons());

        //FFA Achievements
        AuroraMCAPI.registerAchievement(new CliffJumper());
        AuroraMCAPI.registerAchievement(new GetBackToWork());
        AuroraMCAPI.registerAchievement(new GetOutOfMyWay());
        AuroraMCAPI.registerAchievement(new HolyDamage());
        AuroraMCAPI.registerAchievement(new LeapFrog());
        AuroraMCAPI.registerAchievement(new OneAMasterOfDeath());
        AuroraMCAPI.registerAchievement(new Rampage());
        AuroraMCAPI.registerAchievement(new TheUselessUpgrade());
        AuroraMCAPI.registerAchievement(new TtttttripleKill());
        AuroraMCAPI.registerAchievement(new YoureAbsolutelyGodTier());

        //Hot Potato Achievements
        AuroraMCAPI.registerAchievement(new DamnThatWasClose());
        AuroraMCAPI.registerAchievement(new DamnYouveGotMoves());
        AuroraMCAPI.registerAchievement(new NopeCantDoThat());
        AuroraMCAPI.registerAchievement(new OuchThatsHot());
        AuroraMCAPI.registerAchievement(new ReturnToSender());
        AuroraMCAPI.registerAchievement(new SafetyIsKey());
        AuroraMCAPI.registerAchievement(new Smokin());
        AuroraMCAPI.registerAchievement(new ThatsNotNice());
        AuroraMCAPI.registerAchievement(new TheLuckWasNotOnMySide());
        AuroraMCAPI.registerAchievement(new WhoLeftTheOvenOn());
        AuroraMCAPI.registerAchievement(new WhyMe());

        //Lobby Achievements
        AuroraMCAPI.registerAchievement(new JumperMcJumperson());
        AuroraMCAPI.registerAchievement(new ParkourMaster());
        AuroraMCAPI.registerAchievement(new DamnThatWasChallenging());
        AuroraMCAPI.registerAchievement(new HardcoreParkour());
        AuroraMCAPI.registerAchievement(new IsThatAllYouveGot());
        AuroraMCAPI.registerAchievement(new TooEasyForMe());
        AuroraMCAPI.registerAchievement(new WhoopsMyBad());

        //Banners
        AuroraMCAPI.registerCosmetic(new AngryCraig());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.banners.AuroraMC());
        AuroraMCAPI.registerCosmetic(new Blitzen());
        AuroraMCAPI.registerCosmetic(new BunniWabbit());
        AuroraMCAPI.registerCosmetic(new CandyCane());
        AuroraMCAPI.registerCosmetic(new Derp());
        AuroraMCAPI.registerCosmetic(new Honk());
        AuroraMCAPI.registerCosmetic(new JackOLantern());
        AuroraMCAPI.registerCosmetic(new NinjaMonkey());
        AuroraMCAPI.registerCosmetic(new Ogre());
        AuroraMCAPI.registerCosmetic(new Pirate());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.banners.Plus());
        AuroraMCAPI.registerCosmetic(new RainbowRoad());
        AuroraMCAPI.registerCosmetic(new ScreamingGhost());
        AuroraMCAPI.registerCosmetic(new TheEnd());
        AuroraMCAPI.registerCosmetic(new TheGoose());
        AuroraMCAPI.registerCosmetic(new UnitedKingdom());
        AuroraMCAPI.registerCosmetic(new UnitedStates());
        AuroraMCAPI.registerCosmetic(new France());
        AuroraMCAPI.registerCosmetic(new Poland());
        AuroraMCAPI.registerCosmetic(new Denmark());
        AuroraMCAPI.registerCosmetic(new Belgium());
        AuroraMCAPI.registerCosmetic(new Germany());
        AuroraMCAPI.registerCosmetic(new Japan());
        AuroraMCAPI.registerCosmetic(new UwU());
        AuroraMCAPI.registerCosmetic(new Wreath());

        //Friend Statuses
        AuroraMCAPI.registerCosmetic(new AFK());
        AuroraMCAPI.registerCosmetic(new CountingMoney());
        AuroraMCAPI.registerCosmetic(new CreatingNewMaps());
        AuroraMCAPI.registerCosmetic(new DoNotDisturb());
        AuroraMCAPI.registerCosmetic(new Idle());
        AuroraMCAPI.registerCosmetic(new LiveStreaming());
        AuroraMCAPI.registerCosmetic(new MasteringAllTheGames());
        AuroraMCAPI.registerCosmetic(new Offline());
        AuroraMCAPI.registerCosmetic(new Online());
        AuroraMCAPI.registerCosmetic(new ProcessingReports());
        AuroraMCAPI.registerCosmetic(new Programming());
        AuroraMCAPI.registerCosmetic(new Recording());
        AuroraMCAPI.registerCosmetic(new RuiningLives());
        AuroraMCAPI.registerCosmetic(new SwingingTheBanHammer());
        AuroraMCAPI.registerCosmetic(new BusyHavingALife());
        AuroraMCAPI.registerCosmetic(new CountingSheep());
        AuroraMCAPI.registerCosmetic(new DabbinOnTheHaters());
        AuroraMCAPI.registerCosmetic(new EatingDinner());
        AuroraMCAPI.registerCosmetic(new GettingSomeZzzs());
        AuroraMCAPI.registerCosmetic(new HitTheHay());
        AuroraMCAPI.registerCosmetic(new KeepingAuroraMCGoing());
        AuroraMCAPI.registerCosmetic(new WatchingCatVideos());
        AuroraMCAPI.registerCosmetic(new CommittedForLife());
        AuroraMCAPI.registerCosmetic(new BugHunter());
        AuroraMCAPI.registerCosmetic(new PreparingToParty());

        //Plus Symbols
        AuroraMCAPI.registerCosmetic(new Star());
        AuroraMCAPI.registerCosmetic(new Snowman());
        AuroraMCAPI.registerCosmetic(new Tea());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.plussymbols.AuroraMC());
        AuroraMCAPI.registerCosmetic(new Copyright());
        AuroraMCAPI.registerCosmetic(new Snowflake());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.plussymbols.Plus());
        AuroraMCAPI.registerCosmetic(new Heart());
        AuroraMCAPI.registerCosmetic(new Radioactive());
        AuroraMCAPI.registerCosmetic(new Peace());
        AuroraMCAPI.registerCosmetic(new ShootingStar());
        AuroraMCAPI.registerCosmetic(new Mathematician());
        AuroraMCAPI.registerCosmetic(new Flower());
        AuroraMCAPI.registerCosmetic(new Music());
        AuroraMCAPI.registerCosmetic(new Infinity());
        AuroraMCAPI.registerCosmetic(new Arrow());
        AuroraMCAPI.registerCosmetic(new Pencil());
        AuroraMCAPI.registerCosmetic(new Balanced());
        AuroraMCAPI.registerCosmetic(new Smile());
        AuroraMCAPI.registerCosmetic(new Sunny());
        AuroraMCAPI.registerCosmetic(new Hazard());
        AuroraMCAPI.registerCosmetic(new Castle());
        AuroraMCAPI.registerCosmetic(new Pickaxe());
        AuroraMCAPI.registerCosmetic(new Sword());

        //Hats
        AuroraMCAPI.registerCosmetic(new Astronaut());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.Belgium());
        AuroraMCAPI.registerCosmetic(new Brazil());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.Denmark());
        AuroraMCAPI.registerCosmetic(new Finland());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.France());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.Germany());
        AuroraMCAPI.registerCosmetic(new Italy());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.Japan());
        AuroraMCAPI.registerCosmetic(new Korea());
        AuroraMCAPI.registerCosmetic(new Netherlands());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.Poland());
        AuroraMCAPI.registerCosmetic(new Portugal());
        AuroraMCAPI.registerCosmetic(new Spain());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.hats.UnitedStates());
        AuroraMCAPI.registerCosmetic(new ShulkerOne());
        AuroraMCAPI.registerCosmetic(new ShulkerTwo());
        AuroraMCAPI.registerCosmetic(new ShulkerThree());
        AuroraMCAPI.registerCosmetic(new ShulkerFour());
        AuroraMCAPI.registerCosmetic(new ShulkerFive());
        AuroraMCAPI.registerCosmetic(new ShulkerSix());
        AuroraMCAPI.registerCosmetic(new ShulkerSeven());
        AuroraMCAPI.registerCosmetic(new ShulkerEight());
        AuroraMCAPI.registerCosmetic(new ShulkerNine());
        AuroraMCAPI.registerCosmetic(new ShulkerTen());
        AuroraMCAPI.registerCosmetic(new ShulkerEleven());
        AuroraMCAPI.registerCosmetic(new ShulkerTwelve());
        AuroraMCAPI.registerCosmetic(new ShulkerThirteen());
        AuroraMCAPI.registerCosmetic(new ShulkerFourteen());
        AuroraMCAPI.registerCosmetic(new ShulkerFifteen());
        AuroraMCAPI.registerCosmetic(new ShulkerSixteen());
        AuroraMCAPI.registerCosmetic(new ShulkerSeventeen());
        AuroraMCAPI.registerCosmetic(new ShulkerEighteen());
        AuroraMCAPI.registerCosmetic(new ShulkerNineteen());
        AuroraMCAPI.registerCosmetic(new ShulkerTwenty());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyOne());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyTwo());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyThree());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyFour());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyFive());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentySix());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentySeven());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyEight());
        AuroraMCAPI.registerCosmetic(new ShulkerTwentyNine());
        AuroraMCAPI.registerCosmetic(new ShulkerThirty());
        AuroraMCAPI.registerCosmetic(new Goose());

        //Server Messages
        AuroraMCAPI.registerCosmetic(new Default());
        AuroraMCAPI.registerCosmetic(new DeveloperPlayGames());
        AuroraMCAPI.registerCosmetic(new PartyTime());
        AuroraMCAPI.registerCosmetic(new Hungry());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.servermessages.Murderer());
        AuroraMCAPI.registerCosmetic(new PeterPan());

        //Win Effects
        AuroraMCAPI.registerCosmetic(new Fireworks());
        AuroraMCAPI.registerCosmetic(new TwerkApocalypse());
        AuroraMCAPI.registerCosmetic(new Eggsplosion());
        AuroraMCAPI.registerCosmetic(new ConfettiCannon());

        //Death Effects
        AuroraMCAPI.registerCosmetic(new Firework());
        AuroraMCAPI.registerCosmetic(new LayAnEgg());
        AuroraMCAPI.registerCosmetic(new Confetti());

        //Gadgets
        AuroraMCAPI.registerCosmetic(new FireworkGadget());
        AuroraMCAPI.registerCosmetic(new GrapplingHook());

        //Particle Effects
        AuroraMCAPI.registerCosmetic(new BloodSwirl());
        AuroraMCAPI.registerCosmetic(new EmeraldSwirl());

        //Projectile Trails
        AuroraMCAPI.registerCosmetic(new FireworkTrail());
        AuroraMCAPI.registerCosmetic(new EmeraldTrail());

        //Kill Messages
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.killmessages.Default());
        AuroraMCAPI.registerCosmetic(new HalfWayThere());
        AuroraMCAPI.registerCosmetic(new Rainbow());
        AuroraMCAPI.registerCosmetic(new DeveloperKillPlayer());
        AuroraMCAPI.registerCosmetic(new Flex());
        AuroraMCAPI.registerCosmetic(new KillerGoose());

        //Emotes
        AuroraMCAPI.registerCosmetic(new Shrug());
        AuroraMCAPI.registerCosmetic(new net.auroramc.core.cosmetics.emotes.Heart());
        AuroraMCAPI.registerCosmetic(new Left());
        AuroraMCAPI.registerCosmetic(new Right());
        AuroraMCAPI.registerCosmetic(new Up());
        AuroraMCAPI.registerCosmetic(new Yes());
        AuroraMCAPI.registerCosmetic(new No());


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

        //Register the BungeeCord plugin message channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "auroramc:server", new PluginMessageRecievedListener());

        getLogger().info("AuroraMC-Core loaded and ready to accept connections. Letting mission control know...");
        ProtocolMessage message = new ProtocolMessage(Protocol.SERVER_ONLINE, "Mission Control", "", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name());
        CommunicationUtils.sendMessage(message);
    }

    @Override
    public void onDisable() {
        if (!CommunicationUtils.isShutdown()) {
            if (!AuroraMCAPI.isShuttingDown()) {
                ProtocolMessage message = new ProtocolMessage(Protocol.CONFIRM_SHUTDOWN, "Mission Control", "forced", AuroraMCAPI.getServerInfo().getName(), AuroraMCAPI.getServerInfo().getNetwork().name());
                CommunicationUtils.sendMessage(message);
            }
            CommunicationUtils.shutdown();
        }
    }
}
