package network.auroramc.core.commands.general;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.gui.stats.achievements.Achievements;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandAchievements extends Command {
    public CommandAchievements() {
        super("achievements", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("player"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (player.getStats() != null) {
            Achievements achievements = new Achievements(player);
            achievements.open(player);
            AuroraMCAPI.openGUI(player, achievements);
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Statistics", "Your statistics are still loading, please wait to use this command!"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
