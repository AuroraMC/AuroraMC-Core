package net.auroramc.core.commands.general.ignore;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.IgnoredPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandIgnoreRemove extends Command {

    public CommandIgnoreRemove() {
        super("remove", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            if (player.getName().equalsIgnoreCase(args.get(0)) || player.getPlayer().getName().equalsIgnoreCase(args.get(0))) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "You cannot ignore yourself, silly!"));
                return;
            }
            if (!player.isIgnored(args.get(0))) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "You do not have that player ignored."));
                return;
            }
            player.removeIgnored(player.getIgnored(args.get(0)));
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", String.format("**%s** removed from your ignore list.", args.get(0))));
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Ignore", "Invalid syntax. Correct syntax: **/ignore remove [player]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
