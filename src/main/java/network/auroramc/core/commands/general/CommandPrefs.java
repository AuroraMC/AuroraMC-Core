package network.auroramc.core.commands.general;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.gui.preferences.Preferences;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPrefs extends Command {
    public CommandPrefs() {
        super("prefs", new ArrayList<>(Collections.singletonList("preferences")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("player"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        Preferences prefs = new Preferences(player);
        prefs.open(player);
        AuroraMCAPI.openGUI(player, prefs);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
