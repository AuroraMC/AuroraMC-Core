/*
 * Copyright (c) 2021-2023 AuroraMC Ltd. All Rights Reserved.
 *
 * PRIVATE AND CONFIDENTIAL - Distribution and usage outside the scope of your job description is explicitly forbidden except in circumstances where a company director has expressly given written permission to do so.
 */

package net.auroramc.api.utils;

import net.auroramc.api.AuroraMCAPI;
import net.auroramc.api.cosmetics.ChatEmote;
import net.auroramc.api.cosmetics.Cosmetic;
import net.auroramc.api.player.AuroraMCPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.stream.Collectors;

public class ChatFilter {

    private final List<String> coreFilteredWords;
    private final List<String> toxicReplacements;
    private final List<String> wordWhitelist;
    private final List<String> wordBlacklist;
    private final List<String> bannedPhrases;

    private static final Map<String, ChatEmote> emotes;

    private static final List<String> replacements;


    static {
        emotes = new HashMap<>();
        replacements = Arrays.asList("BLEEP!", "BLORP!", "BLURP!", "BLOOP!");
        for (Cosmetic cosmetic : AuroraMCAPI.getCosmetics().values().stream().filter(cosmetic -> cosmetic.getType() == Cosmetic.CosmeticType.CHAT_EMOTE).collect(Collectors.toList())) {
            ChatEmote emote = (ChatEmote) cosmetic;
            emotes.put(emote.getDisplayName(), emote);
        }
    }

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
                finalJoinedMessage = finalJoinedMessage.replace(phrase, replacements.get(new Random().nextInt(replacements.size())));
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
                return replacements.get(new Random().nextInt(replacements.size()));
            }
        }

        for (String blacklistedWord : wordBlacklist) {
            if (word.toLowerCase().equalsIgnoreCase(blacklistedWord)) {
                //So as to skip the second part of the pair, add 1.
                return replacements.get(new Random().nextInt(replacements.size()));
            }
        }

        return word;
    }

    public BaseComponent processEmotes(AuroraMCPlayer player, String message) {
        String[] msg = message.split(" ");
        TextComponent component = new TextComponent("");
        Iterator<String> iterator = Arrays.stream(msg).iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.matches("^:[a-zA-Z]+:$")) {
                ChatEmote emote = emotes.get(s.substring(1, s.length()-1).toLowerCase());
                if (emote != null && emote.hasUnlocked(player)) {

                    TextComponent em = new TextComponent(emote.getDescription());
                    em.setColor(emote.getColor());
                    em.setBold(emote.isBold());
                    component.addExtra(em);
                    if (iterator.hasNext()) {
                        component.addExtra(" ");
                    }
                    continue;
                }
            }
            TextComponent cmp = new TextComponent(s);
            cmp.setBold(false);
            cmp.setColor(player.getRank().getDefaultChatColor());
            component.addExtra(cmp);
            if (iterator.hasNext()) {
                component.addExtra(" ");
            }
        }
        return component;
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

    public static Map<String, ChatEmote> getEmotes() {
        return emotes;
    }
}
