package net.auroramc.core.commands.moderation;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.ChatSlowLength;
import net.auroramc.core.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandChatSlow extends Command {


    public CommandChatSlow() {
        super("chatslow", Collections.singletonList("slow"), Arrays.asList(Permission.ADMIN, Permission.EVENT_MANAGEMENT, Permission.SOCIAL_MEDIA, Permission.STAFF_MANAGEMENT), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 1) {
            short amount;
            try {
                amount = Short.parseShort(args.get(0));
            } catch (NumberFormatException e) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            if (amount < 1 || amount > 300) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", "That is not a valid amount of seconds. The number must be between 1-300."));
                return;
            }

            AuroraMCAPI.setChatSlow(amount);
            ChatSlowLength length = new ChatSlowLength(amount);
            for (AuroraMCPlayer player2 : AuroraMCAPI.getPlayers()) {
                if (player2.hasPermission("moderation") || player2.hasPermission("social") ||  player2.hasPermission("debug.info")) {
                    player2.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", String.format("A chat slow of **%s** was enabled on this server. The goose has granted you immunity from it because of your rank!", length.getFormatted())));
                } else {
                    player2.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", String.format("A Moderator has enabled a chat slow on this server. You may now only chat every **%s**.", length.getFormatted())));
                }
            }
        } else {
            if (AuroraMCAPI.getChatSlow() != -1) {
                AuroraMCAPI.setChatSlow((short) -1);
                for (AuroraMCPlayer player2 : AuroraMCAPI.getPlayers()) {
                    player2.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", "The active chat slow in this server has been disabled."));
                }
            } else {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("ChatSlow", "There is currently no chat slow active. To set a chat slow, use **/chatslow [seconds]**."));
            }
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
