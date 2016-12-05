/**
 * @author EthanPark <br/>
 * @version 1.0
 */
public class Percolation {
   private UnionFind uf;
   private boolean[] array;
   private int width;

   private int start;

   private int end;

   // create n-by-n grid, with all sites blocked
   public Percolation(int n) {
      if (n < 1)
         throw new IllegalArgumentException();

      width = n;
      uf = new UnionFind(n * n + 2);
      array = new boolean[n * n];

      start = 0;
      end = n * n + 1;
   }

   private void checkBounds(int row, int col) {
      if (row < 1 || row > width || col < 1 || row > width)
         throw new IndexOutOfBoundsException();
   }

   // open site (row, col) if it is not open already
   public void open(int row, int col) {
      checkBounds(row, col);

      if (isOpen(row, col))
         return;

      int index = index(row, col);
      array[index - 1] = true;

      if (row == 1) {
         uf.union(start, index);

         tryUnion(index, row + 1, col);

         if (col == 1) {
            tryUnion(index, row, col + 1);
         } else if (col == width) {
            tryUnion(index, row, col - 1);
         } else {
            tryUnion(index, row, col + 1);
            tryUnion(index, row, col - 1);
         }
      } else if (row == width) {
         uf.union(end, index);

         tryUnion(index, row - 1, col);

         if (col == 1) {
            tryUnion(index, row, col + 1);
         } else if (col == width) {
            tryUnion(index, row, col - 1);
         } else {
            tryUnion(index, row, col + 1);
            tryUnion(index, row, col - 1);
         }
      } else {
         tryUnion(index, row - 1, col);
         tryUnion(index, row + 1, col);

         if (col == 1) {
            tryUnion(index, row, col + 1);
         } else if (col == width) {
            tryUnion(index, row, col - 1);
         } else {
            tryUnion(index, row, col + 1);
            tryUnion(index, row, col - 1);
         }
      }
   }

   private void tryUnion(int origin, int row, int col) {
      if (isOpen(row, col))
         uf.union(origin, index(row, col));
   }

   // is site (row, col) open?
   public boolean isOpen(int row, int col) {
      return array[index(row, col) - 1];
   }

   private int index(int row, int col) {
      return (row - 1) * width + col;
   }

   // is site (row, col) full?
   public boolean isFull(int row, int col) {
      checkBounds(row, col);

      return uf.connect(start, index(row, col))
            && uf.connect(end, index(row, col));
   }

   // does the system percolate?
   public boolean percolates() {
      return uf.connect(start, end);
   }
}

class UnionFind {
   private int[] array;

   public UnionFind(int n) {
      array = new int[n];

      for (int i = 0; i < n; i++) {
         array[i] = i;
      }
   }

   public void union(int a, int b) {
      if (root(a) == root(b))
         return;

      if (height(a) >= height(b))
         array[root(b)] = root(a);
      else
         array[root(a)] = root(b);
   }

   public int root(int a) {
      while (array[a] != a) {
         a = array[a];
      }

      return a;
   }

   public int height(int a) {
      int result = 1;
      while (array[a] != a) {
         result++;
         a = array[a];
      }

      return result;
   }

   public boolean connect(int a, int b) {
      return root(a) == root(b);
   }
}
