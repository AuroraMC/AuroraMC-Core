package network.auroramc.core.managers;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.events.CommandEngineOverwriteEvent;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements Listener {

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent e) {
        CommandEngineOverwriteEvent event = new CommandEngineOverwriteEvent(e.getMessage(), AuroraMCAPI.getPlayer(e.getPlayer()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        e.setCancelled(true);
        AuroraMCPlayer player = AuroraMCAPI.getPlayer(e.getPlayer());
        ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getMessage().split(" ")));
        String commandLabel = args.remove(0).substring(1);
        onCommand(commandLabel, args, player);
    }

    private static void onCommand(String commandLabel, List<String> args, AuroraMCPlayer player) {
        Command command = AuroraMCAPI.getCommand(commandLabel);
        if (command != null) {
            for (Permission permission : command.getPermission()) {
                if (player.hasPermission(permission.getId())) {
                    command.execute(player, commandLabel, args);
                    return;
                }
            }

            if (command.showPermissionMessage()) {
                if (command.getCustomPermissionMessage() != null) {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", command.getCustomPermissionMessage()));
                } else {
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "You do not have permission to use that command!"));
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised."));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised."));
        }
    }

}
