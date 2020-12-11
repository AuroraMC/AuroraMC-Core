package net.auroramc.core.commands.moderation.qualityassurance;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.punishments.Punishment;
import net.auroramc.core.api.punishments.Rule;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandAppealLookup extends Command {

    public CommandAppealLookup() {
        super("punishmentlookup", new ArrayList<>(Collections.singletonList("plookup")),  new ArrayList<>(Arrays.asList(AuroraMCAPI.getPermissions().get("debug.info"),AuroraMCAPI.getPermissions().get("admin"))), false, null);
    }

    private static String[] statuses = {"&aActive","&6Pending Approval","&aSM Approved","&cSM Denied","&eExpired","&eExpired (Approved)","&7Warning"};
    private static String[] weights = {"&2Light", "&aMedium", "&eHeavy", "&6Severe", "&4Extreme"};

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if(args.size() == 1) {
            if(args.get(0).matches("[A-Z0-9]{8}")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        String code = args.remove(0);
                        Punishment punishment = AuroraMCAPI.getDbManager().getPunishment(code);
                        if(punishment != null) {
                            String username = punishment.getPunisherName();
                            int ruleId = punishment.getRuleID();
                            Rule rule = AuroraMCAPI.getRules().getRule(ruleId);
                            if(punishment.getStatus() == 5 || punishment.getStatus() == 6) {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("Punishment Lookup for punishment **%s**:\n" +
                                        "Evidence: **%s**\n" +
                                        "Status: **%s**\n" +
                                        "Reason: **%s**\n" +
                                        "Rule: **%s**\n" +
                                        "Rule Weight: **%s**" +
                                        "Issuer: **%s**\n" +
                                        "Expiry: **%s**\n",
                                        code, punishment.getEvidence(), statuses[punishment.getStatus()-1], punishment.getExtraNotes(), rule.getRuleName(), weights[rule.getWeight()-1], username, new Date(punishment.getRemovalTimestamp()))));
                            } else {
                                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("Punishment Lookup for punishment **%s**:\n" +
                                        "Evidence: **%s**\n" +
                                        "Status: **%s**\n" +
                                        "Issuer: **%s**\n" +
                                        "Reason: **%s**" +
                                        "Rule: **%s**" +
                                        "Rule Weight: **%s**",
                                        code, punishment.getEvidence(), statuses[punishment.getStatus()-1], username, punishment.getExtraNotes(), rule.getRuleName(), weights[rule.getWeight()-1])));
                            }

                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", String.format("No matches found for Punishment ID: [**%s**]", code)));
                        }
                    }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());

        } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/punishmentlookup [Punishment Code]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Appeal", "Invalid syntax. Correct syntax: **/punishmentlookup [Punishment Code]**"));
        }

    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return null;
    }
}
