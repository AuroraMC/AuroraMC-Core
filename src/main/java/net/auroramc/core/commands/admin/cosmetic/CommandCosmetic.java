package net.auroramc.core.commands.admin.cosmetic;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.permissions.Permission;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandCosmetic extends Command {

    public CommandCosmetic() {
        super("cosmetic", Collections.emptyList(), Arrays.asList(Permission.ADMIN, Permission.SUPPORT), false, null);
        subcommands.put("add", new CommandCosmeticAdd());
        subcommands.put("remove", new CommandCosmeticRemove());
        subcommands.put("list", new CommandCosmeticList());
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() > 0) {
            switch (args.get(0).toLowerCase()) {
                case "add":
                case "remove":
                case "list":
                    aliasUsed = args.remove(0).toLowerCase();
                    subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                    break;
                default:
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "Available commands:\n" +
                            "**/cosmetic add [player] [id]** - Adds a cosmetic to a players account.\n" +
                            "**/cosmetic remove [player] [id]** - Remove a player to a players account.\n" +
                            "**/cosmetic list [player]** - List all cosmetics a user has."));
                    break;
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "Available commands:\n" +
                    "&b/cosmetic add [player] [id]&r - Adds a cosmetic to a players account.\n" +
                    "&b/cosmetic remove [player] [id]&r - Remove a player to a players account.\n" +
                    "&b/cosmetic list [player]&r - List all cosmetics a user has."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        ArrayList<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            for (String s : subcommands.keySet()){
                if (s.startsWith(lastToken)) {
                    completions.add(s);
                }
            }
        } else if (numberArguments == 2) {
            if (args.get(0).equalsIgnoreCase("add") || args.get(0).equalsIgnoreCase("remove")) {
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    if (player1.getName().toLowerCase().startsWith(lastToken.toLowerCase())) {
                        completions.add(player1.getName());
                    }
                }
            }
        }
        return completions;
    }
}
