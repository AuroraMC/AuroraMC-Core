package network.auroramc.core.listeners;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.ServerInfo;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.punishments.Ban;
import network.auroramc.core.api.punishments.PunishmentLength;
import network.auroramc.core.api.punishments.Rule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        Ban ban = AuroraMCAPI.getDbManager().getBan(e.getUniqueId().toString());
        if (ban != null) {
            PunishmentLength length = new PunishmentLength((ban.getExpire() - System.currentTimeMillis())/3600000d);
            Rule rule = AuroraMCAPI.getRules().getRule(ban.getRuleID());

            switch (ban.getStatus()) {
                case 2:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s [Awaiting Approval]**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rYour punishment has been applied by a Junior Moderator, and is severe enough to need approval\n" +
                            "&rfrom a Staff Management member to ensure that the punishment applied was correct. When it is\n" +
                            "&rapproved, the full punishment length will automatically apply to you. If this punishment is\n" +
                            "&rdenied for being false, **it will automatically be removed**, and the punishment will automatically\n" +
                            "&rbe removed from your Punishment History.\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.network.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                case 3:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s [SM Approved]**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.network.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
                default:
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AuroraMCAPI.getFormatter().pluginMessage("Punishments",String.format("You have been banned from the network.\n" +
                            "\n" +
                            "&rReason: **%s - %s**\n" +
                            "&rExpires:  **%s**\n" +
                            "\n" +
                            "&rPunishment Code: **%s**\n" +
                            "\n" +
                            "&rIf you believe this was a false punishment, please appeal at appeal.auroramc.network.", rule.getRuleName(), ban.getExtraNotes(), length.getFormatted(), ban.getCode())));
                    break;
            }
        }

        ServerInfo serverInfo = AuroraMCAPI.getServerInfo();
        if (serverInfo == null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AuroraMCAPI.getFormatter().pluginMessage("Server Manager", "I don't know what server I am! Please forward this to an admin!"));
            return;
        }

        e.allow();
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        AuroraMCPlayer player = new AuroraMCPlayer(e.getPlayer());
        AuroraMCAPI.newPlayer(player);


    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TabCompleteInjector.onJoin(AuroraMCAPI.getPlayer(e.getPlayer()));
        AuroraMCAPI.getPlayer(e.getPlayer()).setScoreboard();
    }

}
