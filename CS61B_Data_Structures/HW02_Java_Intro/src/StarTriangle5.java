public class StarTriangle5 {
   /**
     * Prints a right-aligned triangle of stars ('*') with 5 lines.
     * The first row contains 1 star, the second 2 stars, and so on. 
     */
   public static void starTriangle5() {
      int n;
      for (n = 1; n <= 5; n++) {
         String s = "";
         for (int i = 1; i <= 5 - n; i++) {
            s += " ";
         }
         for (int i = 1; i <= n; i++) {
            s += "*";
         }
         System.out.println(s);
      }
   }
   
   public static void main(String[] args) {
      starTriangle5();
   }
}