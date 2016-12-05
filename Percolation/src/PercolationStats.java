import java.util.Random;

/**
 * @author EthanPark <br/>
 * @version 1.0
 */
public class PercolationStats {

   private int grid;
   private int trials;

   private double[] tryTime;

   // perform trials independent experiments on an n-by-n grid
   public PercolationStats(int n, int trials) {
      if (n <= 0 || trials <= 0)
         throw new IllegalArgumentException();

      this.grid = n;
      this.trials = trials;
      this.tryTime = new double[trials];

      Random random = new Random();
      for (int i = 0; i < trials; i++) {
         Percolation p = new Percolation(n);
         int tryCount = 0;

         while (true) {
            int row = random.nextInt(grid) + 1;
            int col = random.nextInt(grid) + 1;

            if (p.isOpen(row, col))
               continue;

            p.open(row, col);
            tryCount++;

            if (p.percolates())
               break;
         }
         tryTime[i] = 1.0 * tryCount / (n * n);
      }
   }

   public double mean() {
      double result = 0;
      for (double d : tryTime) {
         result += d;
      }

      return result / trials;
   }

   public double stddev() {
      double mean = mean();

      double sSquare = 0;

      for (double d : tryTime) {
         sSquare += (d - mean) * (d - mean);
      }

      return Math.sqrt(sSquare / (trials - 1));
   }

   public double confidenceLo() {
      return mean() - 1.96 * stddev() / Math.sqrt(trials);
   }

   public double confidenceHi() {
      return mean() + 1.96 * stddev() / Math.sqrt(trials);
   }

   public void output() {
      System.out.println(String.format("mean                    = %s", mean()));
      System.out
            .println(String.format("stddev                  = %s", stddev()));
      System.out.printf("95%% confidence interval = %s, %s%n", confidenceLo(),
            confidenceHi());
   }

   public static void main(String[] args) {
      int grid = Integer.parseInt(args[0]);
      int trials = Integer.parseInt(args[1]);

      PercolationStats stats = new PercolationStats(grid, trials);
      stats.output();
   }
}
