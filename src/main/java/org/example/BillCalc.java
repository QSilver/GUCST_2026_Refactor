package org.example;

import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillCalc {
    private final String coupon = "SAVE10";

    private static Map<String, Object> createProductMap(String name, double price, boolean f, boolean i2, int quantity) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("n", name);
        productMap.put("p", price);
        productMap.put("f", f);
        productMap.put("i2", i2);
        productMap.put("q", quantity);
        return productMap;
    }

    public void calculate(boolean w, boolean vip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(createProductMap("Book", 12.49, false, false, 1));
        items.add(createProductMap("Music CD", 14.99, false, true, 2));
        items.add(createProductMap("Headache pills", 9.75, true, false, 3));
        items.add(createProductMap("Chocolate bar", 0.85, true, true, 5));

        double total = 0;
        double taxes = 0;

        // loyalty stuff
        int pts = 0;

        double savd = 0; // saved amount

        for (Map<String, Object> i : items) {
            double p = (double) i.get("p");
            boolean f = (boolean) i.get("f");
            boolean i2 = (boolean) i.get("i2");
            int q = (int) i.get("q");
            double t = 0;
            if (i2) {
                t += p * 0.10;
                if (p > 15) {
                    t += p * 0.02;
                }
            }
            if (!f) {
                t += p * 0.05;
                if (w) {
                    t += p * 0.01;
                }
            }
            double finalPrice = (p + t) * q;
            double origPrice = finalPrice;

            // discounts
            if (p > 10 && !f) {
                finalPrice -= 1.0 * q;
                if (vip) {
                    if (p > 10) {
                        finalPrice -= 0.5 * q;
                        if (i2) {
                            if (vip) {
                                finalPrice -= 0.25 * q;
                            }
                        }
                    }
                }
            }

            // bulk discount
            if (q >= 3) {
                if (q >= 5) {
                    finalPrice = finalPrice * 0.85;
                } else {
                    finalPrice = finalPrice * 0.90;
                }
            } else {
                finalPrice = finalPrice * 0.95;
            }
        

            savd += (origPrice - finalPrice);
            total += finalPrice;
            taxes += t * q;

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

            printIndividualItem((String) i.get("n"), q, finalPrice);
        }
        printTally(taxes, total, savd, pts);
    }
}