/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.gui.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.permissions.Rank;
import net.auroramc.core.api.permissions.SubRank;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.Disguise;
import net.auroramc.core.api.utils.DiscordWebhook;
import net.auroramc.core.api.utils.gui.GUI;
import net.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class SetRank extends GUI {

    public enum SetRankVariation {FULL, SOCIAL_MEDIA, SUPPORT, BTM, STM}

    private final AuroraMCPlayer player;
    private final String name;
    private final int id;
    private final UUID uuid;
    private final Rank currentRank;
    private final SetRankVariation variation;

    public SetRank(AuroraMCPlayer player, String name, UUID uuid, int id, Rank currentRank, SetRankVariation variation) {
        super(String.format("&3&lSet %s's Rank", name) ,5, true);

        this.player = player;
        this.name = name;
        this.id = id;
        this.uuid = uuid;
        this.currentRank = currentRank;
        this.variation = variation;

        this.setItem(0, 4, new GUIItem(Material.SKULL_ITEM, String.format("&3&lSet %s's Rank", name), 1, String.format("&rCurrent Rank: &b%s", currentRank.getName()), (short)3, false, name));

        int playerCat = 2, socialCat = 2, contentCat = 2, moderationCat = 2, leadershipCat = 2;
        this.setItem(1, 0, new GUIItem(Material.WOOL, "&f&lPlayer Ranks", 1, "&r&7Player/Premium Ranks.", (short)0));
        this.setItem(1, 2, new GUIItem(Material.WOOL, "&5&lSocial Media Ranks", 1, "&r&7Ranks for Social Media partners.", (short)10));
        this.setItem(1, 4, new GUIItem(Material.WOOL, "&a&lContent Creator Ranks", 1, "&r&7Server Content Creators.", (short)5));
        this.setItem(1, 6, new GUIItem(Material.WOOL, "&9&lModeration Staff Ranks", 1, "&r&7Moderation Staff Ranks.", (short)11));
        this.setItem(1, 8, new GUIItem(Material.WOOL, "&c&lLeadership Ranks", 1, "&r&7Leadership Ranks.", (short)14));

        if (player.getRank().hasPermission("admin")) {
            this.setItem(5, 8, new GUIItem(Material.ARROW, String.format("&3&lSet %s's Subranks", name)));
        }

        Rank[] ranks = Rank.values();

        switch (variation) {
            case FULL:

                for (Rank rank : ranks) {
                    switch (rank.getCategory()) {
                        case PLAYER:
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                            break;
                        case SOCIAL_MEDIA:
                            this.setItem(socialCat, 2, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            socialCat++;
                            break;
                        case CONTENT_CREATOR:
                            if (rank.getId() != player.getRank().getId() && rank.getId() < player.getRank().getId()) {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            } else {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &b%s", rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            }
                            contentCat++;
                            break;
                        case MODERATION:
                            this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            moderationCat++;
                            break;
                        case LEADERSHIP:
                            if (rank.getId() != player.getRank().getId() && rank.getId() < player.getRank().getId() && player.getRank().hasPermission("admin")) {
                                this.setItem(leadershipCat, 8, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            } else {
                                this.setItem(leadershipCat, 8, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &b%s", rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            }
                            leadershipCat++;
                            break;
                    }
                }
                break;
            case SOCIAL_MEDIA:
                //Generate GUIs
                if (currentRank.getCategory() == Rank.RankCategory.SOCIAL_MEDIA) {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.SOCIAL_MEDIA) {
                            this.setItem(socialCat, 2, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            socialCat++;
                        }
                    }
                }
                break;
            case SUPPORT:
                //Generate GUI
                for (Rank rank : ranks) {
                    if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                        this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                        playerCat++;
                    }
                }
                break;
            case BTM:
                if (currentRank.getCategory() == Rank.RankCategory.CONTENT_CREATOR) {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.CONTENT_CREATOR) {
                            if (rank.getId() < player.getRank().getId()) {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &a%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            } else {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &a%s", rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            }

                            contentCat++;
                        }
                    }
                }
                break;
            case STM:
                if (currentRank.getCategory() == Rank.RankCategory.MODERATION) {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.MODERATION) {
                            if (rank.getId() < player.getRank().getId()) {
                                this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &a%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            } else {
                                this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&4&lYou do not have permission to set this rank.;;&r&7ID: &a%s", rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            }
                            moderationCat++;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(int row, int column, ItemStack item, ClickType clickType) {
        if (item.getType() != Material.LEATHER_CHESTPLATE) {
            if (item.getType() == Material.ARROW) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        List<SubRank> subRanks = AuroraMCAPI.getDbManager().getSubRanks(id);
                        SetSubRank subRank = new SetSubRank(player, name, uuid, id, subRanks);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                subRank.open(player);
                                AuroraMCAPI.closeGUI(player);
                                AuroraMCAPI.openGUI(player, subRank);
                            }
                        }.runTask(AuroraMCAPI.getCore());
                    }
                }.runTaskAsynchronously(AuroraMCAPI.getCore());
                return;
            }
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ITEM_BREAK, 100, 0);
            return;
        }

        List<String> lore = item.getItemMeta().getLore();

        for (String s : lore) {
            if (ChatColor.stripColor(s).contains("You do not have permission to set this rank.")) {
                return;
            }
        }
        String sid = lore.get(lore.size() - 1);
        sid = ChatColor.stripColor(sid.split(" ")[1]);
        int rankId = Integer.parseInt(sid);
        Rank rank = Rank.getByID(rankId);
        assert rank != null;
        player.getPlayer().closeInventory();
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You set **%s's** rank to **%s**.", name, rank.getName())));
        AuroraMCPlayer setter = this.player;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SetRank");
        out.writeUTF(uuid.toString());
        out.writeInt(rankId);
        this.player.getPlayer().sendPluginMessage(AuroraMCAPI.getCore(), "BungeeCord", out.toByteArray());

        new BukkitRunnable() {
            @Override
            public void run() {
                AuroraMCAPI.getDbManager().setRank(id, rank, currentRank);
                if (variation == SetRankVariation.STM) {
                    if (rank.hasPermission("moderation")) {
                        AuroraMCAPI.getDbManager().addMentee(setter.getId(), id);
                    }
                }
                if (currentRank.getId() == 9) {
                    AuroraMCAPI.getDbManager().removeMentee(id);
                }
                if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                    Disguise disguise = AuroraMCAPI.getDbManager().getDisguise(uuid.toString());
                    if (disguise != null) {
                        AuroraMCAPI.getDbManager().undisguise(uuid.toString(), disguise.getName());
                        AuroraMCAPI.getDbManager().unvanish(uuid.toString());
                    }

                }

                DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/928785758599405680/gnzddoyMeiq9wMKxkKVRptVx3PX8EfGeRtFz9ZuXlpXGJV1G37l_g-KeJgLJIX5WNIUF");

                discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Rank Set").setDescription(String.format("**%s** has set **%s's** rank as **%s**.", player.getName(), name, rank.name())).setColor(new Color(rank.getColor().asRGB())));
                try {
                    discordWebhook.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }
}
