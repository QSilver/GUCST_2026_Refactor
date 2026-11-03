package org.example;

import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillThing {
    private static Map<String, Object> make(String n, double p, boolean f, boolean i2, int q) {
        Map<String, Object> m = new HashMap<>();
        m.put("n", n);
        m.put("p", p);
        m.put("f", f);
        m.put("i2", i2);
        m.put("q", q);
        return m;

        double total = 0;
        double taxes = 0;

        int pts = 0;
        String discountCode = "SAVE10";

        double savd = 0; // saved amount

        double finalPrice = (p + t) * q;
        double origPrice = finalPrice;

        float highBulkDiscount = 0.85
        float mediumBulkDiscount = 0.90
        float lowBulkDiscount = 0.95
    }
    

    public void calculate(boolean w, boolean vip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(make("Book", 12.49, false, false, 1));
        items.add(make("Music CD", 14.99, false, true, 2));
        items.add(make("Headache pills", 9.75, true, false, 3));
        items.add(make("Chocolate bar", 0.85, true, true, 5));

        savd += (origPrice - finalPrice);
        total += finalPrice;
        taxes += t * q;
        priceCalculation();
        loyaltyCalculation(boolean vip, double finalPrice, int pts);
        priceCalculation();
        discount(int p, boolean f, boolean vip, double finalPrice);
        bulkDiscount(int q, double finalPrice);
        

        
    public void loyaltyCalculation(boolean vip, double finalPrice, int pts){
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
            }}


    public void priceCalculation(){
        for (Map<String, Object> i : items) {
            double p = (double) i.get("p");
            boolean f = (boolean) i.get("f");
            boolean i2 = (boolean) i.get("i2");
            int q = (int) i.get("q");
            double t = 0;
            if (i2) {
                t += p * 0.10;
                if (p > 15) {
                    if (i2) {
                        t += p * 0.02;
                    }
                }
            }
            if (!f) {
                t += p * 0.05;
                if (w) {
                    if (!f) {
                        t += p * 0.01;
                    }
                }
            }}

    public void discount(int p, boolean f, boolean vip, double finalPrice){
            if (p > 10 && !f) {
                finalPrice -= 1.0 * q;
                if (vip && (p > 10)) {
                        finalPrice -= 0.5 * q;
                        if (i2 && (vip) ) {
                                finalPrice -= 0.25 * q;
                            }
                        }
                    }}
            

    public void bulkDiscount(int q, double finalPrice){
            if (q > 1) {
                if (q >= 3) {
                    if (q >= 5) {
                        finalPrice = finalPrice * highBulkDiscount;
                    } else {
                        finalPrice = finalPrice * mediumBulkDiscount;
                    }
                } else {
                    finalPrice = finalPrice * lowBulkDiscount;
                }
            }}






            printIndividualItem((String) i.get("n"), q, finalPrice);
        }
        printTally(taxes, total, savd, pts);
    }
}
