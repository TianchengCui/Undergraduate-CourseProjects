public class PrintIndexed {
   /**
     * Prints each character of a given string followed by the reverse of its index.
     * Example: printIndexed("hello") -> h4e3l2l1o0
     */
   public static void printIndexed(String s) {
      int sLength=s.length();
      String newS = "";
      for (int i=0; i<sLength; i++){
         int index=sLength - i -1;
         char letter = s.charAt(i);
         newS += letter;
         newS += index;
      }
      System.out.println(newS);
   }

   public static void main(String[] args) {
      printIndexed("hello");
      printIndexed("cat"); // should print c2a1t0
   }
}