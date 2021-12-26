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
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split(" ")));
        List<String> finalMessage = new ArrayList<>();

        if (splitMessage.size() == 1) {
            return filterWord(message);
        }

        pairs:
        for (String word : splitMessage) {
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

        if (word.matches("^[eE][zZ]+$") || word.matches("^[lL]+$")) {
            Random random = new Random();
            int phrase = random.nextInt(toxicReplacements.size());
            return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
        }

        return word;
    }

    public String processMentions(AuroraMCPlayer pl, String message) {
        List<String> splitMessage = new ArrayList<>(Arrays.asList(message.split(" ")));
        List<String> finalMessage = new ArrayList<>();
        for (String word : splitMessage) {
            AuroraMCPlayer player = AuroraMCAPI.getPlayer(word);
            if (player != null) {
                if (player.isLoaded()) {
                    if (!player.isVanished()) {
                        finalMessage.add("§c" + player.getPlayer().getName() + "§r");
                        if (player.getActiveMutes().size() > 0 && player.getPreferences().getMuteInformMode() == PlayerPreferences.MuteInformMode.MESSAGE_AND_MENTIONS) {
                            String msg = AuroraMCAPI.getFormatter().privateMessage(player.getPlayer().getName(), pl, "Hey! I'm currently muted and cannot message you right now.");
                            player.getPlayer().sendMessage(msg);
                        }
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.NOTE_PLING, 100, 1);
                        continue;
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
