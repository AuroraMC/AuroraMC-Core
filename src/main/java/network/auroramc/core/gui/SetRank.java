package network.auroramc.core.gui;

import network.auroramc.core.api.AuroraMCAPI;
import network.auroramc.core.api.permissions.Rank;
import network.auroramc.core.api.permissions.SubRank;
import network.auroramc.core.api.players.AuroraMCPlayer;
import network.auroramc.core.api.utils.gui.GUI;
import network.auroramc.core.api.utils.gui.GUIItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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

        //Generate GUI
        Map<Integer, Rank> ranks = AuroraMCAPI.getRanks();

        //To make sure I sort them by ID, so appear correctly in the GUI.
        List<Integer> rankIDs = new ArrayList<>(ranks.keySet());
        Collections.sort(rankIDs);

        switch (variation) {
            case FULL:

                for (int i : rankIDs) {
                    Rank rank = ranks.get(i);
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
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
                        if (rank.getCategory() == Rank.RankCategory.SOCIAL_MEDIA) {
                            this.setItem(socialCat, 2, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", rank.getPrefixColor(), rank.getName()), 1, String.format("&r&7Set %s's rank to %s;;&r&7ID: &b%s", name, rank.getPrefixAppearance(), rank.getId()), (short) 0, rank.getId() == currentRank.getId(), rank.getColor()));
                            socialCat++;
                        }
                    }
                }
                break;
            case SUPPORT:
                //Generate GUI
                for (int i : rankIDs) {
                    Rank rank = ranks.get(i);
                    if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                        this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                        playerCat++;
                    }
                }
                break;
            case BTM:
                if (currentRank.getCategory() == Rank.RankCategory.CONTENT_CREATOR) {
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
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
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
                        if (rank.getCategory() == Rank.RankCategory.PLAYER) {
                            this.setItem(playerCat, 0, new GUIItem(Material.LEATHER_CHESTPLATE, String.format("&%s&l%s", ((rank.getPrefixColor()==null)?'f':rank.getPrefixColor()), rank.getName()), 1, String.format("&rSet %s's rank to %s;;&r&7ID: &b%s", name, ((rank.getPrefixAppearance()==null)?"Player":rank.getPrefixAppearance()), rank.getId()), (short)0, rank.getId() == currentRank.getId(), rank.getColor()));
                            playerCat++;
                        }
                    }
                } else {
                    for (int i : rankIDs) {
                        Rank rank = ranks.get(i);
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
        Rank rank = AuroraMCAPI.getRanks().get(rankId);
        player.getPlayer().closeInventory();
        player.getPlayer().sendMessage(AuroraMCAPI.getFormatter().pluginMessage("SetRank", String.format("You set **%s's** rank to **%s**.", name, rank.getName())));
        AuroraMCPlayer setter = this.player;


        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            if (player.isOnline()) {
                player.sendMessage(AuroraMCAPI.getFormatter().pluginMessage("Permissions", String.format("Your rank was set to **%s**.", rank.getName())));
                AuroraMCAPI.getPlayer(player).setRank(rank);
                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    AuroraMCPlayer otherAMCPlayer = AuroraMCAPI.getPlayer(otherPlayer);
                    if (otherAMCPlayer != null) {
                        otherAMCPlayer.updateNametag(AuroraMCAPI.getPlayer(player));
                    }
                }
            }
        }

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
            }
        }.runTaskAsynchronously(AuroraMCAPI.getCore());
    }
}
