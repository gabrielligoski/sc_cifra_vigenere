package Cifrador;

import java.util.Scanner;

public class Cifrador {

    private static String plainText="", key, keyStream, cipherText;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter plain text:");
        String nextLine = "a";
        while (nextLine.length() > 0) {
            nextLine = scanner.nextLine();
            plainText += nextLine;
        }
        System.out.println("Enter key:");
        key = scanner.nextLine();
        keyStream = extendKey(key, plainText);
        cipherText = cipherText(plainText, keyStream);
        System.out.println(cipherText);
    }

    private static String extendKey(String key, String plainText) {
        StringBuilder extendedKey = new StringBuilder();

        for (int i = 0; i < plainText.replaceAll("[^a-zA-Z]", "").length(); i++)
            extendedKey.append(key.charAt(i % key.length()));

        for (int i = 0; i < plainText.length(); i++)
            if (plainText.charAt(i) < 65)
                extendedKey.insert(i, ' ');

        return String.valueOf(extendedKey);
    }

    private static String cipherText(String text, String key) {
        StringBuilder cipheredText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) < 65) {
                cipheredText.append(text.charAt(i));
                continue;
            }

            char pTextIndex = getCharIndex(text.charAt(i));
            char keyIndex = getCharIndex(key.charAt(i));
            int cipheredIndex = (pTextIndex + keyIndex) % 26;
            if (text.charAt(i) > 90)
                cipheredText.append((char) (cipheredIndex + 97));
            else
                cipheredText.append((char) (cipheredIndex + 65));
        }
        return String.valueOf(cipheredText);
    }

    private static char getCharIndex(char character) {
        if (character > 90)
            return (char) (character - 97);
        else
            return (char) (character - 65);
    }
}
