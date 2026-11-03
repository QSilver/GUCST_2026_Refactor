package org.example;

import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;


public class BillThing {

    public static final float SALES_TAX_WEEKDAYS = 0.05;
    public static final float SALES_TAX_WEEKEND = 0.01;
    public static final float IMPORT_TAX = 0.1;
    public static final float EXTRA_IMPORT_TAX = 0.02;

    public static final float BASE_MONEY_OFF = 1;
    public static final float DISCOUNT_THRESHOLD = 10;
    public static final float VIP_MONEY_OFF = 0.5;
    public static final float VIP_MONEY_OFF_IMPORT = 0.25;

    private static Map<String, Object> make(String name, double price, boolean isFood, boolean isImported, int quantity) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("price", price);
        m.put("isFood", isFood);
        m.put("isImported", isImported);
        m.put("quantity", quantity);
        return m;
    }

    public void calculate(boolean isWeekend, boolean isVIP) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(make("Book", 12.49, false, false, 1));
        items.add(make("Music CD", 14.99, false, true, 2));
        items.add(make("Headache pills", 9.75, true, false, 3));
        items.add(make("Chocolate bar", 0.85, true, true, 5));

        double total = 0;
        double taxes = 0;

        // loyalty stuff
        int loyaltyPts = 0;
        String discountCode = "SAVE10";

        double savd = 0; // saved amount

        for (Map<String, Object> i : items) {
            double price = (double) i.get("price");
            boolean isFood = (boolean) i.get("isFood");
            boolean isImported = (boolean) i.get("isImported");
            int quantity = (int) i.get("quantity");
            double tax = 0;
            if (isImported) {
                tax += price * IMPORT_TAX;
                if (price > 15) {
                    if (isImported) {
                        tax += price * EXTRA_IMPORT_TAX;
                    }
                }
            }
            if (!isFood) {
                tax += price * SALES_TAX_WEEKDAYS;
                if (isWeekend) {
                    if (!isFood) {
                        tax += price * SALES_TAX_WEEKEND;
                    }
                }
            }
            double finalPrice = (price + tax) * quantity;
            double origPrice = finalPrice;

            // discounts
            if (price > 10 && !isFood) {
                finalPrice -= BASE_MONEY_OFF * quantity;
                if (isVIP) {
                    if (price > DISCOUNT_THRESHOLD) {
                        finalPrice -= VIP_MONEY_OFF * quantity;
                        if (isImported) {
                            if (isVIP) {
                                finalPrice -= VIP_MONEY_OFF_IMPORT * quantity;
                            }
                        }
                    }
                }
            }

            // bulk discount
            if (quantity > 1) {
                if (quantity >= 3) {
                    if (quantity >= 5) {
                        finalPrice = finalPrice * 0.85;
                    } else {
                        finalPrice = finalPrice * 0.90;
                    }
                } else {
                    finalPrice = finalPrice * 0.95;
                }
            }

            savd += (origPrice - finalPrice);
            total += finalPrice;
            taxes += tax * quantity;

            // loyalty points
            if (isVIP) {
                loyaltyPts += (int)(finalPrice * 2);
                if (loyaltyPts > 100) {
                    loyaltyPts += 10;
                }
            } else {
                loyaltyPts += (int)finalPrice;
                if (loyaltyPts > 100) {
                    loyaltyPts += 10;
                }
            }

            printIndividualItem((String) i.get("name"), quantity, finalPrice);
        }
        printTally(taxes, total, savd, loyaltyPts);
    }
}