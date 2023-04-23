/*
 * Copyright (c) 2022 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.commands.moderation;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.backend.store.Payment;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.api.permissions.Permission;

import net.auroramc.core.api.player.AuroraMCServerPlayer;
import net.auroramc.core.gui.support.PaymentHistory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandPayments extends ServerCommand {

    public CommandPayments() {
        super("payments", Collections.singletonList("purchases"), Arrays.asList(Permission.DEBUG_ACTION, Permission.ADMIN, Permission.SUPPORT), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(0));
                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Payment", String.format("No records found for user **%s**.", args.get(0))));
                        return;
                    }

                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);
                    List<Payment> payments = AuroraMCAPI.getDbManager().getPayments(id);
                    if (payments.size() == 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Payment", String.format("User **%s** does not have any payments.", args.get(0))));
                        return;
                    }

                    PaymentHistory paymentHistory = new PaymentHistory(player, uuid, args.get(0), payments);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            paymentHistory.open(player);
                        }
                    }.runTask(ServerAPI.getCore());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Payment", "Invalid syntax. Correct syntax: **/payments [username]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
