package net.auroramc.core.commands.general.ignore;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnore extends Command {


    public CommandIgnore() {
        super("ignore", Collections.emptyList(), Collections.singletonList(Permission.PLAYER), false, null);
        this.registerSubcommand("add", new ArrayList<>(), new CommandIgnoreAdd());
        this.registerSubcommand("remove", new ArrayList<>(), new CommandIgnoreRemove());
        this.registerSubcommand("list", new ArrayList<>(), new CommandIgnoreList());
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 0) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "Available commands:\n" +
                    "**/ignore add [name]** - Add a user to your ignore list.\n" +
                    "**/ignore remove [name]** - Remove a user from your ignore list\n" +
                    "**/ignore list [page]** - List all users in your ignore list."));
            return;
        }
        switch (args.get(0).toLowerCase()) {
            case "add":
            case "remove":
            case "list":
                aliasUsed = args.remove(0).toLowerCase();
                subcommands.get(aliasUsed).execute(player, aliasUsed, args);
                break;
            default:
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "Available commands:\n" +
                        "**/ignore add [name]** - Add a user to your ignore list.\n" +
                        "**/ignore remove [name]** - Remove a user from your ignore list\n" +
                        "**/ignore list [page]** - List all users in your ignore list."));
                break;
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
