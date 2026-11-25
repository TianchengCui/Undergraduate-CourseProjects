public class DoubleUp {
   /**
     * Returns a new string where each character of the given string is repeated twice.
     * Example: doubleUp("hello") -> "hheelllloo"
     */
   public static String doubleUp(String s) {
      int sLength=s.length();
      String newS = "";
      for (int i=0; i<sLength; i++){
         int index=sLength - i -1;
         char letter = s.charAt(i);
         newS += letter;
         newS += letter;
      }
      return newS;
   }
   
   public static void main(String[] args) {
      String s = doubleUp("hello");
      System.out.println(s);
      
      System.out.println(doubleUp("cat"));
   }
}