
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.*;

/**
 * ImageDisplay
 */
public class ImageDisplay
{
   // Content Pane
   JFrame _frame;

   // Original and Modified Image Labels
   JLabel _lbIm1;
   JLabel _lbIm2;

   // Width/Height of the FRAME
   private static final int FRAME_WIDTH = 1250;
   private static final int FRAME_HEIGHT = 750;

   // Width/Height of the Image
   private static final int WIDTH = 512;
   private static final int HEIGHT = 512;

   // Default FPS
   private static final int DEFAULT_FPS = 60;

   // Threshold for Temporal Filtering
   private static final int T_DELTA = 128;

   // GridBag Constraints
   private GridBagConstraints _c;

   /**
    * Constructor
    */
   public ImageDisplay()
   {
      // Use labels to display the images
      _frame = new JFrame();
      GridBagLayout gLayout = new GridBagLayout();
      _frame.getContentPane().setLayout(gLayout);
      _c = new GridBagConstraints();
   }

   /**
    * showImages - Shows the Original and the Modified Scale Image
    *
    * @param numLines      - Number of Spokes in Radial Image
    * @param scale         - Scale Factor of Modified Image
    * @param isAntiAliased - Indicator if anti-aliasing is performed
    */
   public void showImages(final int numLines, final double scale, final boolean isAntiAliased)
   {
      // Initialize Original Image Label
      final JLabel lbText1 = new JLabel("Original image (Left)");
      lbText1.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Modified Image Label
      final JLabel lbText2 = new JLabel("Image after modification (Right)");
      lbText2.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Original Image
      final RadialImage origImage = new RadialImage(WIDTH, HEIGHT, numLines);
      _lbIm1 = new JLabel(new ImageIcon(origImage.getBufferedImage()));

      // Initialize Scaled Image
      final BufferedImage scaledImage = scaleImage(origImage.getBufferedImage(), scale);

      // TODO: Uncomment to Run Tests for Analysis Questions Part 1
      // runAnalysisTest1();
      // runAnalysisTest2();

      // Check if anti-aliasing should be performed
      if(isAntiAliased)
      {
         // Apply Anti-Aliasing to Image
         final BufferedImage filteredImage = filterImage(scaledImage);
         _lbIm2 = new JLabel(new ImageIcon(filteredImage));
      }
      else
      {
         // Maintain Aliasing in Image
         _lbIm2 = new JLabel(new ImageIcon(scaledImage));
      }

      // Initialize Original Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weightx = 0.5;
      _c.gridx = 0;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText1, _c);

