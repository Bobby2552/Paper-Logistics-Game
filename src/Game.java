import java.util.concurrent.ThreadLocalRandom;

public class Game {

    public static int ITERATIONS = 10000;

    private int currentCash = 10;
    private int incomingCashTomorrow = 0;
    private int incomingCashTwoDays = 0;
    private int outgoingCashTomorrow = 0;
    private int currentStock = 6;
    // Each position in the array is the amount of raw materials to buy at a price
    private int [] buy;
    // Each position in the array is the percentage of products to sell at a price
    private double [] sell;

    public Game(int [] buy, double [] sell) {
        this.buy = buy;
        this.sell = sell;
    }

    // Returns the average totalValue of ITERATIONS days.
    public int play() {
        // Used for averaging
        int aggregateValue = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            int result = runGame();
            aggregateValue += result;
            resetGame();
        }
        return aggregateValue / ITERATIONS;
    }

    @Override
    public String toString() {
        return "Game{" +
                "currentCash=" + currentCash +
                ", incomingCashTomorrow=" + incomingCashTomorrow +
                ", incomingCashTwoDays=" + incomingCashTwoDays +
                ", outgoingCashTomorrow=" + outgoingCashTomorrow +
                ", currentStock=" + currentStock +
                '}';
    }

    // Returns the totalValue from the game
    private int runGame() {
        for (int i = 0; i < 10; i++) {
            runDay();
        }
        return currentCash + incomingCashTwoDays + incomingCashTomorrow - outgoingCashTomorrow + (currentStock * rollSell());
    }

    private void resetGame() {
        currentCash = 10;
        incomingCashTomorrow = 0;
        incomingCashTwoDays = 0;
        outgoingCashTomorrow = 0;
        currentStock = 6;
    }

    private void runDay() {
        // Balance accounts for the day
        currentCash = currentCash + incomingCashTomorrow - outgoingCashTomorrow;
        outgoingCashTomorrow = 0;
        incomingCashTomorrow = incomingCashTwoDays;
        incomingCashTwoDays = 0;

        int buyPrice = rollBuy();
        int sellPrice = rollSell();

        // Sell products
        sell (sellPrice, (int) (currentStock * sell[sellPrice - 1]));

        // Is there enough money to buy product?
        if (currentCash + incomingCashTomorrow > buy[buyPrice - 1] * buyPrice) {
            buy(buyPrice, buy[buyPrice - 1]);
        }
        // If you want to buy 2, and only have the money for 1, still buy it.
        else if (buy[buyPrice - 1] == 2 && currentCash + incomingCashTomorrow > buyPrice) {
            buy(buyPrice, 1);
        }
//        System.out.print("Buy: $" + buyPrice + ", Sell: $" + sellPrice + "\t");
//        System.out.println(toString());
    }

    private void buy(int price, int quantity) {
        currentStock += quantity * 6;
        outgoingCashTomorrow = price * quantity;
    }
    private void sell(int price, int quantity) {
        currentStock -= quantity;
        incomingCashTwoDays = price * quantity;
    }

    private int rollBuy() {
        return ThreadLocalRandom.current().nextInt(1, 8 + 1);
    }

    private int rollSell() {
        return ThreadLocalRandom.current().nextInt(1, 6 + 1);
    }
}
