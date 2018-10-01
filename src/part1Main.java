/**
 * part1Main
 *
 * This Program deals with Spatial Resampling and Aliasing
 */
public class part1Main 
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
         System.out.println("Usage: part1Main numLines scale isAntiAliased");
         System.exit(1);
      }
      else
      {
         // Read a parameters from command line
         final String numLinesStr = args[0];
         final String scaleFactorStr = args[1];
         final String aliasStr = args[2];

         try
         {
            // Get Arguments
            final int numLines = Integer.parseInt(numLinesStr);
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

            // Show the Images
            ren.showImages(numLines, scale, isAntiAliased);
         }
         catch(Exception e)
         {
            // Print Stack Trace
            e.printStackTrace();

            // Print Usage Statement and End Program
            System.out.println("Usage: part1Main numLines scale isAntiAliased");
            System.exit(1);
         }
      }
   }
}
