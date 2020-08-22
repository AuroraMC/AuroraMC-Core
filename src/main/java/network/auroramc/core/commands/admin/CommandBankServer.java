package network.auroramc.core.commands.admin;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBankServer extends Command {
    public CommandBankServer() {
        super("bankserver", new ArrayList<>(), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("admin"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Economy", String.format("Your current server Economy details:\n" +
                "Tickets: **%s**\n" +
                "Crowns: **%s**", player.getBank().getTickets(), player.getBank().getCrowns())));
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
