public class StarTriangleN {
   /**
     * Prints a right-aligned triangle of stars ('*') with N lines.
     * The first row contains 1 star, the second 2 stars, and so on. 
     */
   public static void starTriangle(int N) {
      int n;
      for (n = 1; n <= N; n++) {
         String s = "";
         for (int i = 1; i <= N - n; i++) {
            s += " ";
         }
         for (int i = 1; i <= n; i++) {
            s += "*";
         }
         System.out.println(s);
      }
   }
   
   public static void main(String[] args) {
      starTriangle(7);
   }
}