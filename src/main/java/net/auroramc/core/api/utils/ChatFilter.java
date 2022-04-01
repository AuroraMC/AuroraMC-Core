/*
 * Copyright (c) 2021-2021 AuroraMC Ltd. All Rights Reserved.
 */

package net.auroramc.core.api.utils;

import net.auroramc.core.api.AuroraMCAPI;
import net.auroramc.core.api.players.AuroraMCPlayer;
import net.auroramc.core.api.players.PlayerPreferences;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChatFilter {

    private final List<String> coreFilteredWords;
    private final List<String> toxicReplacements;
    private final List<String> wordWhitelist;
    private final List<String> wordBlacklist;
    private final List<String> bannedPhrases;

    public ChatFilter(List<String> coreFilteredWords, List<String> wordBlacklist, List<String> wordWhitelist, List<String> toxicReplacements, List<String> bannedPhrases) {
        this.coreFilteredWords = coreFilteredWords;
        this.wordBlacklist = wordBlacklist;
        this.wordWhitelist = wordWhitelist;
        this.toxicReplacements = toxicReplacements;
        this.bannedPhrases = bannedPhrases;
    }

    public String filter(String message) {
        return filter(null, message);
    }

    public String filter(AuroraMCPlayer player, String message) {
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split(" ")));
        List<String> finalMessage = new ArrayList<>();

        if (splitMessage.size() == 1) {
            if (message.matches("^[eE][zZ]+$") || message.matches("^[lL]+$") || message.equalsIgnoreCase("trash")) {
                if (player != null) {
                    if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(12))) {
                        player.getStats().achievementGained(AuroraMCAPI.getAchievement(12), 1, true);
                    }
                }
                Random random = new Random();
                int phrase = random.nextInt(toxicReplacements.size());
                return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
            }
            return filterWord(message);
        }

        pairs:
        for (String word : splitMessage) {
            if (word.matches("^[eE][zZ]+$") || word.matches("^[lL]+$") || word.equalsIgnoreCase("trash")) {
                if (player != null) {
                    if (!player.getStats().getAchievementsGained().containsKey(AuroraMCAPI.getAchievement(12))) {
                        player.getStats().achievementGained(AuroraMCAPI.getAchievement(12), 1, true);
                    }
                }
                Random random = new Random();
                int phrase = random.nextInt(toxicReplacements.size());
                return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
            }
            finalMessage.add(filterWord(word));
        }

        String finalJoinedMessage = String.join(" ", finalMessage);

        for (String phrase : bannedPhrases) {
            if (finalJoinedMessage.toLowerCase().contains(phrase)) {
                finalJoinedMessage = finalJoinedMessage.replace(phrase, "HONK!");
            }
        }

        return finalJoinedMessage;
    }

    private String filterWord(String word) {
        filteredWords:
        for (String filteredWord : coreFilteredWords) {
            if (word.toLowerCase().equalsIgnoreCase(filteredWord)) {
                for (String whitelistedWord : wordWhitelist) {
                    if (word.toLowerCase().equalsIgnoreCase(whitelistedWord)) {
                        break filteredWords;
                    }
                }
                //This is not in the word whitelist, filter it;
                //So as to skip the second part of the pair, add 1.
                return "HONK!";
            }
        }

        for (String blacklistedWord : wordBlacklist) {
            if (word.toLowerCase().equalsIgnoreCase(blacklistedWord)) {
                //So as to skip the second part of the pair, add 1.
                return "HONK!";
            }
        }

        return word;
    }

    public String processMentions(String message) {
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split(" ")));
        List<String> finalMessage = new ArrayList<>();
        for (String word : splitMessage) {
            AuroraMCPlayer disguise = AuroraMCAPI.getDisguisedPlayer(word);
            if (disguise != null) {
                if (disguise.isLoaded()) {
                    if (!disguise.isVanished()) {
                        finalMessage.add("§b" + disguise.getPlayer().getName() + "§r");
                        continue;
                    }
                }
            }
            AuroraMCPlayer player = AuroraMCAPI.getPlayer(word);
            if (player != null) {
                if (player.isLoaded()) {
                    if (!player.isVanished() && player.getActiveDisguise() == null) {
                        finalMessage.add("§b" + player.getPlayer().getName() + "§r");
                        continue;
                    }
                }
            }
            finalMessage.add(word);
        }
        return String.join(" ", finalMessage);
    }

    public String processMentions(AuroraMCPlayer sender, AuroraMCPlayer recipient, String message) {
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split(" ")));
        List<String> finalMessage = new ArrayList<>();
        boolean alreadyFound = false;
        for (String word : splitMessage) {
            if (word.equalsIgnoreCase(recipient.getPlayer().getName()) && !alreadyFound) {
                if (recipient.isLoaded()) {
                    if (!recipient.isVanished()) {
                        if (recipient.getActiveDisguise() != null && !recipient.getPreferences().isHideDisguiseNameEnabled()) {
                            finalMessage.add("§b" + recipient.getPlayer().getName() + "§r");
                            alreadyFound = true;
                            continue;
                        } else {
                            finalMessage.add("§b" + recipient.getName() + "§r");
                            alreadyFound = true;
                            continue;
                        }

                    }
                }
            }
            finalMessage.add(word);
        }
        return String.join(" ", finalMessage);
    }

    public List<String> getCoreFilteredWords() {
        return new ArrayList<>(coreFilteredWords);
    }

    public List<String> getToxicReplacements() {
        return new ArrayList<>(toxicReplacements);
    }

    public List<String> getWordWhitelist() {
        return new ArrayList<>(wordWhitelist);
    }

    public List<String> getWordBlacklist() {
        return new ArrayList<>(wordBlacklist);
    }

    public List<String> getBannedPhrases() {
        return new ArrayList<>(bannedPhrases);
    }

    public void addWhitelistedWord(String word) {
        wordWhitelist.add(word);
    }

    public void addBlacklistedWord(String word) {
        wordBlacklist.add(word);
    }

    public void addCoreFilteredWord(String word) {
        coreFilteredWords.add(word);
    }

    public void addToxicReplacement(String word) {
        toxicReplacements.add(word);
    }

    public void addBannedPhrase(String phrase) {
        bannedPhrases.add(phrase);
    }

}
