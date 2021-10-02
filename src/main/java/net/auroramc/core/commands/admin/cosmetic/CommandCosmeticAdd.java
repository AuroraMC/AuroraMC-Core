package net.auroramc.core.commands.admin.cosmetic;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.command.Command;
import net.auroramc.core.api.cosmetics.Cosmetic;
import net.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandCosmeticAdd extends Command {


    public CommandCosmeticAdd() {
        super("add", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            int cosID;
            try {
                cosID = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic add [player] [id]**"));
                return;
            }
            Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(cosID);
            if (cosmetic == null) {
                player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "That cosmetic doesn't exist. Refer to the cosmetics document for cosmetic ID's."));
                return;
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id == -1) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("No results found for player [**%s**]. Try searching for an old name.", args.get(0))));
                        return;
                    }
                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);
                    if (AuroraMCAPI.getDbManager().hasUnlockedCosmetic(uuid, cosID)) {
                        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("Player [**%s**] already has this cosmetic unlocked.", args.get(0))));
                        return;
                    }

                    AuroraMCAPI.getDbManager().addCosmetic(uuid, cosmetic);
                    player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", String.format("Cosmetic **%s %s** added to player **%s**.", cosmetic.getName(), cosmetic.getType().getName(), args.get(0))));
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("CosmeticAdd");
                    out.writeUTF(uuid.toString());
                    out.writeInt(cosID);
                    player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());
                }
            }.runTaskAsynchronously(AuroraMCAPI.getCore());
        } else {
            player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic add [player] [id]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
