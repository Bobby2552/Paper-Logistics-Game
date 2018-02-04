import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class Driver {
    public static void main (String args[]) {
        long totalIterations = 0;
        int max = 0;
        int [] bestBuy = new int[8];
        double [] bestSell = new double[6];
        int [] buy = new int[8];
        double [] sell = new double[6];
        final long startTime = System.nanoTime();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                buy[i] = j;
                for (int k = 5; k >= 0; k--) {
                    for (int l = 0; l <= 100; l++) {
                        sell[k] = .01 * l;
                        Game game = new Game(buy, sell);
                        int result = game.play();
                        if (result > max) {
                            max = result;
                            bestBuy = buy.clone();
                            bestSell = sell.clone();
                        }
                        totalIterations += Game.ITERATIONS;
                    }
                }
            }
        }
        final long duration = System.nanoTime() - startTime;
        System.out.println("Best strategy ($" + max + "):");
        for (int i = 0; i < 8; i++) {
            System.out.println("At price $" + (i + 1) + ", buy " + bestBuy[i] + " raw products.");
        }
        System.out.println("");
        for (int i = 0; i < 6; i++) {
            System.out.println("At price $" + (i + 1) + ", sell " + (bestSell[i] * 100) + "% of stock.");
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        System.out.println("\nTested using " + numberFormat.format(totalIterations) + " iterations, " +
                numberFormat.format(Game.ITERATIONS) + " per set, in " + (duration / 1000000000) + " seconds.");
    }
}
