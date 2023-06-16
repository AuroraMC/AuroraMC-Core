/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.commands.moderation.staffmanagement;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Permission;
import net.auroramc.api.punishments.Punishment;
import net.auroramc.api.punishments.PunishmentHistory;
import net.auroramc.api.punishments.ipprofiles.PlayerProfile;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.ServerCommand;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.gui.misc.RecruitmentLookup;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandRecruitmentLookup extends ServerCommand {


    public CommandRecruitmentLookup() {
        super("rlookup", Collections.emptyList(), Collections.singletonList(Permission.RECRUITMENT), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id < 1) {
                        player.sendMessage(TextFormatter.pluginMessage("Punish", String.format("User [**%s**] has never joined the network, so cannot have received a punishment.", args.get(0))));
                        return;
                    }

                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);

                    PlayerProfile lookup = AuroraMCAPI.getDbManager().ipLookup(uuid);
                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, id);
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
                        }
                    }.runTask(ServerAPI.getCore());

                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Recruitment", "Invalid syntax. Correct syntax: **/rlookup [username]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
