package network.auroramc.core.commands.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.UUIDUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandUndisguise extends Command {


    public CommandUndisguise() {
        super("undisguise", new ArrayList<>(Collections.singletonList("ud")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("disguise"))), false, null);
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
