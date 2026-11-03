package org.example;

import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillCalculator {
    private static Map<String, Object> make(String itemName, double startingPrice, boolean isFood, boolean isImport, int quantity) {
        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("itemName", itemName);
        orderInfo.put("startingPrice", startingPrice);
        orderInfo.put("isFood", isFood);
        orderInfo.put("isImport", isImport);
        orderInfo.put("quantity", quantity);
        return orderInfo;
    }

    public void calculate(boolean isWeekend, boolean isVip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(make("Book", 12.49, false, false, 1));
        items.add(make("Music CD", 14.99, false, true, 2));
        items.add(make("Headache pills", 9.75, true, false, 3));
        items.add(make("Chocolate bar", 0.85, true, true, 5));

        double total = 0;
        double taxes = 0;

        // loyalty stuff
        int pts = 0;
        String discountCode = "SAVE10";

        double saved = 0; // saved amount

        for (Map<String, Object> i : items) {
            double startingPrice = (double) i.get("startingPrice");
            boolean isFood = (boolean) i.get("isFood");
            boolean isImport = (boolean) i.get("isImport");
            int quantity = (int) i.get("quantity");
            double t = 0;
            if (isImport) {
                t += startingPrice * 0.10;
                if (startingPrice > 15) {
                    if (isImport) {
                        t += startingPrice * 0.02;
                    }
                }
            }
            if (!isFood) {
                t += startingPrice * 0.05;
                if (isWeekend) {
                    if (!isFood) {
                        t += startingPrice * 0.01;
                    }
                }
            }
            double finalPrice = (startingPrice + t) * startingPrice;
            double origPrice = finalPrice;

            // discounts
            if (startingPrice > 10 && !isFood) {
                finalPrice -= 1.0 * quantity;
                if (isVip && startingPrice > 10) {
                    finalPrice -= 0.5 * quantity;
                    if (isImport && isVip) {
                        finalPrice -= 0.25 * quantity;
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

            saved += (origPrice - finalPrice);
            total += finalPrice;
            taxes += t * quantity;

            // loyalty points
            if (isVip) {
                pts += (int)(finalPrice * 2);
                if (pts > 100) {
                    pts += 10;
                }
            } else {
                pts += (int)finalPrice;
                if (pts > 100) {
                    pts += 10;
                }
            }

            printIndividualItem((String) i.get("n"), quantity, finalPrice);
        }
        printTally(taxes, total, saved, pts);
    }
}