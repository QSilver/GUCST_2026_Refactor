package org.example;

//import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillThing {
    private static Map<String, Object> make(String name, double price, boolean isFood, boolean isImportedItem, int quantity) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("price", price);
        m.put("isFood", isFood);
        m.put("isImportedItem", isImportedItem);
        m.put("quantity", quantity);
        return m;
    }

    public void calculate(boolean w, boolean vip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(make("Book", 12.49, false, false, 1));
        items.add(make("Music CD", 14.99, false, true, 2));
        items.add(make("Headache pills", 9.75, true, false, 3));
        items.add(make("Chocolate bar", 0.85, true, true, 5));

        double total = 0;
        double taxes = 0;

        // loyalty stuff
        int pts = 0;
        String c = "SAVE10";

        double savd = 0; // saved amount

        for (Map<String, Object> i : items) {
            double price = (double) i.get("price");
            boolean isFood = (boolean) i.get("isFood");
            boolean isImportedItem = (boolean) i.get("isImportedItem");
            int quantity = (int) i.get("quantity");
            double tax = 0;
            if (isImportedItem) {
                tax += price * 0.10;
                if (price > 15) {
                    if (isImportedItem) {
                        tax += price * 0.02;
                    }
                }
            }
            if (!isFood) {
                tax += price * 0.05;
                if (w) {
                    if (!isFood) {
                        tax += price * 0.01;
                    }
                }
            }
            double finalPrice = (price + tax) * quantity;
            double origPrice = finalPrice;

            // discounts
            if (price > 10 && !isFood) {
                finalPrice -= 1.0 * quantity;
                if (vip) {
                    if (price > 10) {
                        finalPrice -= 0.5 * quantity;
                        if (isImportedItem) {
                            if (vip) {
                                finalPrice -= 0.25 * quantity;
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
            if (vip) {
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

            printIndividualItem((String) i.get("name"), quantity, finalPrice);
        }
        printTally(taxes, total, savd, pts);
    }
}