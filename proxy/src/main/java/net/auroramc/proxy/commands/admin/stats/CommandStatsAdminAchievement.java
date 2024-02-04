/*
 * Copyright (c) 2021-2024 Ethan P-B. All Rights Reserved.
 */

package net.auroramc.proxy.commands.admin.stats;


import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.stats.Achievement;
import net.auroramc.api.stats.PlayerStatistics;
import net.auroramc.api.stats.TieredAcheivement;
import net.auroramc.api.utils.TextFormatter;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.ProxyCommand;
import net.auroramc.proxy.api.backend.communication.CommunicationUtils;
import net.auroramc.proxy.api.backend.communication.Protocol;
import net.auroramc.proxy.api.backend.communication.ProtocolMessage;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CommandStatsAdminAchievement extends ProxyCommand {
    public CommandStatsAdminAchievement() {
        super("achievement", Collections.emptyList(), Collections.emptyList(), false, null);
    }

    @Override
    public void execute(AuroraMCProxyPlayer player, String aliasUsed, List<String> args) {
        if (args.size() == 3 || args.size() == 4) {
            if (args.get(0).equalsIgnoreCase("add")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user does not exist."));
                        return;
                    }

                    int achievementId;
                    try {
                        achievementId = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid achievement ID. Please refer to the achievements spreadsheet for a list of achievements."));
                        return;
                    }

                    Achievement achievement = AuroraMCAPI.getAchievement(achievementId);

                    if (achievement == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid achievement ID. Please refer to the achievements spreadsheet for a list of achievements."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot manage statistics of someone who has not joined the network."));
                        return;
                    }

                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, amcId);

                    if (stats.getAchievementsGained().containsKey(achievement)) {
                        if (args.size() == 4) {
                            if (achievement instanceof TieredAcheivement) {
                                int tier;

                                try {
                                    tier = Integer.parseInt(args.get(3));
                                } catch (NumberFormatException e) {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                    return;
                                }

                                if (tier > 0 && tier <= ((TieredAcheivement)achievement).getTiers().size()) {
                                    if (stats.getAchievementsGained().get(achievement) < tier) {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                        if (target != null) {
                                            ProxyAPI.getPlayer(target).getStats().achievementGained(achievement, tier, true);
                                        } else {
                                            if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_achievement", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId + "\n" + tier);
                                                CommunicationUtils.sendMessage(message);
                                            } else {
                                                AuroraMCAPI.getDbManager().achievementGet(uuid, achievement, tier);
                                            }

                                        }
                                        player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s Tier %s** added to **%s**", achievement.getName(), tier, args.get(1))));
                                    } else {
                                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user has already achieved that tier."));
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot give tiers of a non-tiered achievement."));
                            }
                        } else {
                            player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user already has that achievement."));
                        }
                    } else {
                        if (args.size() == 4) {
                            if (achievement instanceof TieredAcheivement) {
                                int tier;

                                try {
                                    tier = Integer.parseInt(args.get(3));
                                } catch (NumberFormatException e) {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                    return;
                                }

                                if (tier > 0 && tier <= ((TieredAcheivement)achievement).getTiers().size()) {
                                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                    if (target != null) {
                                        ProxyAPI.getPlayer(target).getStats().achievementGained(achievement, tier, true);
                                    } else {
                                        if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                            ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_achievement", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId + "\n" + tier);
                                            CommunicationUtils.sendMessage(message);
                                        } else {
                                            AuroraMCAPI.getDbManager().achievementGet(uuid, achievement, tier);
                                        }
                                    }


                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s Tier %s** added to **%s**", achievement.getName(), tier, args.get(1))));
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                }
                            } else {
                                player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot give tiers of a non-tiered achievement."));
                            }
                        } else {
                            if (achievement instanceof TieredAcheivement) {
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                if (target != null) {
                                    ProxyAPI.getPlayer(target).getStats().achievementGained(achievement, 1, true);
                                } else {
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_achievement", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId + "\n1");
                                        CommunicationUtils.sendMessage(message);
                                    } else {
                                        AuroraMCAPI.getDbManager().achievementGet(uuid, achievement, 1);
                                    }
                                }

                                player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s Tier %s** added to **%s**", achievement.getName(), 1, args.get(1))));
                            } else {
                                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                if (target != null) {
                                    ProxyAPI.getPlayer(target).getStats().achievementGained(achievement, 1, true);
                                } else {
                                    if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                        ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_achievement", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId + "\n" + 1);
                                        CommunicationUtils.sendMessage(message);
                                    } else {
                                        AuroraMCAPI.getDbManager().achievementGet(uuid, achievement, 1);
                                    }
                                }

                                player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s** added to **%s**", achievement.getName(), args.get(1))));
                            }
                        }
                    }
                });
            } else if (args.get(0).equalsIgnoreCase("remove")) {
                ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> {
                    UUID uuid = AuroraMCAPI.getDbManager().getUUID(args.get(1));

                    if (uuid == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user does not exist."));
                        return;
                    }

                    int achievementId;
                    try {
                        achievementId = Integer.parseInt(args.get(2));
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid achievement ID. Please refer to the achievements spreadsheet for a list of achievements."));
                        return;
                    }

                    Achievement achievement = AuroraMCAPI.getAchievement(achievementId);

                    if (achievement == null) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid achievement ID. Please refer to the achievements spreadsheet for a list of achievements."));
                        return;
                    }

                    int amcId = AuroraMCAPI.getDbManager().getAuroraMCID(uuid);

                    if (amcId < 0) {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot manage statistics of someone who has not joined the network."));
                        return;
                    }

                    PlayerStatistics stats = AuroraMCAPI.getDbManager().getStatistics(uuid, amcId);

                    if (stats.getAchievementsGained().containsKey(achievement)) {
                        if (args.size() == 4) {
                            if (achievement instanceof TieredAcheivement) {
                                int tier;

                                try {
                                    tier = Integer.parseInt(args.get(3));
                                } catch (NumberFormatException e) {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                    return;
                                }

                                if (tier > 0 && tier <= ((TieredAcheivement)achievement).getTiers().size()) {
                                    if (stats.getAchievementsGained().get(achievement) < tier) {
                                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user has not achieved that tier."));
                                    } else {
                                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                                        if (tier - 1 == 0) {
                                            if (target != null) {
                                                ProxyAPI.getPlayer(target).getStats().achievementRemoved(achievement, true);
                                                ProxyAPI.getPlayer(target).getStats().setAchievementProgress(achievement, 0, true);
                                            } else {
                                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove_achievement_reset", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId);
                                                    CommunicationUtils.sendMessage(message);
                                                }
                                            }
                                            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> AuroraMCAPI.getDbManager().achievementRemove(uuid, achievement));
                                            ProxyAPI.getCore().getProxy().getScheduler().runAsync(ProxyAPI.getCore(), () -> AuroraMCAPI.getDbManager().achievementSet(uuid, achievement, 0));
                                        } else {
                                            if (target != null) {
                                                ProxyAPI.getPlayer(target).getStats().achievementGained(achievement, tier - 1, true);
                                                ProxyAPI.getPlayer(target).getStats().setAchievementProgress(achievement, ((TieredAcheivement)achievement).getTier(tier - 1).getRequirement(), true);
                                            } else {
                                                if (AuroraMCAPI.getDbManager().hasActiveSession(uuid)) {
                                                    ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "add_achievement_update", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId + "\n" + (tier - 1));
                                                    CommunicationUtils.sendMessage(message);
                                                } else {
                                                    AuroraMCAPI.getDbManager().achievementGet(uuid, achievement, tier - 1);
                                                }
                                            }
                                            AuroraMCAPI.getDbManager().achievementSet(uuid, achievement, ((TieredAcheivement)achievement).getTier(tier - 1).getRequirement());
                                        }
                                        player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s Tier %s** removed from **%s**", achievement.getName(), tier, args.get(1))));
                                    }
                                } else {
                                    player.sendMessage(TextFormatter.pluginMessage("Statistics", "That is not a valid tier."));
                                }
                            } else {

                                player.sendMessage(TextFormatter.pluginMessage("Statistics", "You cannot remove tiers of a non-tiered achievement."));
                            }
                        } else {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
                            if (target != null) {
                                ProxyAPI.getPlayer(target).getStats().achievementRemoved(achievement, true);
                            } else {
                                ProtocolMessage message = new ProtocolMessage(Protocol.UPDATE_PROFILE, AuroraMCAPI.getDbManager().getProxy(uuid).toString(), "remove_achievement", AuroraMCAPI.getInfo().getName(), uuid + "\n" + achievementId);
                                CommunicationUtils.sendMessage(message);
                            }
                            AuroraMCAPI.getDbManager().achievementRemove(uuid, achievement);
                            AuroraMCAPI.getDbManager().achievementSet(uuid, achievement, 0);
                            player.sendMessage(TextFormatter.pluginMessage("Statistics", String.format("Achievement **%s** removed from **%s**", achievement.getName(), args.get(1))));
                        }
                    } else {
                        player.sendMessage(TextFormatter.pluginMessage("Statistics", "That user does not have that achievement."));
                    }
                });
            } else {
                player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                        "**/statsadmin achievement add <player> <achievement ID> [tier]** - Add an achievement to a player.\n" +
                        "**/statsadmin achievement remove <player> <achievement ID> [tier]** - Remove an achievement from a player\n"));
            }
        } else {
            player.sendMessage(TextFormatter.pluginMessage("Statistics", "Available commands\n" +
                    "**/statsadmin achievement add <player> <achievement ID> [tier]** - Add an achievement to a player.\n" +
                    "**/statsadmin achievement remove <player> <achievement ID> [tier]** - Remove an achievement from a player\n"));
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(AuroraMCProxyPlayer player, String aliasUsed, List<String> args, String lastToken, int numberArguments) {
        List<String> completions = new ArrayList<>();
        if (numberArguments == 1) {
            if ("add".startsWith(lastToken)) completions.add("add");
            if ("remove".startsWith(lastToken)) completions.add("remove");
        }
        return completions;
    }
}
