/**
 * part2Main
 *
 * This Program deals with Temporal Aliasing
 */
public class part2Main 
{
   /**
    * main
    *
    * @param args
    */
   public static void main(String[] args)
   {
      // Generate the Images to be displayed
      ImageDisplay ren = new ImageDisplay();

      // Ensure the Program has 3 arguments passed
      if(args.length < 3 || args.length >= 4)
      {
         // Print Usage Statement and End Program
         System.out.println("Usage: myPart2 numLines rotsPerSec fps");
         System.exit(1);
      }
      else
      {
         // Read a parameters from command line
         final String numLinesStr = args[0];
         final String rotsPerSecStr = args[1];
         final String fpsStr = args[2];

         try
         {
            // Get Arguments
            final int numLines = Integer.parseInt(numLinesStr);
            final double rotsPerSec = Double.parseDouble(rotsPerSecStr);
            final double fps = Double.parseDouble(fpsStr);

            // Show the Videos
            ren.showVideos(numLines, rotsPerSec, fps);
         }
         catch(Exception e)
         {
            // Print Stack Trace
            e.printStackTrace();

            // Print Usage Statement and End Program
            System.out.println("Usage: extraCreditMain numLines rotsPerSec fps");
            System.exit(1);
         }
      }
   }
}
