package util;

import java.util.Random;

public class CardNumberGenerator {

    private static final Random random = new Random();

    // Generates a random card number with exactly 10 digits
    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();

        // Generate 10 random digits
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10)); // 0-9
        }

        return cardNumber.toString();
    }
}
