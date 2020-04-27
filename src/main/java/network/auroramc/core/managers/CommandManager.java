package network.auroramc.core.managers;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.backend.Cache;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.CoreCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements Listener {

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(true);
        Cache cache = AuroraMCAPI.getCache("AuroraMC-Core");
        if (cache instanceof CoreCache) {
            CoreCache coreCache = (CoreCache) cache;
            AuroraMCPlayer player = coreCache.getPlayer(e.getPlayer());
            ArrayList<String> args = new ArrayList<>(Arrays.asList(e.getMessage().split(" ")));
            String commandLabel = args.remove(0);
            onCommand(commandLabel, args, player);
        }
    }

    private static void onCommand(String commandLabel, List<String> args, AuroraMCPlayer player) {
        Cache cache = AuroraMCAPI.getCache("AuroraMC-Core");
        if (cache instanceof CoreCache) {
            CoreCache coreCache = (CoreCache) cache;
            Command command = coreCache.getCommand(commandLabel);
            if (command != null) {
                if (player.hasPermission(command.getPermission().getId())) {
                    command.execute(player, commandLabel, args);
                } else {
                    if (command.showPermissionMessage()) {
                        if (command.getCustomPermissionMessage() != null) {
                            AuroraMCAPI.getFormatter().pluginMessage("Command Manager", command.getCustomPermissionMessage());
                        } else {
                            AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "You do not have permission to use that command!");
                        }
                    } else {
                        AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised.");
                    }
                }
            } else {
                AuroraMCAPI.getFormatter().pluginMessage("Command Manager", "That command is unrecognised.");
            }
        }
    }

}
