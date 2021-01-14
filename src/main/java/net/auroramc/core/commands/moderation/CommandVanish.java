package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.events.VanishEvent;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandVanish extends Command {


    public CommandVanish() {
        super("vanish", new ArrayList<>(Collections.singletonList("v")), new ArrayList<>(Collections.singletonList(AuroraMCAPI.getPermissions().get("moderation"))), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        VanishEvent event = new VanishEvent(player, !player.isVanished());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You cannot vanish at this time."));
            return;
        }
        if (!event.isVanish()) {
            player.unvanish();
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You are no longer vanished."));
        } else {
            player.vanish();
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Vanish", "You are now vanished."));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
