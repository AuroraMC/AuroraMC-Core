package network.auroramc.core.commands;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.command.Command;
import network.auroramc.core.api.permissions.Permission;
import network.auroramc.core.api.players.AuroraMCPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLink extends Command {


    public CommandLink() {
        super("link", new ArrayList<>(Arrays.asList("discordlink","discord")), Collections.singletonList(AuroraMCAPI.getPermissions().get("player")), true, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (player.getLinkedDiscord() == null) {
            if (!player.isDiscordCodeGenerated()) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if (AuroraMCAPI.getDbManager().getDiscord(player.getId()) == null) {
                            String code = RandomStringUtils.randomAlphanumeric(8);
                            while (AuroraMCAPI.getDbManager().codeExists(code)) {
                                code = RandomStringUtils.randomAlphanumeric(8);
                            }
                            AuroraMCAPI.getDbManager().newCode(code, player);
                            player.codeGenerated();
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", String.format("Code generated: **%s**! In order to link your in-game account to your Discord, all you have to do is do **!link %s**. This code only lasts 60 seconds!", code, code)));
                        } else {
                            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
                        }
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You already have a code active. Please use that code of wait till it expires and run this command again."));
            }
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Discord", "You are already linked with a Discord account! In order to prevent abuse, you cannot unlink your Discord and in-game accounts yourself. Please contact our customer support who can help you further."));
        }
    }
}
