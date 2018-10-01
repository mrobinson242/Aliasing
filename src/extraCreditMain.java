/**
 * extraCreditMain 
 *
 * This Program deals with Temporal and Spatial Aliasing
 */
public class extraCreditMain 
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

      // Ensure the Program has 5 arguments passed
      if(args.length < 5 || args.length >= 6)
      {
         // Print Usage Statement and End Program
         System.out.println("Usage: extraCreditMain numLines rotsPerSec fps scale isAntiAliased");
         System.exit(1);
      }
      else
      {
         // Read a parameters from command line
         final String numLinesStr = args[0];
         final String rotsPerSecStr = args[1];
         final String fpsStr = args[2];
         final String scaleFactorStr = args[3];
         final String aliasStr = args[4];

         try
         {
            // Get Arguments
            final int numLines = Integer.parseInt(numLinesStr);
            final double rotsPerSec = Double.parseDouble(rotsPerSecStr);
            final double fps = Double.parseDouble(fpsStr);
            final double scale = Double.parseDouble(scaleFactorStr);
            final int antiAliased = Integer.parseInt(aliasStr);
            final boolean isAntiAliased;
            if(antiAliased == 1)
            {
               isAntiAliased = true;
            }
            else
            {
               isAntiAliased = false;
            }

            // Show the Videos
            ren.showVideos(numLines, rotsPerSec, fps, scale, isAntiAliased);
         }
         catch(Exception e)
         {
            // Print Stack Trace
            e.printStackTrace();

            // Print Usage Statement and End Program
            System.out.println("Usage: extraCreditMain numLines rotsPerSec fps scale isAntiAliased");
            System.exit(1);
         }
      }
   }
}
