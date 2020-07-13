package network.auroramc.core.commands;

import network.auroramc.core.AuroraMC;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.players.lookup.IPLookup;
import network.auroramc.core.api.players.lookup.LookupUser;
import network.auroramc.core.api.utils.UUIDUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandLookup extends Command {


    public CommandLookup() {
        super("lookup", new ArrayList<>(Arrays.asList("iplookup","ip")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("admin"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).matches("[0-9a-zA-Z_]{1,16}")) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Performing lookup for **%s**...", args.get(0))));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Player [**%s**] does not exist.", args.get(0))));
                            return;
                        }

                        IPLookup lookup = AuroraMCAPI.getDbManager().ipLookup(uuid);
                        if (lookup == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("No matches found for [**%s**].", args.get(0))));
                            return;
                        }

                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP lookup information for **%s**:\n" +
                                "The first account to join on this IP is **%s**.\n" +
                                "This IP has **%s** alternate accounts.\n" +
                                "Of those accounts:\n" +
                                " - **%s** are currently muted.\n" +
                                " - **%s** are currently banned.\n\n" +
                                "To view a full list of alternate account names, please execute **/lookup %s full**.", args.get(0), lookup.getNames().get(0).getName(), (lookup.getNames().size() - 1) + "", lookup.getMuteNumbers() + "", lookup.getBanNumbers() + "",args.get(0))));
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup [username] [full]**"));
            }
        } else if (args.size() == 2) {
            if (args.get(1).equalsIgnoreCase("full")) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Performing full lookup for **%s**...", args.get(0))));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        UUID uuid = UUIDUtil.getUUID(args.get(0));
                        if (uuid == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("Player [**%s**] does not exist.", args.get(0))));
                            return;
                        }

                        IPLookup lookup = AuroraMCAPI.getDbManager().ipLookup(uuid);
                        if (lookup == null) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("No matches found for [**%s**].", args.get(0))));
                            return;
                        }
                        StringBuilder s = new StringBuilder();

                        for (LookupUser user : lookup.getNames()) {
                            s.append((user.isBanned() || user.isMuted()) ? "&c" : "&a").append(user.getName()).append("&r ");
                         }

                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", String.format("IP lookup information for **%s**:\n" +
                                "The first account to join on this IP is **%s**.\n" +
                                "This IP has **%s** alternate accounts.\n" +
                                "Of those accounts:\n" +
                                " - **%s** are currently muted.\n" +
                                " - **%s** are currently banned.\n" +
                                "Alternate account names:\n" +
                                "%s", args.get(0), lookup.getNames().get(0).getName(), (lookup.getNames().size() - 1) + "", lookup.getMuteNumbers() + "", lookup.getBanNumbers() + "", s.toString().trim())));
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup [username] [full]**"));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("IP Lookup", "Invalid syntax. Correct syntax: **/lookup [username] [full]**"));
        }

    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
