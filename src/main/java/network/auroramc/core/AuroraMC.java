package network.auroramc.core;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.commands.*;
import network.auroramc.core.listeners.JoinListener;
import network.auroramc.core.listeners.TempChatListener;
import network.auroramc.core.listeners.TempJoinListener;
import network.auroramc.core.managers.CommandManager;
import network.auroramc.core.managers.GUIManager;
import network.auroramc.core.permissions.permissions.*;
import network.auroramc.core.permissions.ranks.*;
import network.auroramc.core.permissions.ranks.Admin;
import network.auroramc.core.permissions.ranks.BuildTeamManagement;
import network.auroramc.core.permissions.ranks.Elite;
import network.auroramc.core.permissions.ranks.Master;
import network.auroramc.core.permissions.ranks.Player;
import network.auroramc.core.permissions.subranks.JrQA;
import network.auroramc.core.permissions.subranks.SrDev;
import network.auroramc.core.permissions.subranks.SrQA;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
        AuroraMCAPI.registerPermission(new Ultimate());

        //Register Ranks with the API.
        AuroraMCAPI.registerRank(new Player());
        AuroraMCAPI.registerRank(new Elite());
        AuroraMCAPI.registerRank(new Master());
        AuroraMCAPI.registerRank(new YouTube());
        AuroraMCAPI.registerRank(new Twitch());
        AuroraMCAPI.registerRank(new Developer());
        AuroraMCAPI.registerRank(new Builder());
        AuroraMCAPI.registerRank(new TrialModerator());
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

        //Register Commands with the API.
        AuroraMCAPI.registerCommand(new CommandSetRank());
        AuroraMCAPI.registerCommand(new CommandLink());
        AuroraMCAPI.registerCommand(new CommandPunish());
        AuroraMCAPI.registerCommand(new CommandPunishHistory());
        AuroraMCAPI.registerCommand(new CommandEvidence());
        AuroraMCAPI.registerCommand(new CommandSM());

        //Registering default Event Listeners
        Bukkit.getPluginManager().registerEvents(new TempChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new TempJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandManager(), this);
        Bukkit.getPluginManager().registerEvents(new GUIManager(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        //Register the BungeeCord plugin message channel
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public static AuroraMC get() {
        return i;
    }

    @Override
    public void onDisable() {

    }
}
