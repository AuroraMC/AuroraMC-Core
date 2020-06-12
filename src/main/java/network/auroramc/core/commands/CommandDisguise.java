package network.auroramc.core.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandDisguise extends Command {


    public CommandDisguise() {
        super("disguise", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("disguise"))), false, null);
    }

    //TODO: Disguise filter
    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.size()) {
                case 1:
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (player.disguise(args.get(0), args.get(0), AuroraMCAPI.getRanks().get(0))) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s**.", args.get(0))));
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Disguise");
                            out.writeUTF(player.getName());
                            out.writeUTF(args.get(0));
                            out.writeUTF(args.get(0));
                            out.writeInt(AuroraMCAPI.getRanks().get(0).getId());
                            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Something went wrong when attempting to disguise. Maybe the disguise skin doesn't exist?"));
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin]**"));
                    }
                    break;
                case 2:
                    //Set skin and name seperately.
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}") && args.get(1).matches("[a-zA-Z0-9_]{3,16}")) {
                        if (player.disguise(args.get(1), args.get(0), AuroraMCAPI.getRanks().get(0))) {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**.", args.get(0), args.get(1))));
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Disguise");
                            out.writeUTF(player.getName());
                            out.writeUTF(args.get(0));
                            out.writeUTF(args.get(1));
                            out.writeInt(AuroraMCAPI.getRanks().get(0).getId());
                            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Something went wrong when attempting to disguise. Maybe the disguise skin doesn't exist?"));
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin]**"));
                    }
                    break;
                case 3:
                    //Set skin and name seperately.
                    if (args.get(0).matches("[a-zA-Z0-9_]{3,16}") && args.get(1).matches("[a-zA-Z0-9_]{3,16}") && args.get(2).matches("[a-zA-Z]{3,16}")) {
                        Rank chosenRank = null;
                        for (Rank rank : AuroraMCAPI.getRanks().values()) {
                            if (rank.getName().equalsIgnoreCase(args.get(2))) {
                                if (rank.hasPermission("moderation") || rank.hasPermission("disguise") || rank.hasPermission("debug.info")) {
                                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot disguise as that rank, you may only choose between premium ranks."));
                                    return;
                                }
                                chosenRank = rank;
                            }
                        }
                        if (chosenRank ==  null) {
                            chosenRank = AuroraMCAPI.getRanks().get(0);
                        }
                        if (player.disguise(args.get(1), args.get(0), chosenRank)) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Disguise");
                            out.writeUTF(player.getName());
                            out.writeUTF(args.get(0));
                            out.writeUTF(args.get(1));
                            out.writeInt(chosenRank.getId());
                            player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", String.format("You are now disguised as **%s** with the skin of **%s**.", args.get(0), args.get(1))));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Something went wrong when attempting to disguise. Maybe the disguise skin doesn't exist?"));
                        }
                    } else {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin]**"));
                    }
                    break;
                default:
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "Invalid syntax. Correct syntax: **/disguise <user> [skin] [rank]**"));
                    break;
            }
        } else {
            //Undisguise.
            if (player.getActiveDisguise() != null) {
                player.undisguise();
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You have been undisguised."));
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("UnDisguise");
                out.writeUTF(player.getName());
                player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Disguise", "You cannot undisguise when you are not disguised."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
