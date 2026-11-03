package org.example;

import java.util.ArrayList;
import java.util.List;

import static org.example.Util.printIndividualItem;
import static org.example.Util.printTally;

public class BillThing {
    private static final double BASIC_TAX_RATE = 0.05;
    private static final double WEEKEND_TAX_RATE = 0.01;
    private static final double IMPORT_TAX_RATE = 0.10;
    private static final double ADDITIONAL_IMPORT_TAX_RATE = 0.02;
    private static final double DISCOUNT_THRESHOLD = 10.0;
    private static final double STANDARD_ITEM_DISCOUNT = 1.0;
    private static final double VIP_ITEM_DISCOUNT = 0.50;
    private static final double VIP_IMPORT_DISCOUNT = 0.25;
    private static final double QUANTITY_TWO_DISCOUNT = 0.95;
    private static final double QUANTITY_THREE_DISCOUNT = 0.90;
    private static final double QUANTITY_FIVE_DISCOUNT = 0.85;
    private static final int LOYALTY_POINT_BONUS_THRESHOLD = 100;
    private static final int LOYALTY_POINT_BONUS = 10;

    private static Product product(String name, double unitPrice, boolean food,
                                   boolean imported, int quantity) {
        return new Product(name, unitPrice, food, imported, quantity);
    }

    public void calculate(boolean weekend, boolean vip) {
        List<Product> products = createProducts();

        double totalPrice = 0;
        double totalTax = 0;
        double totalSaved = 0;
        int loyaltyPoints = 0;

        for (Product product : products) {
            double tax = calculateTax(product, weekend);
            double originalPrice = (product.unitPrice() + tax) * product.quantity();
            double finalPrice = applyDiscounts(product, originalPrice, vip);

            totalSaved += originalPrice - finalPrice;
            totalPrice += finalPrice;
            totalTax += tax * product.quantity();
            loyaltyPoints = calculateLoyaltyPoints(loyaltyPoints, finalPrice, vip);

            printIndividualItem(product.name(), product.quantity(), finalPrice);
        }
        printTally(totalTax, totalPrice, totalSaved, loyaltyPoints);
    }

    private List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        products.add(product("Book", 12.49, false, false, 1));
        products.add(product("Music CD", 14.99, false, true, 2));
        products.add(product("Headache pills", 9.75, true, false, 3));
        products.add(product("Chocolate bar", 0.85, true, true, 5));
        return products;
    }

    private double calculateTax(Product product, boolean weekend) {
        double tax = 0;
        if (product.imported()) {
            tax += product.unitPrice() * IMPORT_TAX_RATE;
            if (product.unitPrice() > 15) {
                tax += product.unitPrice() * ADDITIONAL_IMPORT_TAX_RATE;
            }
        }
        if (!product.food()) {
            tax += product.unitPrice() * BASIC_TAX_RATE;
            if (weekend) {
                tax += product.unitPrice() * WEEKEND_TAX_RATE;
            }
        }
        return tax;
    }

    private double applyDiscounts(Product product, double originalPrice, boolean vip) {
        double discountedPrice = applyItemDiscount(product, originalPrice, vip);
        return applyQuantityDiscount(discountedPrice, product.quantity());
    }

    private double applyItemDiscount(Product product, double price, boolean vip) {
        if (product.food() || product.unitPrice() <= DISCOUNT_THRESHOLD) {
            return price;
        }

        double discountedPrice = price - STANDARD_ITEM_DISCOUNT * product.quantity();
        if (vip) {
            discountedPrice -= VIP_ITEM_DISCOUNT * product.quantity();
            if (product.imported()) {
                discountedPrice -= VIP_IMPORT_DISCOUNT * product.quantity();
            }
        }
        return discountedPrice;
    }

    private double applyQuantityDiscount(double price, int quantity) {
        if (quantity >= 5) {
            return price * QUANTITY_FIVE_DISCOUNT;
        }
        if (quantity >= 3) {
            return price * QUANTITY_THREE_DISCOUNT;
        }
        if (quantity == 2) {
            return price * QUANTITY_TWO_DISCOUNT;
        }
        return price;
    }

    private int calculateLoyaltyPoints(int currentPoints, double finalPrice, boolean vip) {
        int pointsEarned = vip ? (int) (finalPrice * 2) : (int) finalPrice;
        int updatedPoints = currentPoints + pointsEarned;
        return updatedPoints > LOYALTY_POINT_BONUS_THRESHOLD
                ? updatedPoints + LOYALTY_POINT_BONUS
                : updatedPoints;
    }

    private record Product(String name, double unitPrice, boolean food,
                           boolean imported, int quantity) {
    }
}
