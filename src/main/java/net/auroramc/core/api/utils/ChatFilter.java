package net.auroramc.core.api.utils;

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
        for (int i = 0;i < splitMessage.size() - 1;i++) {
            String pair = splitMessage.get(i).toLowerCase() + splitMessage.get(i+1).toLowerCase();
            if (splitMessage.get(i).equalsIgnoreCase("ez") || splitMessage.get(i).matches("^[lL]+$")) {
                Random random = new Random();
                int phrase = random.nextInt(toxicReplacements.size());
                return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
            }
            filteredWords:
            for (String filteredWord : coreFilteredWords) {
                if (pair.contains(filteredWord)) {
                    for (String whitelistedWord : wordWhitelist) {
                        if (pair.contains(whitelistedWord)) {
                            continue filteredWords;
                        }
                    }
                    //This is not in the word whitelist, filter it;
                    //So as to skip the second part of the pair, add 1.
                    if (!splitMessage.get(i).toLowerCase().contains(filteredWord) && splitMessage.get(i + 1).toLowerCase().contains(filteredWord)) {
                        finalMessage.add(filterWord(splitMessage.get(i)));
                        finalMessage.add("HONK!");
                    } else if (splitMessage.get(i).toLowerCase().contains(filteredWord) && !splitMessage.get(i + 1).toLowerCase().contains(filteredWord)) {
                        finalMessage.add("HONK!");
                        finalMessage.add(filterWord(splitMessage.get(i+1)));
                    } else {
                        finalMessage.add("HONK!");
                    }
                    i++;
                    if (i == splitMessage.size() - 2) {
                        finalMessage.add(filterWord(splitMessage.get(i + 1)));
                    }
                    continue pairs;
                }
            }

            for (String blacklistedWord : wordBlacklist) {
                if (pair.contains(blacklistedWord)) {
                    if (!splitMessage.get(i).toLowerCase().contains(blacklistedWord) && splitMessage.get(i + 1).toLowerCase().contains(blacklistedWord)) {
                        finalMessage.add(filterWord(splitMessage.get(i)));
                        finalMessage.add("HONK!");
                    } else if (splitMessage.get(i).toLowerCase().contains(blacklistedWord) && !splitMessage.get(i + 1).toLowerCase().contains(blacklistedWord)) {
                        finalMessage.add("HONK!");
                        finalMessage.add(filterWord(splitMessage.get(i+1)));
                    } else {
                        finalMessage.add("HONK!");
                    }
                    //So as to skip the second part of the pair, add 1.
                    i++;
                    if (i == splitMessage.size() - 2) {
                        finalMessage.add(filterWord(splitMessage.get(i + 1)));
                    }
                    continue pairs;
                }
            }

            finalMessage.add(splitMessage.get(i));
            if (i == splitMessage.size() - 2) {
                finalMessage.add(filterWord(splitMessage.get(i + 1)));
            }
        }

        if (splitMessage.get(splitMessage.size() - 1).equalsIgnoreCase("ez") || splitMessage.get(splitMessage.size() - 1).matches("^[lL]+$")) {
            Random random = new Random();
            int phrase = random.nextInt(toxicReplacements.size());
            return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
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
            if (word.toLowerCase().contains(filteredWord)) {
                for (String whitelistedWord : wordWhitelist) {
                    if (word.toLowerCase().contains(whitelistedWord)) {
                        break filteredWords;
                    }
                }
                //This is not in the word whitelist, filter it;
                //So as to skip the second part of the pair, add 1.
                return "HONK!";
            }
        }

        for (String blacklistedWord : wordBlacklist) {
            if (word.toLowerCase().contains(blacklistedWord)) {
                //So as to skip the second part of the pair, add 1.
                return "HONK!";
            }
        }

        if (word.equalsIgnoreCase("ez") || word.matches("^[lL]+$")) {
            Random random = new Random();
            int phrase = random.nextInt(toxicReplacements.size());
            return toxicReplacements.get(phrase).replace("&#39;","'").replace("&#38;","&").replace("&#34;", "\"");
        }

        return word;
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
