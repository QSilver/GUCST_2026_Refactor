package org.example;

public class Main {
    private static final BillCalc CALCULATOR = new BillCalc();

    /**
     * Tax:
     *      Sales tax is 5% and 6% on weekends
     *      Import tax is 10% plus an additional 2% for items over $15
     *      Food items are exempt from basic sales tax
     * Discounts:
     *      $1 off items over $10 that are not food
     *      Additional $0.50 off for VIP customers on items over $10
     *      Additional $0.25 off for VIP customers on imported items over $10
     * Bulk discounts:
     *      15% off for quantities of 5 or more
     *      10% off for quantities of 3 or more
     *      5% off for quantities of 2
     *  Loyalty program:
     *    If VIP, earn 2 points for every $1 spent, otherwise earn 1 point for every $1 spent
     *    If over 100 points earned, earn an additional 10 points
     *    Reach Silver status at 50 points
     *    Reach Gold status at 100 points
     *    Reach Platinum status at 200 points
     */
    public static void main() {
        CALCULATOR.calculate(false, true);
    }
}