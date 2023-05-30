/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.utils.targetselector;

import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.proxy.api.ProxyAPI;
import net.auroramc.proxy.api.player.AuroraMCProxyPlayer;
import net.auroramc.proxy.api.utils.Game;

import java.util.ArrayList;
import java.util.List;

public class TargetSelection {

    private CombinerType type;
    private boolean plus;
    private boolean all;
    private List<Rank> ranks;
    private List<SubRank> subranks;
    private List<Game> games;

    public TargetSelection(boolean all, boolean plus, List<Rank> ranks, List<SubRank> subranks, List<Game> games) {
        this.plus = plus;
        this.all = all;
        this.ranks = ranks;
        this.subranks = subranks;
        this.games = games;
    }

    public TargetSelection(CombinerType type, List<TargetSelection> selections) {
        this.type = type;
        for (TargetSelection selection : selections) {
            plus = plus || selection.plus;
            all = all || selection.all;
            if (selection.subranks != null) {
                this.subranks = selection.subranks;
            } else if (selection.ranks != null) {
               this.ranks = selection.ranks;
            } else if (selection.games != null) {
                this.games = selection.games;
            }
        }

        if (subranks == null) {
            subranks = new ArrayList<>();
        }
        if (ranks == null) {
            ranks = new ArrayList<>();
        }
        if (games == null) {
            games = new ArrayList<>();
        }
    }

    public boolean shouldSend(AuroraMCProxyPlayer player) {
        if (type != null) {
            if (type == CombinerType.OR) {
                //This is an or operation.
                if (all) {
                    return true;
                }
                if (games.contains(Game.valueOf(ProxyAPI.getAmcServers().get(player.getServer().getName()).getServerType().getString("game")))) {
                    return true;
                }
                if (ranks.contains(player.getRank())) {
                    return true;
                }
                if (subranks.stream().anyMatch(player.getSubranks()::contains)) {
                    return true;
                }
                return plus && player.getActiveSubscription() != null;
            } else {
                if (!ranks.contains(player.getRank())) {
                    return false;
                }
                if (!games.contains(Game.valueOf(ProxyAPI.getAmcServers().get(player.getServer().getName()).getServerType().getString("game")))) {
                    return false;
                }
                if (subranks.stream().noneMatch(player.getSubranks()::contains) && subranks.size() > 0) {
                    return false;
                }
                return !plus || player.getActiveSubscription() != null;
            }
        } else {
            //This is a singular selection. Check the singular condition.
            if (all) {
                return true;
            }
            if (plus && player.getActiveSubscription() != null) {
                return true;
            }
            if (ranks != null) {
                if (ranks.contains(player.getRank())) {
                    return true;
                }
            }
            if (subranks != null) {
                if (subranks.stream().anyMatch(player.getSubranks()::contains)) {
                    return true;
                }
            }
            if (games != null) {
                return games.contains(Game.valueOf(ProxyAPI.getAmcServers().get(player.getServer().getName()).getServerType().getString("game")));
            }
            return false;
        }
    }

    public enum CombinerType {
        OR('/'), AND('*');

        private final char symbol;

        CombinerType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

}
