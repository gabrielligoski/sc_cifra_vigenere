package ataque;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    // {'e', 't', 'a', 'o', 'i'};
    static final char[] EN_LETTER_FREQUENCY = new char[] {'E', 'T', 'A', 'O', 'I'};
    // {'a', 'e', 'o', 's', 'r'};
    static final char[] PT_LETTER_FREQUENCY = new char[] {'A', 'E', 'O', 'S', 'R'};

    private static String textToDecipher = "", language, key;
    private static Integer keySize;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter text to decipher:");

        String nextLine = "a";

        while (nextLine.length() > 0) {
            nextLine = scanner.nextLine();
            textToDecipher += nextLine;
        }
        System.out.println("Choose language [en or pt]:");
        language = scanner.nextLine();
        keySize = findKeySize(textToDecipher);
        System.out.println("Presumed key size: " + keySize);
        key = findSecretKey(textToDecipher, keySize, language);
        System.out.println("Presumed key: " + key);
    }

    static Integer findKeySize(String textToDecipher) {
        String formattedText = textToDecipher.replaceAll("[^a-zA-Z]", "");
        HashMap<String, ArrayList<Integer>> frequency = new HashMap<>();

        for (int i = 0; i < formattedText.length() - 3; i++) {
            String keyword = formattedText.substring(i, i + 3);

            if (!frequency.containsKey(keyword))
                frequency.put(keyword, new ArrayList<>());
            frequency.get(keyword).add(i);
        }

        LinkedHashMap<Integer, Integer> mode = new LinkedHashMap<>();

        frequency.forEach((s, wordFrequency) -> {
            if (wordFrequency.size() > 1)
                for (int i = 0; i < wordFrequency.size() - 1; i++) {
                    int spacing = wordFrequency.get(i + 1) - wordFrequency.get(i);
                    for (int j = 2; j < spacing; j++) {
                        if (spacing % j == 0) {
                            if (!mode.containsKey(j))
                                mode.put(j, 1);
                            else
                                mode.put(j, mode.get(j) + 1);
                        }
                    }
                }
        });

        // mega funcao mucho loka pra dar sort em dicionario
        LinkedHashMap<Integer, Integer> sortedMode = mode.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt((Integer obj) -> obj).reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        int prevKey = -1;

        for (Integer i : sortedMode.keySet()) {
            if ((prevKey != -1 && sortedMode.get(i) / (float) sortedMode.get(prevKey) < 0.75) || i % prevKey != 0)
                return prevKey;
            prevKey = i;
        }

        return -1;
    }

    static String findSecretKey(String textToDecipher, int keySize, String language) {
        String formattedText = textToDecipher.replaceAll("[^a-zA-Z]", "");

        StringBuilder secretKey = new StringBuilder();

        for (int i = 0; i < keySize; i++) {

            LinkedHashMap<Character, Integer> frequency = getLetterFrequency(formattedText, i, keySize);

            int bestMatch = findBestMatch(frequency, language);

            secretKey.append((char) (bestMatch + 65));
        }

        return secretKey.toString();
    }

    static int findBestMatch(LinkedHashMap<Character, Integer> frequency, String language) {
        int prevLookAlike = 0;
        int bestMatch = 0;
        for (int j = 0; j < 26; j++) {
            LinkedHashMap<Character, Integer> frequencyShifted = new LinkedHashMap<>();

            for (Character c : frequency.keySet())
                frequencyShifted.put((char) ((c + j - 65) % 26 + 65), frequency.get(c));

            int lookAlike = 0;
            if (language.equals("en")) {
                int weight = 5;
                for (Character value : EN_LETTER_FREQUENCY) {
                    weight--;
                    if (frequencyShifted.containsKey(value))
                        lookAlike += frequencyShifted.get(value) * weight;
                }
            } else {
                int weight = 5;
                for (Character value : PT_LETTER_FREQUENCY) {
                    weight--;
                    if (frequencyShifted.containsKey(value))
                        lookAlike += frequencyShifted.get(value) * weight;
                }
            }
            if (lookAlike >= prevLookAlike) {
                bestMatch = j;
                prevLookAlike = lookAlike;
            }
        }

        return (26 - bestMatch) % 26;
    }

    static LinkedHashMap<Character, Integer> getLetterFrequency(String text, int offset, int keySize) {
        String formattedText = text.toUpperCase();

        LinkedHashMap<Character, Integer> frequency = new LinkedHashMap<>();

        for (int i = offset; i < formattedText.length() - keySize; i += keySize) {
            char letter = formattedText.charAt(i);
            if (!frequency.containsKey(letter))
                frequency.put(letter, 0);
            frequency.put(letter, frequency.get(letter) + 1);
        }

        return frequency;
    }

}
