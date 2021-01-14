package net.auroramc.core.commands.moderation.staffmanagement;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.lookup.IPLookup;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.PunishmentHistory;
import net.auroramc.core.api.stats.PlayerStatistics;
import net.auroramc.core.api.utils.UUIDUtil;
import net.auroramc.core.gui.misc.RecruitmentLookup;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandRecruitmentLookup extends Command {


    public CommandRecruitmentLookup() {
        super("rlookup", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("recruitment"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = UUIDUtil.getUUID(args.get(0));
                    if (uuid == null) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Recruitment", String.format("Player [**%s**] does not exist.", args.get(0))));
                        return;
                    }

                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                    if (id < 1) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot have received a punishment.", args.get(0))));
                        return;
                    }

                    IPLookup lookup = AuroraMCAPI.getDbManager().ipLookup(uuid);
                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid);
                    List<Punishment> punishments = AuroraMCAPI.getDbManager().getPunishmentHistory(id);
                    PunishmentHistory history = new PunishmentHistory(id);
                    for (Punishment punishment : punishments) {
                        history.registerPunishment(punishment);
                    }

                    RecruitmentLookup recruitmentLookup = new RecruitmentLookup(args.get(0), history, stats, lookup);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            recruitmentLookup.open(player);
                            AuroraMCAPI.openGUI(player, recruitmentLookup);
                        }
                    }.runTask(AuroraMCAPI.getCore());

                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Recruitment", "Invalid syntax. Correct syntax: **/rlookup [username]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
