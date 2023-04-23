/*
 * Copyright (c) 2023 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.common;

import net.auroramc.api.AuroraMCAPI;

import net.auroramc.api.utils.Reward;
import net.auroramc.common.achievements.experience.*;
import net.auroramc.common.achievements.friends.*;
import net.auroramc.common.achievements.game.crystalquest.*;
import net.auroramc.common.achievements.game.ffa.*;
import net.auroramc.common.achievements.game.hotpotato.*;
import net.auroramc.common.achievements.game.spleef.*;
import net.auroramc.common.achievements.general.*;
import net.auroramc.common.achievements.general.Murderer;
import net.auroramc.common.achievements.lobby.*;
import net.auroramc.common.achievements.loyalty.*;
import net.auroramc.common.achievements.party.*;
import net.auroramc.common.achievements.time.*;

import net.auroramc.common.cosmetics.banners.*;
import net.auroramc.common.cosmetics.banners.AuroraMC;
import net.auroramc.common.cosmetics.banners.Belgium;
import net.auroramc.common.cosmetics.banners.Denmark;
import net.auroramc.common.cosmetics.banners.France;
import net.auroramc.common.cosmetics.banners.Germany;
import net.auroramc.common.cosmetics.banners.Japan;
import net.auroramc.common.cosmetics.banners.Poland;
import net.auroramc.common.cosmetics.banners.UnitedStates;
import net.auroramc.common.cosmetics.deatheffects.*;
import net.auroramc.common.cosmetics.emotes.*;
import net.auroramc.common.cosmetics.emotes.Heart;
import net.auroramc.common.cosmetics.emotes.Peace;
import net.auroramc.common.cosmetics.friendstatuses.*;
import net.auroramc.common.cosmetics.gadgets.*;
import net.auroramc.common.cosmetics.hats.*;
import net.auroramc.common.cosmetics.hats.countries.*;
import net.auroramc.common.cosmetics.hats.rewards.*;
import net.auroramc.common.cosmetics.killmessages.*;
import net.auroramc.common.cosmetics.killmessages.levelrewards.Flex;
import net.auroramc.common.cosmetics.particleeffects.*;
import net.auroramc.common.cosmetics.plussymbols.*;
import net.auroramc.common.cosmetics.plussymbols.Star;
import net.auroramc.common.cosmetics.plussymbols.Tea;
import net.auroramc.common.cosmetics.plussymbols.kitrewards.*;
import net.auroramc.common.cosmetics.projectiletrails.*;
import net.auroramc.common.cosmetics.servermessages.*;
import net.auroramc.common.cosmetics.servermessages.Default;
import net.auroramc.common.cosmetics.wineffects.*;

import java.util.Collections;

public class CommonUtils {

    public static void loadCosmetics() {
        //Banners
        AuroraMCAPI.registerCosmetic(new AngryCraig());
        AuroraMCAPI.registerCosmetic(new AuroraMC());
        AuroraMCAPI.registerCosmetic(new Blitzen());
        AuroraMCAPI.registerCosmetic(new BunniWabbit());
        AuroraMCAPI.registerCosmetic(new CandyCane());
        AuroraMCAPI.registerCosmetic(new Derp());
        AuroraMCAPI.registerCosmetic(new Honk());
        AuroraMCAPI.registerCosmetic(new JackOLantern());
        AuroraMCAPI.registerCosmetic(new NinjaMonkey());
        AuroraMCAPI.registerCosmetic(new Ogre());
        AuroraMCAPI.registerCosmetic(new Pirate());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.banners.Plus());
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
        AuroraMCAPI.registerCosmetic(new NotSwimmingThatsForSure());

        //Plus Symbols
        AuroraMCAPI.registerCosmetic(new Star());
        AuroraMCAPI.registerCosmetic(new Snowman());
        AuroraMCAPI.registerCosmetic(new Tea());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.plussymbols.AuroraMC());
        AuroraMCAPI.registerCosmetic(new Copyright());
        AuroraMCAPI.registerCosmetic(new Snowflake());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.plussymbols.Plus());
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
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.Belgium());
        AuroraMCAPI.registerCosmetic(new Brazil());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.Denmark());
        AuroraMCAPI.registerCosmetic(new Finland());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.France());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.Germany());
        AuroraMCAPI.registerCosmetic(new Italy());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.Japan());
        AuroraMCAPI.registerCosmetic(new Korea());
        AuroraMCAPI.registerCosmetic(new Netherlands());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.Poland());
        AuroraMCAPI.registerCosmetic(new Portugal());
        AuroraMCAPI.registerCosmetic(new Spain());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.hats.countries.UnitedStates());
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
        AuroraMCAPI.registerCosmetic(new Enderman());
        AuroraMCAPI.registerCosmetic(new Croatia());
        AuroraMCAPI.registerCosmetic(new England());
        AuroraMCAPI.registerCosmetic(new Estonia());
        AuroraMCAPI.registerCosmetic(new Iceland());
        AuroraMCAPI.registerCosmetic(new Ireland());
        AuroraMCAPI.registerCosmetic(new Latvia());
        AuroraMCAPI.registerCosmetic(new Lithuania());
        AuroraMCAPI.registerCosmetic(new Norway());
        AuroraMCAPI.registerCosmetic(new Scotland());
        AuroraMCAPI.registerCosmetic(new Serbia());
        AuroraMCAPI.registerCosmetic(new Singapore());
        AuroraMCAPI.registerCosmetic(new Sweden());
        AuroraMCAPI.registerCosmetic(new Ukraine());
        AuroraMCAPI.registerCosmetic(new Wales());

        //Server Messages
        AuroraMCAPI.registerCosmetic(new Default());
        AuroraMCAPI.registerCosmetic(new DeveloperPlayGames());
        AuroraMCAPI.registerCosmetic(new PartyTime());
        AuroraMCAPI.registerCosmetic(new Hungry());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.servermessages.Murderer());
        AuroraMCAPI.registerCosmetic(new PeterPan());
        AuroraMCAPI.registerCosmetic(new Teleported());

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
        AuroraMCAPI.registerCosmetic(new PurpleSwirl());

        //Projectile Trails
        AuroraMCAPI.registerCosmetic(new FireworkTrail());
        AuroraMCAPI.registerCosmetic(new EmeraldTrail());
        AuroraMCAPI.registerCosmetic(new PurpleTrail());

        //Kill Messages
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.killmessages.Default());
        AuroraMCAPI.registerCosmetic(new HalfWayThere());
        AuroraMCAPI.registerCosmetic(new Rainbow());
        AuroraMCAPI.registerCosmetic(new DeveloperKillPlayer());
        AuroraMCAPI.registerCosmetic(new Flex());
        AuroraMCAPI.registerCosmetic(new KillerGoose());

        //Emotes
        AuroraMCAPI.registerCosmetic(new Shrug());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.emotes.Heart());
        AuroraMCAPI.registerCosmetic(new Left());
        AuroraMCAPI.registerCosmetic(new Right());
        AuroraMCAPI.registerCosmetic(new Up());
        AuroraMCAPI.registerCosmetic(new Yes());
        AuroraMCAPI.registerCosmetic(new No());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.emotes.Peace());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.emotes.Star());
        AuroraMCAPI.registerCosmetic(new net.auroramc.common.cosmetics.emotes.Tea());
        AuroraMCAPI.registerCosmetic(new Tableflip());
        AuroraMCAPI.registerCosmetic(new Unflip());
        AuroraMCAPI.registerCosmetic(new Wave());
        AuroraMCAPI.registerCosmetic(new Dead());
        AuroraMCAPI.registerCosmetic(new Hug());
        AuroraMCAPI.registerCosmetic(new HeartEyes());
        AuroraMCAPI.registerCosmetic(new Running());
        AuroraMCAPI.registerCosmetic(new Angry());
        AuroraMCAPI.registerCosmetic(new Wink());
    }

    public static void loadAchievements() {
        //Register achievements with the API
        AuroraMCAPI.registerAchievement(new Welcome());
        AuroraMCAPI.registerAchievement(new OG());
        AuroraMCAPI.registerAchievement(new net.auroramc.common.achievements.general.Elite());
        AuroraMCAPI.registerAchievement(new net.auroramc.common.achievements.general.Master());
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
    }

    public static void loadRewards() {
        AuroraMCAPI.getLevelRewards().put(1, new Reward("ShulkerOne Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(316)));
        AuroraMCAPI.getLevelRewards().put(2, new Reward("ShulkerTwo Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(317)));
        AuroraMCAPI.getLevelRewards().put(3, new Reward("ShulkerThree Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(318)));
        AuroraMCAPI.getLevelRewards().put(4, new Reward("ShulkerFour Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(319)));
        AuroraMCAPI.getLevelRewards().put(5, new Reward("ShulkerFive Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(320)));
        AuroraMCAPI.getLevelRewards().put(6, new Reward("ShulkerSix Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(321)));
        AuroraMCAPI.getLevelRewards().put(7, new Reward("ShulkerSeven Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(322)));
        AuroraMCAPI.getLevelRewards().put(8, new Reward("ShulkerEight Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(323)));
        AuroraMCAPI.getLevelRewards().put(9, new Reward("ShulkerNine Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(324)));
        AuroraMCAPI.getLevelRewards().put(10, new Reward("ShulkerTen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(325)));
        AuroraMCAPI.getLevelRewards().put(11, new Reward("ShulkerEleven Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(326)));
        AuroraMCAPI.getLevelRewards().put(12, new Reward("ShulkerTwelve Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(327)));
        AuroraMCAPI.getLevelRewards().put(13, new Reward("ShulkerThirteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(328)));
        AuroraMCAPI.getLevelRewards().put(14, new Reward("ShulkerFourteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(329)));
        AuroraMCAPI.getLevelRewards().put(15, new Reward("ShulkerFifteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(330)));
        AuroraMCAPI.getLevelRewards().put(16, new Reward("ShulkerSixteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(331)));
        AuroraMCAPI.getLevelRewards().put(17, new Reward("ShulkerSeventeen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(332)));
        AuroraMCAPI.getLevelRewards().put(18, new Reward("ShulkerEighteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(333)));
        AuroraMCAPI.getLevelRewards().put(19, new Reward("ShulkerNineteen Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(334)));
        AuroraMCAPI.getLevelRewards().put(20, new Reward("ShulkerTwenty Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(335)));
        AuroraMCAPI.getLevelRewards().put(21, new Reward("ShulkerTwentyOne Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(336)));
        AuroraMCAPI.getLevelRewards().put(22, new Reward("ShulkerTwentyTwo Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(337)));
        AuroraMCAPI.getLevelRewards().put(23, new Reward("ShulkerTwentyThree Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(338)));
        AuroraMCAPI.getLevelRewards().put(24, new Reward("ShulkerTwentyFour Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(339)));
        AuroraMCAPI.getLevelRewards().put(25, new Reward("ShulkerTwentyFive Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(340)));
        AuroraMCAPI.getLevelRewards().put(26, new Reward("ShulkerTwentySix Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(341)));
        AuroraMCAPI.getLevelRewards().put(27, new Reward("ShulkerTwentySeven Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(342)));
        AuroraMCAPI.getLevelRewards().put(28, new Reward("ShulkerTwentyEight Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(343)));
        AuroraMCAPI.getLevelRewards().put(29, new Reward("ShulkerTwentyNine Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(344)));
        AuroraMCAPI.getLevelRewards().put(30, new Reward("ShulkerThirty Hat", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(345)));
        AuroraMCAPI.getLevelRewards().put(100, new Reward("Flex Kill Message", 0, 0, 0, Collections.emptyMap(), Collections.singletonList(507)));
    }

}
