package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillCalc {
    private static Map<String, Object> makeItem(String name, double price, boolean isFood, boolean isImported, int quantity) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("price", price);
        item.put("isFood", isFood);
        item.put("isImported", isImported);
        item.put("quantity", quantity);
        return item;
    }

    public void calculate(boolean isWeekend, boolean isVip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(makeItem("Book", 12.49, false, false, 1));
        items.add(makeItem("Music CD", 14.99, false, true, 2));
        items.add(makeItem("Headache pills", 9.75, true, false, 3));
        items.add(makeItem("Chocolate bar", 0.85, true, true, 5));

        double totalPrice = 0;
        double totalTax = 0;
        double totalSaved = 0;
        int loyaltyPoints = 0;
        String couponCode = loadSecret("COUPON_CODE");

        for (Map<String, Object> item : items) {
            double price = (double) item.get("price");
            boolean isFood = (boolean) item.get("isFood");
            boolean isImported = (boolean) item.get("isImported");
            int quantity = (int) item.get("quantity");

            double taxPerUnit = taxPerUnit(price, isFood, isImported, isWeekend);
            double priceBeforeDiscounts = (price + taxPerUnit) * quantity;
            double finalPrice = priceBeforeDiscounts - discountPerUnit(price, isFood, isImported, isVip) * quantity;
            finalPrice *= bulkMultiplier(quantity);

            totalSaved += priceBeforeDiscounts - finalPrice;
            totalPrice += finalPrice;
            totalTax += taxPerUnit * quantity;

            int pointsPerDollar = isVip ? 2 : 1;
            loyaltyPoints += (int) (finalPrice * pointsPerDollar);
            if (loyaltyPoints > 100) {
                loyaltyPoints += 10;
            }

            printIndividualItem((String) item.get("name"), quantity, finalPrice);
        }
        printTally(totalTax, totalPrice, totalSaved, loyaltyPoints);
    }

    private static double taxPerUnit(double price, boolean isFood, boolean isImported, boolean isWeekend) {
        double rate = 0;
        if (isImported) {
            rate += 0.10;
            if (price > 15) {
                rate += 0.02;
            }
        }
        if (!isFood) {
            rate += isWeekend ? 0.06 : 0.05;
        }
        return price * rate;
    }

    private static double discountPerUnit(double price, boolean isFood, boolean isImported, boolean isVip) {
        if (price <= 10 || isFood) {
            return 0;
        }
        double discount = 1.0;
        if (isVip) {
            discount += 0.5;
            if (isImported) {
                discount += 0.25;
            }
        }
        return discount;
    }

    private static double bulkMultiplier(int quantity) {
        if (quantity >= 5) return 0.85;
        if (quantity >= 3) return 0.90;
        if (quantity == 2) return 0.95;
        return 1.0;
    }

    // Real environment variables win; otherwise fall back to the .env file in the working directory.
    private static String loadSecret(String key) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null) {
            return fromEnv;
        }
        try {
            for (String line : Files.readAllLines(Path.of(".env"))) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2 && parts[0].trim().equals(key)) {
                    return parts[1].trim();
                }
            }
        } catch (IOException e) {
            // no .env file - secret stays unset
        }
        return null;
    }
}
