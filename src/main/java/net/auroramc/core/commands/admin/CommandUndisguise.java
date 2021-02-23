package net.auroramc.core.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUndisguise extends Command {


    public CommandUndisguise() {
        super("undisguise", Collections.singletonList("ud"), Collections.singletonList(Permission.DISGUISE), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
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

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
