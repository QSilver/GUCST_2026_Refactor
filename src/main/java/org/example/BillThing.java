package org.example;

import java.util.*;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillThing {
    private static final double BASIC_SALES_TAX_RATE = 0.05;
    private static final double IMPORT_DUTY_RATE = 0.10;
    private static final double LUXURY_IMPORT_SURCHARGE_RATE = 0.02;
    private static final double LUXURY_PRICE_THRESHOLD = 15;
    private static final double WEEKEND_SURCHARGE_RATE = 0.01;

    private static final double DISCOUNT_PRICE_THRESHOLD = 10;
    private static final double STANDARD_DISCOUNT_PER_UNIT = 1.0;
    private static final double VIP_DISCOUNT_PER_UNIT = 0.5;
    private static final double VIP_IMPORTED_DISCOUNT_PER_UNIT = 0.25;

    private static final int LOYALTY_BONUS_THRESHOLD = 100;
    private static final int LOYALTY_BONUS_POINTS = 10;

    private static Map<String, Object> makeItem(String name, double price, boolean taxExempt, boolean imported,
            int quantity) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("price", price);
        item.put("taxExempt", taxExempt);
        item.put("imported", imported);
        item.put("quantity", quantity);
        return item;
    }

    public void calculate(boolean isWeekend, boolean vip) {
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(makeItem("Book", 12.49, false, false, 1));
        items.add(makeItem("Music CD", 14.99, false, true, 2));
        items.add(makeItem("Headache pills", 9.75, true, false, 3));
        items.add(makeItem("Chocolate bar", 0.85, true, true, 5));

        double total = 0;
        double taxes = 0;
        double saved = 0;
        int loyaltyPoints = 0;

        for (Map<String, Object> item : items) {
            double tax = calculateTaxPerUnit(item, isWeekend);
            int quantity = (int) item.get("quantity");

            double priceBeforeDiscounts = ((double) item.get("price") + tax) * quantity;
            double finalPrice = applyBulkDiscount(applyUnitDiscounts(item, priceBeforeDiscounts, vip), quantity);

            saved += priceBeforeDiscounts - finalPrice;
            total += finalPrice;
            taxes += tax * quantity;
            loyaltyPoints = addLoyaltyPoints(loyaltyPoints, finalPrice, vip);

            printIndividualItem((String) item.get("name"), quantity, finalPrice);
        }

        printTally(taxes, total, saved, loyaltyPoints);
    }

    /**
     * Tax for a single unit, built up from the following rules:
     * - imported items: 10% import duty, plus 2% if the price is over 15
     * - non-exempt items: 5% basic sales tax, plus 1% on weekends
     */
    private static double calculateTaxPerUnit(Map<String, Object> item, boolean isWeekend) {
        double price = (double) item.get("price");
        boolean taxExempt = (boolean) item.get("taxExempt");
        boolean imported = (boolean) item.get("imported");

        double tax = 0;
        if (imported) {
            tax += price * IMPORT_DUTY_RATE;
            if (price > LUXURY_PRICE_THRESHOLD) {
                tax += price * LUXURY_IMPORT_SURCHARGE_RATE;
            }
        }

        if (!taxExempt) {
            tax += price * BASIC_SALES_TAX_RATE;
            if (isWeekend) {
                tax += price * WEEKEND_SURCHARGE_RATE;
            }
        }

        return tax;
    }

    /**
     * Flat per-unit discounts on non-exempt items priced over 10.
     * VIPs get an extra discount, and a further one if the item is imported.
     */
    private static double applyUnitDiscounts(Map<String, Object> item, double priceBeforeDiscounts, boolean vip) {
        double price = (double) item.get("price");
        boolean taxExempt = (boolean) item.get("taxExempt");
        boolean imported = (boolean) item.get("imported");
        int quantity = (int) item.get("quantity");

        if (price <= DISCOUNT_PRICE_THRESHOLD || taxExempt) {
            return priceBeforeDiscounts;
        }

        double discountPerUnit = STANDARD_DISCOUNT_PER_UNIT;
        if (vip) {
            discountPerUnit += VIP_DISCOUNT_PER_UNIT;
            if (imported) {
                discountPerUnit += VIP_IMPORTED_DISCOUNT_PER_UNIT;
            }
        }

        return priceBeforeDiscounts - discountPerUnit * quantity;
    }

    private static double applyBulkDiscount(double price, int quantity) {
        if (quantity >= 5) {
            return price * 0.85;
        } else if (quantity >= 3) {
            return price * 0.90;
        } else if (quantity > 1) {
            return price * 0.95;
        }

        return price;
    }

    /**
     * VIPs earn double points; once the running total exceeds 100, each item earns
     * a bonus.
     */
    private static int addLoyaltyPoints(int currentPoints, double finalPrice, boolean vip) {
        int points = currentPoints + (int) (vip ? finalPrice * 2 : finalPrice);
        if (points > LOYALTY_BONUS_THRESHOLD) {
            points += LOYALTY_BONUS_POINTS;
        }

        return points;
    }
}
