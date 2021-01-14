package net.auroramc.core.commands.general;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.gui.PlusColour;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandPlus extends Command {


    public CommandPlus() {
        super("plus", new ArrayList<>(Arrays.asList("pluscolor", "pluscolour")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("plus"))), true, "You must have an active Plus subscription in order to use this command!");
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        PlusColour plusColour = new PlusColour(player);
        plusColour.open(player);
        AuroraMCAPI.openGUI(player, plusColour);
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
