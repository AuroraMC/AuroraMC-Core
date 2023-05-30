/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.proxy.api.utils.targetselector;

import net.auroramc.api.permissions.Rank;
import net.auroramc.api.permissions.SubRank;
import net.auroramc.proxy.api.utils.Game;

import java.util.ArrayList;
import java.util.List;

public class TargetSelectorParser {

    public static TargetSelection parse(String selectors) throws SelectorParseFailException {
        String[] args = selectors.split("/");
        if (args.length > 1) {
            //This is an OR operation.
            List<TargetSelection> selections = new ArrayList<>();
            for (String statement : args) {
                selections.add(parseSingular(statement));
            }
            return new TargetSelection(TargetSelection.CombinerType.OR, selections);
        }

        args = selectors.split("[*]");
        if (args.length > 1) {
            //This is an AND operation.
            List<TargetSelection> selections = new ArrayList<>();
            for (String statement : args) {
                selections.add(parseSingular(statement));
            }
            return new TargetSelection(TargetSelection.CombinerType.AND, selections);
        }

        //It's not a combiner, parse as singular selector.
        return parseSingular(selectors);
    }

    private static TargetSelection parseSingular(String selector) throws SelectorParseFailException {
        String[] args = selector.split(":");
        if (args.length == 2) {
            switch (args[0]) {
                case "rank": {
                    List<Rank> ranks = new ArrayList<>();
                    String[] rankStrings = args[1].split(",");
                    for (String rank : rankStrings) {
                        if (rank.endsWith("+")) {
                            //Look for children too.
                            String baseRankString = rank.replace("+", "");
                            Rank baseRank;
                            try {
                                baseRank = Rank.valueOf(baseRankString);
                            } catch (IllegalArgumentException e) {
                                throw new SelectorParseFailException("An invalid rank was specified.");
                            }
                            ranks.add(baseRank);
                            for (Rank rank1 : Rank.values()) {
                                if (baseRank.isChild(rank1)) {
                                    ranks.add(rank1);
                                }
                            }
                        } else if (rank.endsWith("-")) {
                            //Look for parents too.
                            String baseRankString = rank.replace("-", "");
                            Rank baseRank;
                            try {
                                baseRank = Rank.valueOf(baseRankString);
                            } catch (IllegalArgumentException e) {
                                throw new SelectorParseFailException("An invalid rank was specified.");
                            }
                            ranks.add(baseRank);
                            for (Rank rank1 : Rank.values()) {
                                if (baseRank.isParent(rank1)) {
                                    ranks.add(rank1);
                                }
                            }
                        } else {
                            Rank rank1;
                            try {
                                rank1 = Rank.valueOf(rank);
                            } catch (IllegalArgumentException e) {
                                throw new SelectorParseFailException("An invalid rank was specified.");
                            }
                            ranks.add(rank1);
                        }
                    }
                    return new TargetSelection(false, false, ranks, null, null);
                }
                case "subrank": {
                    List<SubRank> subranks = new ArrayList<>();
                    String[] subrankStrings = args[1].split(",");
                    for (String subrank : subrankStrings) {
                        SubRank subrank1;
                        try {
                            subrank1 = SubRank.valueOf(subrank);
                        } catch (IllegalArgumentException e) {
                            throw new SelectorParseFailException("An invalid subrank was specified.");
                        }
                        subranks.add(subrank1);
                    }
                    return new TargetSelection(false, false, null, subranks, null);
                }
                case "game": {
                    List<Game> games = new ArrayList<>();
                    String[] gameStrings = args[1].split(",");
                    for (String game : gameStrings) {
                        Game game1;
                        try {
                            game1 = Game.valueOf(game);
                        } catch (IllegalArgumentException e) {
                            throw new SelectorParseFailException("An invalid game was specified.");
                        }
                        games.add(game1);
                    }
                    return new TargetSelection(false, false, null, null, games);
                }
                default: {
                    throw new SelectorParseFailException("The selector '" + args[0] + "' is not valid as a selector with an argument.");
                }
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) {
                return new TargetSelection(true, false, null, null, null);
            }
            if (args[0].equalsIgnoreCase("plus")) {
                return new TargetSelection(false, true, null, null, null);
            }
            throw new SelectorParseFailException("The selector '" + args[0] + "' is not valid as a selector without an argument.");
        } else {
            throw new SelectorParseFailException("One of the selectors specified does not have a valid arguments.");
        }
    }

}