      // Initialize Modified Image Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weightx = 0.5;
      _c.gridx = 1;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText2, _c);

      // Initialize Original Image Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.gridx = 0;
      _c.gridy = 1;
      _frame.getContentPane().add(_lbIm1, _c);

      // Initialize Modified Image Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.gridx = 1;
      _c.gridy = 1;
      _frame.getContentPane().add(_lbIm2, _c);

      _frame.pack();
      _frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      _frame.setVisible(true);
   }

   /**
    * showVideos - Shows the Original and the Modified Videos
    *
    * @param numLines    - Number of Spokes in Radial Image
    * @param speedOfRots - Rotations per Second
    * @param fps         - Frames per Second
    */
   public void showVideos(final int numLines, final double rotsPerSec, final double fps)
   {
      // Initialize Original Image Label
      JLabel lbText1 = new JLabel("Original Video (Left)");
      lbText1.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Modified Image Label
      JLabel lbText2 = new JLabel("Video after modification (Right)");
      lbText2.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Original Video
      final RadialImage originalVideo = new RadialImage(WIDTH, HEIGHT, numLines);
      final RadialImage modifiedVideo = new RadialImage(WIDTH, HEIGHT, numLines);

      // Initialize Original Video Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.WEST;
      _c.weightx = 0.5;
      _c.gridx = 1;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText1, _c);

      // Initialize Modified Video Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weightx = 0.5;
      _c.gridx = 2;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText2, _c);

      // Initialize Original Video
      _c.fill = GridBagConstraints.BOTH;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weighty = 1.0;
      _c.gridx = 1;
      _c.gridy = 1;
      _frame.getContentPane().add(originalVideo, _c);

      // Initialize Modified Video
      _c.fill = GridBagConstraints.BOTH;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weighty = 1.0;
      _c.gridx = 2;
      _c.gridy = 1;
      _frame.getContentPane().add(modifiedVideo, _c);

      _frame.pack();
      _frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      _frame.setVisible(true);

      // Perform Animation on Images
      originalVideo.rotate(rotsPerSec, DEFAULT_FPS);
      modifiedVideo.rotate(rotsPerSec, fps);
   }

   /**
    * showVideos - Shows the Original and the Modified Videos
    *
    * @param numLines      - Number of Spokes in Radial Image
    * @param speedOfRots   - Rotations per Second
    * @param fps           - Frames per Second
    * @param scale         - Scale Factor of Modified Image
    * @param isAntiAliased - Is Temporal/Spatially AntiAliased
    */
   public void showVideos(final int numLines, final double rotsPerSec, final double fps, final double scale, final boolean isAntiAliased)
   {
      // Initialize Original Image Label
      JLabel lbText1 = new JLabel("Original Video (Left)");
      lbText1.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Modified Image Label
      JLabel lbText2 = new JLabel("Video after modification (Right)");
      lbText2.setHorizontalAlignment(SwingConstants.CENTER);

      // Initialize Original Image
      final RadialImage radialImage = new RadialImage(WIDTH, HEIGHT, numLines);

      // Initialize Scaled Image
      final int width = (int) (WIDTH * (1/scale));
      final int height = (int) (HEIGHT * (1/scale));
      final RadialImage scaledImage = new RadialImage(width, height, numLines);

      // Initialize Original Video Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.WEST;
      _c.weightx = 0.5;
      _c.gridx = 1;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText1, _c);

      // Initialize Modified Video Label Location
      _c.fill = GridBagConstraints.HORIZONTAL;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weightx = 0.5;
      _c.gridx = 2;
      _c.gridy = 0;
      _frame.getContentPane().add(lbText2, _c);

      // Initialize Original Video
      _c.fill = GridBagConstraints.BOTH;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weighty = 1.0;
      _c.gridx = 1;
      _c.gridy = 1;
      _frame.getContentPane().add(radialImage, _c);

      // Initialize Modified Video
      _c.fill = GridBagConstraints.BOTH;
      _c.anchor = GridBagConstraints.CENTER;
      _c.weighty = 1.0;
      _c.gridx = 2;
      _c.gridy = 1;
      _frame.getContentPane().add(scaledImage, _c);

      _frame.pack();
      _frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      _frame.setVisible(true);

      // Perform Animation on Images
      radialImage.rotate(rotsPerSec, DEFAULT_FPS);
      
      // Check if below Nyquist Threshold
      if(fps < 2*rotsPerSec)
      {
         scaledImage.rotate(rotsPerSec, fps, isAntiAliased, true);
      }
      else
      {
         scaledImage.rotate(rotsPerSec, fps, isAntiAliased, false);
      }
   }

   /**
    * filterImage - Filters Image by taking an average of a 3x3 square surrounding pixel in question
    *
    * @param image - The Buffered Image to Filter
    * @return BufferedImage - The Filtered Image
    */
   public static BufferedImage filterImage(final BufferedImage image)
   {
      // Initialize new Anti-Aliased Image
      BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

      // Get Width/Height
      final int width = image.getHeight();
      final int height = image.getHeight();

      // Iterate over Images Height
      for(int j = 0; j < height; ++j)
      {
         // Iterate over Images Width
         for(int i = 0; i < width; ++i)
         {
            // Initialize RGB accumulators
            int redTotal = 0;
            int greenTotal = 0;
            int blueTotal = 0;

            // Check if not border pixels of image
            if((i != 0) && (i != width-1) && (j != 0) && (j != height-1))
            {
               // Iterate over 3x3 grid surrounding Pixel(i,j)
               for(int x = (i-1); x <= (i+1); ++x)
               {
                  for(int y = (j-1); y <= (j+1); ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/9;
               final int newGreen = greenTotal/9;
               final int newBlue = blueTotal/9;

               // Get new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Far Right Row
            else if((i != 0) && (j != 0) && (j != height-1))
            {
               // Iterate over 2x3 grid surrounding Pixel(i,j)
               for(int x = (i-1); x <= i; ++x)
               {
                  for(int y = (j-1); y <= (j+1); ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/6;
               final int newGreen = greenTotal/6;
               final int newBlue = blueTotal/6;

               // Get new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Far Left Row
            else if((i != width-1) && (j != 0) && (j != height-1))
            {
               // Iterate over 2x3 grid surrounding Pixel(i,j)
               for(int x = i; x <= i+1; ++x)
               {
                  for(int y = (j-1); y <= (j+1); ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/6;
               final int newGreen = greenTotal/6;
               final int newBlue = blueTotal/6;

               // Get new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Top Row
            else if((i != 0) && (i != width-1) && (j != height-1))
            {
               // Iterate over 3x2 grid surrounding Pixel(i,j)
               for(int x = i-1; x <= i+1; ++x)
               {
                  for(int y = j; y <= (j+1); ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/6;
               final int newGreen = greenTotal/6;
               final int newBlue = blueTotal/6;

               // Get new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Bottom Row
            else if((i != 0) && (i != width-1) && (j != 0))
            {
               // Iterate over 3x2 grid surrounding Pixel(i,j)
               for(int x = (i-1); x <= (i+1); ++x)
               {
                  for(int y = (j-1); y <= j; ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/6;
               final int newGreen = greenTotal/6;
               final int newBlue = blueTotal/6;

               // Get new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Top Left Corner
            else if((i == 0) && (j==0))
            {
               // Iterate over 2x2 grid surrounding Pixel(i,j)
               for(int x = i; x <= i+1; ++x)
               {
                  for(int y = j; y <= j+1; ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/4;
               final int newGreen = greenTotal/4;
               final int newBlue = blueTotal/4;

               // Set new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Bottom Left Corner
            else if((i==0) && (j== height-1))
            {
               // Iterate over 2x2 grid surrounding Pixel(i,j)
               for(int x = i; x <= i+1; ++x)
               {
                  for(int y = j-1; y <= j; ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Colors
               final int newRed = redTotal/4;
               final int newGreen = greenTotal/4;
               final int newBlue = blueTotal/4;

               // Set new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               filteredImage.setRGB(i, j, pix);
            }
            // Check if Pixel is in Top Right Corner
            else if((i==width-1) && (j==height-1))
            {   
               // Iterate over 2x2 grid surrounding Pixel(i,j)
               for(int x = i-1; x <= i; ++x)
               {
                  for(int y = j-1; y <= j; ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Pixel Colors
               final int newRed = redTotal/4;
               final int newGreen = greenTotal/4;
               final int newBlue = blueTotal/4;

               // Set new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
            // if Pixel is in Bottom Right Corner
            else
            {
               // Iterate over 2x2 grid surrounding Pixel(i,j)
               for(int x = i-1; x <= i; ++x)
               {
                  for(int y = j; y <= j+1; ++y)
                  {
                     // Get RGB Values at each Pixel
                     int pix = image.getRGB(x, y);
                     int red   = (pix & 0x00ff0000) >> 16;
                     int green = (pix & 0x0000ff00) >> 8;
                     int blue  =  pix & 0x000000ff;

                     redTotal += red;
                     greenTotal += green;
                     blueTotal += blue;
                  }
               }

               // Average Pixel Colors
               final int newRed = redTotal/4;
               final int newGreen = greenTotal/4;
               final int newBlue = blueTotal/4;

               // Set new Pixel Color
               int pix = 0xff000000 | ((newRed & 0xff) << 16) | ((newGreen & 0xff) << 8) | (newBlue & 0xff);

               // Set Pixel
               filteredImage.setRGB(i, j, pix);
            }
         }
      }

      return filteredImage;
   }

   /**
    * temporalFilterImage - Adds a Shaded Pixels "behind" Black Pixels
    *                       to create the illusion of movement in the 
    *                       desired direction
    *
    * @param image - The Buffered Image to Filter
    * @return BufferedImage - The Filtered Image
    */
   public static BufferedImage temporalFilterImage(final BufferedImage image)
   {
      // Initialize new Anti-Aliased Image
      BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

      // Get Width/Height
      final int width = image.getHeight();
      final int height = image.getHeight();

      // Iterate over Images Height
      for(int j = 0; j < height; ++j)
      {
         // Iterate over Images Width
         for(int i = 0; i < width; ++i)
         {
            // Initially Set Point
            filteredImage.setRGB(i, j, image.getRGB(i, j));
 
            // Quadrant 3
            if((i < (width/2)) && (j >= (height/2)) && (i != 0) && (j < (height-1)))
            {
               int pix = image.getRGB(i, j);
               int red   = (pix & 0x00ff0000) >> 16;
               int green = (pix & 0x0000ff00) >> 8;
               int blue  =  pix & 0x000000ff;

               // Check if Pixel is Black
               if((red < T_DELTA) && (green < T_DELTA) && (blue < T_DELTA))
               {
                  // Shade Pixel
                  red = 0;
                  blue = 0;
                  green = 0;

                  // Get new Pixel Color
                  int newPix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);

                  // Get Pixel Below Spoke
                  int pix2 = image.getRGB(i-1, j-1);
                  int red2   = (pix2 & 0x00ff0000) >> 16;
                  int green2 = (pix2 & 0x0000ff00) >> 8;
                  int blue2  =  pix2 & 0x000000ff;

                  // Check if Pixel is not black
                  if((red2 != 0) && (green2 != 0) && (blue2 != 0))
                  {
                     // Set Pixel
                     filteredImage.setRGB(i-1, j-1, newPix);
                  }
                  else
                  {
                     filteredImage.setRGB(i, j, image.getRGB(i, j));
                  }
               }
               else
               {
                  filteredImage.setRGB(i, j, image.getRGB(i, j));
               }
            }
            // Quadrant 2
            else if((i < (width/2)) && j < (height/2) && (i != 0) && (j != 0))
            {
               int pix = image.getRGB(i, j);
               int red   = (pix & 0x00ff0000) >> 16;
               int green = (pix & 0x0000ff00) >> 8;
               int blue  =  pix & 0x000000ff;

               // Check if Pixel is Black
               if((red < T_DELTA) && (green < T_DELTA) && (blue < T_DELTA))
               {
                  // Shade Pixel
                  red = red + red/2;
                  blue = blue + blue/2;
                  green = green + green/2;

                  // Get new Pixel Color
                  int newPix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);

                  // Get Pixel Below Spoke
                  int pix2 = image.getRGB(i-1, j-1);
                  int red2   = (pix2 & 0x00ff0000) >> 16;
                  int green2 = (pix2 & 0x0000ff00) >> 8;
                  int blue2  =  pix2 & 0x000000ff;

                  // Check if Pixel is not black
                  if((red2 != 0) && (green2 != 0) && (blue2 != 0))
                  {
                     // Set Pixel
                     filteredImage.setRGB(i-1, j-1, newPix);
                  }
                  else
                  {
                     filteredImage.setRGB(i, j, image.getRGB(i, j));
                  }
               }
               else
               {
                  filteredImage.setRGB(i, j, image.getRGB(i, j));
               }
            }
            // Quadrant 1
            else if((i >= (width/2)) && j < (height/2) && (i < (width-1)) && (j != 0))
            {
               int pix = image.getRGB(i, j);
               int red   = (pix & 0x00ff0000) >> 16;
               int green = (pix & 0x0000ff00) >> 8;
               int blue  =  pix & 0x000000ff;

               // Check if Pixel is Black
               if((red < T_DELTA) && (green < T_DELTA) && (blue < T_DELTA))
               {
                  // Shade Pixel
                  red = red + red/2;
                  blue = blue + blue/2;
                  green = green + green/2;

                  // Get new Pixel Color
                  int newPix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);

                  // Get Pixel Below Spoke
                  int pix2 = image.getRGB(i-1, j-1);
                  int red2   = (pix2 & 0x00ff0000) >> 16;
                  int green2 = (pix2 & 0x0000ff00) >> 8;
                  int blue2  =  pix2 & 0x000000ff;

                  // Check if Pixel is not black
                  if((red2 != 0) && (green2 != 0) && (blue2 != 0))
                  {
                     // Set Pixel
                     filteredImage.setRGB(i-1, j-1, newPix);
                  }
                  else
                  {
                     filteredImage.setRGB(i, j, image.getRGB(i, j));
                  }
               }
               else
               {
                  filteredImage.setRGB(i, j, image.getRGB(i, j));
               }
            }
            // Quadrant 4
            else if((i >= (width/2)) && (j >= (height/2)) && (i < (width-1)) && (j < (height-1)))
            {
               int pix = image.getRGB(i, j);
               int red   = (pix & 0x00ff0000) >> 16;
               int green = (pix & 0x0000ff00) >> 8;
               int blue  =  pix & 0x000000ff;

               // Check if Pixel is Black
               if((red < T_DELTA) && (green < T_DELTA) && (blue < T_DELTA))
               {
                  // Shade Pixel
                  red = red + red/2;
                  blue = blue + blue/2;
                  green = green + green/2;

                  // Get new Pixel Color
                  int newPix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);

                  // Get Pixel Below Spoke
                  int pix2 = image.getRGB(i+1, j-1);
                  int red2   = (pix2 & 0x00ff0000) >> 16;
                  int green2 = (pix2 & 0x0000ff00) >> 8;
                  int blue2  =  pix2 & 0x000000ff;

                  // Check if Pixel is White
                  if((red2 == 255) && (green2 == 255) && (blue2 == 255))
                  {
                     // Set Pixel
                     filteredImage.setRGB(i+1, j-1, newPix);
                  }
                  else
                  {
                     filteredImage.setRGB(i, j, image.getRGB(i, j));
                  }
               }
               else
               {
                  filteredImage.setRGB(i, j, image.getRGB(i, j));
               }
            }
            else
            {
               filteredImage.setRGB(i, j, image.getRGB(i, j));
            }
         }
      }

      return filteredImage;
   }

   /**
    * scaleImage - Performs a Bilinear Interpolation on an image
    *              to create a new Scaled Image
    *
    * @param origImage - The Original Image
    * @param scaled    - Scale Factor of which to Scale by
    * @return BufferedImage - A Scaled Buffered Image
    */
   private BufferedImage scaleImage(final BufferedImage origImage, final double scale)
   {
      // Get the new Dimensions of Scaled Image
      final double newWidth = (origImage.getWidth() * (1.0/scale));
      final double newHeight = (origImage.getHeight() * (1.0/scale));

      // Convert Dimensions to be whole numbers
      final int scaledWidth = (int)Math.round(newWidth);
      final int scaledHeight = (int)Math.round(newHeight);

      // Initialize new Scaled Image
      BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, origImage.getType());

      // Iterate over Scaled Image Height
      for(int y = 0; y < scaledHeight; ++y)
      {
         // Iterate over Scaled Image Width
         for(int x = 0; x < scaledWidth; ++x)
         {
            // Get Pixel Ratio of Scaled Image
            final double ratioX = x/newWidth;
            final double ratioY = y/newHeight;

            // Gets equivalent pixel on Original Image to Pixel on Scaled Image
            final int diffX;
            final int diffY;
            if(scale < 1)
            {
               diffX =  (int) Math.round(((ratioX) * (origImage.getWidth()-2)));
               diffY =  (int) Math.round(((ratioY) * (origImage.getHeight()-2)));
            }
            else
            {
               diffX =  (int) Math.round(((ratioX) * (origImage.getWidth()-1)));
               diffY =  (int) Math.round(((ratioY) * (origImage.getHeight()-1)));
            }

            // Get RGB Values for Point A
            int a = origImage.getRGB(diffX, diffY);
            int redA   = (a & 0x00ff0000) >> 16;
            int greenA = (a & 0x0000ff00) >> 8;
            int blueA  =  a & 0x000000ff;

            // Get RGB Values for Point B
            int b = origImage.getRGB(diffX+1, diffY);
            int redB   = (b & 0x00ff0000) >> 16;
            int greenB = (b & 0x0000ff00) >> 8;
            int blueB  =  b & 0x000000ff;

            // Get RGB Values for Point C
            int c = origImage.getRGB(diffX, diffY + 1);
            int redC   = (c & 0x00ff0000) >> 16;
            int greenC = (c & 0x0000ff00) >> 8;
            int blueC  =  c & 0x000000ff;

            // Get RGB Values for Point D
            int d = origImage.getRGB(diffX+1, diffY+1);
            int redD   = (d & 0x00ff0000) >> 16;
            int greenD = (d & 0x0000ff00) >> 8;
            int blueD  =  d & 0x000000ff;

            // Get RGB (Based on Interpolation formula Y = A(1-x)(1-y) + B(x)(1-y) + C(y)(1-x) + D(x)(y))
            int red = (int)(redA*(1-diffX)*(1-diffY) + redB*(diffX)*(1-diffY) + redC*(diffY)*(1-diffX) + redD*(diffX*diffY));
            int green = (int)(greenA*(1-diffX)*(1-diffY) + greenB*(diffX)*(1-diffY) + greenC*(diffY)*(1-diffX) + greenD*(diffX*diffY));
            int blue = (int)(blueA*(1-diffX)*(1-diffY) + blueB*(diffX)*(1-diffY) + blueC*(diffY)*(1-diffX) + blueD*(diffX*diffY));
   
            // Get new Pixel
            int pix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
   
            scaledImage.setRGB(x, y, pix);
         }
      }

      return scaledImage;
   }

   /**
    * getMse - Gets the Mean Squared Error between two Images with similar dimensions
    *
    * @param img1 - The First Image
    * @param img2 - The Second Image
    * @return double
    */
   private double getMse(final BufferedImage img1, final BufferedImage img2)
   {
      // Initialize Mean Square Error of Image
      double mse = 0.0;

      // Get the Size of the image
      final double size = img1.getWidth() * img2.getWidth();

      // Check that the images are the same dimensions
      if((img1.getWidth() == img2.getWidth()) && (img1.getHeight() == img2.getHeight()))
      {
         // Get Rasters for Images
         final Raster r1 = img1.getRaster();
         final Raster r2 = img2.getRaster();

         // Iterate over Width
         for(int x=0; x < img1.getWidth(); x++)
         {
            // Iterate over Height
            for(int y=0; y < img1.getHeight(); y++)
            {
               // Accumulate
               mse += Math.pow(r2.getSample(x,y,0)-r1.getSample(x,y,0), 2);
            }
         }

         // Take Average of Accumulation to get MSE
         mse = mse/size;
      }
      else
      {
         mse = Double.NaN;
      }

      return mse;
   }

   /**
    * getPsnr - Gets the Peak Signal to Noise Ratio between two images
    *
    * @param mse     - Mean Square Error Value
    * @return double - The PSNR Value
    */
   private double getPsnr(final double mse)
   {
      double m = Math.pow(255, 2) / mse;
      double psnr = 10.0 * (Math.log(m)/Math.log(10));
      return psnr;
   }

   /**
    * runAnalysisTest1 - Function to help run Analysis Test for Part 1 of Assignment
    */
   private void runAnalysisTest1()
   {
      int numLines = 360;

      for(int i=0; i < 1; ++i)
      {
         // Test for Analysis Questions Part 1
         final RadialImage image1 = new RadialImage(WIDTH, HEIGHT, 8);
         final RadialImage image2 = new RadialImage(WIDTH, HEIGHT, numLines);
         final double mse = getMse(image1.getBufferedImage(), image2.getBufferedImage());
         final double psnr = getPsnr(mse);

         System.out.println(" ");
         System.out.println("8, " + numLines);
         System.out.println("MSE: " + mse);
         System.out.println("PSNR: " + psnr);

         numLines = numLines +=8;
      }
   }

   /**
    * runAnalysisTest2 - Function to help run Analysis Test for Part 2 of Assignment
    */
   private void runAnalysisTest2()
   {
      // Check 1 512 Aliased Image versus 512 Anti-Aliased Image
      final RadialImage image1 = new RadialImage(WIDTH, HEIGHT, 16);
      final RadialImage image2 = new RadialImage(WIDTH, HEIGHT, 16);
      final BufferedImage filteredImage = filterImage(image2.getBufferedImage());
      final double mse = getMse(image1.getBufferedImage(), filteredImage);
      final double psnr = getPsnr(mse);

      System.out.println(" ");
      System.out.println("MSE: " + mse);
      System.out.println("PSNR: " + psnr);

      // Check 2 256 Aliased Image versus 256 Anti-Aliased Image
      final RadialImage image3 = new RadialImage(WIDTH, HEIGHT, 16);
      final RadialImage image4 = new RadialImage(WIDTH, HEIGHT, 16);
      final BufferedImage scaleImage3 = scaleImage(image3.getBufferedImage(), 2.0);
      final BufferedImage scaleImage4 = scaleImage(image4.getBufferedImage(), 2.0);
      final BufferedImage filteredImage2 = filterImage(scaleImage4);
      final double mse2 = getMse(scaleImage3, filteredImage2);
      final double psnr2 = getPsnr(mse2);

      System.out.println(" ");
      System.out.println("MSE: " + mse2);
      System.out.println("PSNR: " + psnr2);

      // Check 3 128 Aliased Image versus 128 Anti-Aliased Image
      final RadialImage image5 = new RadialImage(WIDTH, HEIGHT, 16);
      final RadialImage image6 = new RadialImage(WIDTH, HEIGHT, 16);
      final BufferedImage scaleImage5 = scaleImage(image5.getBufferedImage(), 4.0);
      final BufferedImage scaleImage6 = scaleImage(image6.getBufferedImage(), 4.0);
      final BufferedImage filteredImage3 = filterImage(scaleImage6);
      final double mse3 = getMse(scaleImage5, filteredImage3);
      final double psnr3 = getPsnr(mse3);
      
      System.out.println(" ");
      System.out.println("MSE: " + mse3);
      System.out.println("PSNR: " + psnr3);

      // Check 4 1024 Aliased Image versus 1024 Anti-Aliased Image
      final RadialImage image7 = new RadialImage(WIDTH, HEIGHT, 16);
      final RadialImage image8 = new RadialImage(WIDTH, HEIGHT, 16);
      final BufferedImage scaleImage7 = scaleImage(image7.getBufferedImage(), 0.5);
      final BufferedImage scaleImage8 = scaleImage(image8.getBufferedImage(), 0.5);
      final BufferedImage filteredImage4 = filterImage(scaleImage8);
      final double mse4 = getMse(scaleImage7, filteredImage4);
      final double psnr4 = getPsnr(mse4);
      
      System.out.println(" ");
      System.out.println("MSE: " + mse4);
      System.out.println("PSNR: " + psnr4);
   }
}