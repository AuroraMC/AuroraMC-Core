/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.core.commands.admin.cosmetic;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.core.api.ServerAPI;
import net.auroramc.core.api.ServerCommand;
import net.auroramc.core.api.player.AuroraMCServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandCosmeticAdd extends ServerCommand {


    public CommandCosmeticAdd() {
        super("add", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCServerPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 2) {
            int cosID;
            try {
                cosID = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic add [player] [id]**"));
                return;
            }
            Cosmetic cosmetic = AuroraMCAPI.getCosmetics().get(cosID);
            if (cosmetic == null) {
                player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "That cosmetic doesn't exist. Refer to the cosmetics document for cosmetic ID's."));
                return;
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    int id = AuroraMCAPI.getDbManager().getAuroraMCID(args.get(0));
                    if (id == -1) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("No results found for player [**%s**]. Try searching for an old name.", args.get(0))));
                        return;
                    }
                    UUID uuid = AuroraMCAPI.getDbManager().getUUIDFromID(id);
                    if (AuroraMCAPI.getDbManager().hasUnlockedCosmetic(uuid, cosID)) {
                        player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("Player [**%s**] already has this cosmetic unlocked.", args.get(0))));
                        return;
                    }

                    if (!AuroraMCAPI.isTestServer()) {
                        AuroraMCAPI.getDbManager().addCosmetic(uuid, cosmetic);
                    }
                    player.sendMessage(TextFormatter.pluginMessage("Cosmetics", String.format("Cosmetic **%s %s** added to player **%s**.", cosmetic.getName(), cosmetic.getType().getName(), args.get(0))));
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("CosmeticAdd");
                    out.writeUTF(uuid.toString());
                    out.writeInt(cosID);
                    player.sendPluginMessage(out.toByteArray());
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Cosmetics", "Invalid syntax. Correct syntax: **/cosmetic add [player] [id]**"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCServerPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        return new ArrayList<>();
    }
}
