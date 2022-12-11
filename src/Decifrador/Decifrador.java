package Decifrador;

import java.util.Scanner;

public class Decifrador {

    private static String plainText, key, keyStream, cipherText="";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ciphered text:");
        String nextLine = "a";
        while (nextLine.length() > 0) {
            nextLine = scanner.nextLine();
            cipherText += nextLine;
        }
        System.out.println("Enter key:");
        key = scanner.nextLine();
        keyStream = extendKey(key, cipherText);
        plainText = decipherText(cipherText, keyStream);
        System.out.println(plainText);
    }

    private static String extendKey(String key, String text) {
        StringBuilder extendedKey = new StringBuilder();

        for (int i = 0; i < text.replaceAll("[^a-zA-Z]", "").length(); i++)
            extendedKey.append(key.charAt(i % key.length()));

        for (int i = 0; i < text.length(); i++)
            if (text.charAt(i) < 65)
                extendedKey.insert(i, ' ');

        return String.valueOf(extendedKey);
    }

    private static String decipherText(String text, String key) {
        StringBuilder cipheredText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) < 65) {
                cipheredText.append(text.charAt(i));
                continue;
            }

            char pTextIndex = getCharIndex(text.charAt(i));
            char keyIndex = getCharIndex(key.charAt(i));
            int decipheredIndex = ((26 - keyIndex) + pTextIndex) % 26;
            if (text.charAt(i) > 90)
                cipheredText.append((char) (decipheredIndex + 97));
            else
                cipheredText.append((char) (decipheredIndex + 65));
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
