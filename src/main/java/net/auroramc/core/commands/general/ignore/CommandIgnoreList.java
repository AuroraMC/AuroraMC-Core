package net.auroramc.core.commands.general.ignore;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandIgnoreList extends Command {

    public CommandIgnoreList() {
        super("list", new ArrayList<>(), new ArrayList<>(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            int page;
            try {
                page = Integer.parseInt(args.get(0));
            } catch (NumberFormatException ignored) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore list <page>**"));
                return;
            }

            if (page < 1) {
                page = 1;
            }

            int totalPages = (player.getIgnoredPlayers().size() / 7) + ((player.getIgnoredPlayers().size() % 7 > 0)?1:0);
            if (page > totalPages) {
                page = totalPages;
            }

            player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatIgnoreList(player, page));
        } else {
            player.getPlayer().spigot().sendMessage(AuroraMCAPI.getFormatter().formatIgnoreList(player, 1));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
