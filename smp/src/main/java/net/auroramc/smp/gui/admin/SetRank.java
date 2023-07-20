/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.smp.gui.admin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.api.player.Disguise;
import net.auroramc.api.utils.DiscordWebhook;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.smp.api.ServerAPI;
import net.auroramc.smp.api.player.AuroraMCServerPlayer;
import net.auroramc.smp.api.utils.gui.GUI;
import net.auroramc.smp.api.utils.gui.GUIItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class SetRank extends GUI {

    public enum SetRankVariation {FULL, SOCIAL_MEDIA, SUPPORT, BTM, STM}

    private final AuroraMCServerPlayer player;
    private final String name;
    private final int id;
    private final UUID uuid;
    private final Rank currentRank;
    private final SetRankVariation variation;

    public SetRank(AuroraMCServerPlayer player, String name, UUID uuid, int id, Rank currentRank, SetRankVariation variation) {
        super(String.format("&3&lSet %s's Rank", name) ,5, true);

        this.player = player;
        this.name = name;
        this.id = id;
        this.uuid = uuid;
        this.currentRank = currentRank;
        this.variation = variation;

        this.setItem(0, 4, new GUIItem(Material.PLAYER_HEAD, String.format("&3&lSet %s's Rank", name), 1, String.format("&r&fCurrent Rank: &b%s", currentRank.getName()), (short)0, false, name));

        int playerCat = 2, socialCat = 2, contentCat = 2, moderationCat = 2, leadershipCat = 2;
        this.setItem(1, 0, new GUIItem(Material.WHITE_WOOL, "&f&lPlayer Ranks", 1, "&r&7Player/Premium Ranks."));
        this.setItem(1, 2, new GUIItem(Material.PURPLE_WOOL, "&5&lSocial Media Ranks", 1, "&r&7Ranks for Social Media partners."));
        this.setItem(1, 4, new GUIItem(Material.LIME_WOOL, "&a&lContent Creator Ranks", 1, "&r&7Server Content Creators."));
        this.setItem(1, 6, new GUIItem(Material.BLUE_WOOL, "&9&lModeration Staff Ranks", 1, "&r&7Moderation Staff Ranks."));
        this.setItem(1, 8, new GUIItem(Material.RED_WOOL, "&c&lLeadership Ranks", 1, "&r&7Leadership Ranks."));

        if (player.getRank().hasPermission("admin")) {
            this.setItem(5, 8, new GUIItem(Material.ARROW, String.format("&3&lSet %s's Subranks", name)));
        }

        Rank[] ranks = Rank.values();

        switch (variation) {
            case FULL:

                for (Rank rank : ranks) {
                    switch (rank.getCategory()) {
                        case PLAYER:
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", ((rank.getPrefixColor()==null)?"§f":rank.getPrefixColor()), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            playerCat++;
                            break;
                        case SOCIAL_MEDIA:
                            this.setItem(socialCat, 2, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            socialCat++;
                            break;
                        case CONTENT_CREATOR:
                            if (rank.getId() != player.getRank().getId() && rank.getId() < player.getRank().getId()) {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            } else {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &b%s", rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            }
                            contentCat++;
                            break;
                        case MODERATION:
                            this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            moderationCat++;
                            break;
                        case LEADERSHIP:
                            if (rank.getId() != player.getRank().getId() && rank.getId() < player.getRank().getId() && player.getRank().hasPermission("admin")) {
                                this.setItem(leadershipCat, 8, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            } else {
                                this.setItem(leadershipCat, 8, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &b%s", rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
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
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.SOCIAL_MEDIA) {
                            this.setItem(socialCat, 2, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            socialCat++;
                        }
                    }
                }
                break;
            case SUPPORT:
                //Generate GUI
                for (Rank rank : ranks) {
                    if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                        this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                        playerCat++;
                    }
                }
                break;
            case BTM:
                if (currentRank.getCategory() == Rank.RankCategory.CONTENT_CREATOR) {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.CONTENT_CREATOR) {
                            if (rank.getId() < player.getRank().getId()) {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &a%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            } else {
                                this.setItem(contentCat, 4, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&4&lYou do not have permission to set this rank.;;&r&7ID: &a%s", rank.getId()), (short) 0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
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
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&r&fSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            playerCat++;
                        }
                    }
                } else {
                    for (Rank rank : ranks) {
                        if (rank.getCategory() == Rank.RankCategory.MODERATION) {
                            if (rank.getId() < player.getRank().getId()) {
                                this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &a%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
                            } else {
                                this.setItem(moderationCat, 6, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&4&lYou do not have permission to set this rank.;;&r&7ID: &a%s", rank.getId()), (short) 0, rank.getId() == currentRank.getId(), Color.fromRGB(rank.getR(), rank.getG(), rank.getB())));
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
                            }
                        }.runTask(ServerAPI.getCore());
                    }
                }.runTaskAsynchronously(ServerAPI.getCore());
                return;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 0);
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
        player.closeInventory();
        player.sendMessage(TextFormatter.pluginMessage("SetRank", String.format("You set **%s's** rank to **%s**.", name, rank.getName())));
        AuroraMCServerPlayer setter = this.player;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SetRank");
        out.writeUTF(uuid.toString());
        out.writeInt(rankId);
        this.player.sendPluginMessage(out.toByteArray());

        if (!AuroraMCAPI.isTestServer()) {
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

                    DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/929016334912733205/bFwMdYwk1mI2adr1aubBW3aUDEHcWViNUsfOa_5GrD9KVT2ijI3N5NHKesknQuJNW1H1");

                    discordWebhook.addEmbed(new DiscordWebhook.EmbedObject().setTitle("Rank Set").setDescription(String.format("**%s** has set **%s's** rank as **%s**.", player.getName(), name, rank.name())).setColor(new java.awt.Color(Color.fromRGB(Color.fromRGB(rank.getR(), rank.getG(), rank.getB()).asRGB()).asRGB())));
                    try {
                        discordWebhook.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(ServerAPI.getCore());
        }
    }
}
