import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * RadialImage - Creates the Radial Image Object
 */
public class RadialImage extends JPanel
{
   // Default Serial Id
   private static final long serialVersionUID = 1L;

   // Radial Image 
   private BufferedImage _radialImage;

   // Rotated Radial Image
   private BufferedImage _rotatedImage;

   // Video X/Y Offset
   private static final int VIDEO_X_OFFSET = 30;
   private static final int VIDEO_Y_OFFSET = 0;

   // Width/Height of the Radial Image
   private int _width;
   private int _height;

   // Center of Radial Image
   private int _centerX;
   private int _centerY;

   // Number of Spokes on Radial Image
   private final int _spokes;

   // Initial Endpoint of first Radial Image Spoke
   private double _initX;
   private double _initY;

   // Deg of Rotation
   private double _theta;

   /**
    * Constructor
    *
    * @param width  - The Width of the Image
    * @param height - The Height of the Image
    * @param spokes - The Number of Spokes on the Radial Image
    */
   public RadialImage(final int width, final int height, final int spokes)
   {
      // Initialize a plain white image
      _radialImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      // Initialize Width/Height
      _width = width;
      _height = height;

      // Initialize Center X and Center Y
      _centerX = width/2;
      _centerY = height/2;

      // Initialize Degree of Rotation
      _theta = 0;

      // Initialize Initial X,Y Endpoint of First Spoke
      _initX = (_width/2.0);
      _initY = 0;

      // Initialize Spokes
      _spokes = spokes;

      // Initialize Radial Image
      _radialImage = generateImage(_spokes, _initX, _initY);
   }

   @Override
   public void paintComponent(final Graphics g) 
   {
      super.paintComponent(g);
      if (_rotatedImage != null) 
      {
         Graphics2D g2d = (Graphics2D) g.create();
         g2d.drawImage(_rotatedImage, VIDEO_X_OFFSET, VIDEO_Y_OFFSET, this);
         g2d.dispose();
      }
   }

   /**
    * getBufferedImage
    *
    * @return BufferedImage
    */
   public BufferedImage getBufferedImage()
   {
      return _radialImage;
   }

   /**
    * generateImage - Creates Radial Image with n spokes
    *
    * @param numLines
    */
   public BufferedImage generateImage(final int spokes, final double endX, final double endY)
   {
      // Initialize new Radial Image
      BufferedImage img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);

      // Iterate over Y coordinates of Image
      for(int y = 0; y < _height; y++)
      {
         // Iterate over X coordinates of Image
         for(int x = 0; x < _width; x++)
         {
            // Get Pixel Value
            byte r = (byte) 255;
            byte g = (byte) 255;
            byte b = (byte) 255;
            int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

            // Set Pixel of Image to White
            img.setRGB(x,y,pix);
         }
      }

      // Draw Frame around Image
      drawLine(img, 0, 0, _width-1, 0, Color.BLACK,1);                 // top edge
      drawLine(img, 0, 0, 0, _height-1, Color.BLACK,1);                // left edge
      drawLine(img, 0, _height-1, _width-1, _height-1, Color.BLACK,1); // bottom edge
      drawLine(img, _width-1, _height-1, _width-1, 0, Color.BLACK,1);  // right edge

      // Get Degrees of Spoke Rotation
      double deg = (360.0/spokes);
      double theta = Math.toRadians(deg);

      // Initialize Line Endpoint
      double x1 = endX;
      double y1 = endY;

      // Initialize Draw X/Y
      double drawX = endX;
      double drawY = endY;

      // Iterate over the Remaining Lines
      for(int i = 0; i < spokes; ++i)
      {
         // 1.) Translate so initial point is at origin
         double translateX = x1 - _centerX;
         double translateY = y1 - _centerY;

         // 2.) Rotate around center point
         double rotateX = (translateX * Math.cos(theta)) - (translateY * Math.sin(theta));
         double rotateY = (translateY * Math.cos(theta)) + (translateX * Math.sin(theta));

         // 3.) Undo Translation
         x1 = rotateX + _centerX;
         y1 = rotateY + _centerY;

         // 4.) Extend Lines
         double m = calculateSlope(0.0, 0.0, rotateX, rotateY);
         double b = getYIntercept(x1, y1, m);

         // Check if Slope is not 0 or Undefined
         if((m != Double.NaN) && (Math.abs(m) > 0.001))
         {
            // Quadrant 1
            if(m > 0 && y1 < _centerY)
            {
               drawX = _width;
               drawY = (-m*drawX) + b;
            }
            // Quadrant 2
            else if(m < 0 && y1 < _centerY)
            {
               drawX = 0;
               drawY = (m*drawX) + b;
            }
            // Quadrant 3
            else if(m > 0 && y1 > _centerY)
            {
               drawX = 0;
               drawY = (m*drawX) + b;
            }
            // Quadrant 4
            else if(m < 0 && y1 > _centerY)
            {
               drawX = _width;
               drawY = (-m*drawX) + b;
            }
         }
         else
         {
            drawX = x1;
            drawY = y1;
         }

         // 5.) Draw the Line
         drawLine(img, _centerX, _centerY, (int)Math.round(drawX), (int)Math.round(drawY), Color.BLACK, 1);

//         // TODO: Uncomment to Test Rotation by Coloring one spoke RED
//         if(i == spokes-1)
//         {
//            // 5.) Draw the Line
//            drawLine(img, _centerX, _centerY, (int)Math.round(drawX), (int)Math.round(drawY), Color.RED, 5);
//         }
//         else
//         {
//            // 5.) Draw the Line
//            drawLine(img, _centerX, _centerY, (int)Math.round(drawX), (int)Math.round(drawY), Color.BLACK, 1);
//         }
      }

      return img;
   }

   /**
    * rotate - Rotates the Radial Image
    *
    * @param s - Speed of Rotation
    * @param fps - Frames per Second
    */
   public void rotate(final double s, final double fps)
   {
      rotate(s, fps, false);
   }

   /**
    * rotate - Rotates the Radial Image
    *
    * @param s             - Speed of Rotation
    * @param fps           - Frames per Second
    * @param isAntiAlaised - AntiAliasing Indicator
    */
   public void rotate(final double s, final double fps, final boolean isAntiAliased)
   {
      rotate(s, fps, isAntiAliased, false);
   }

   /**
    * rotate - Rotates the Radial Image
    *
    * @param s                      - Speed of Rotation
    * @param fps                    - Frames per Second
    * @param isAntiAlaised          - AntiAliasing Indicator
    * @param isTemporarlAntiAliased - Temporal AntiAliasing Indicator
    */
   public void rotate(final double s, final double fps, final boolean isAntiAliased, final boolean isTemporalAntiAliased)
   {
      // Refresh Rate
      final int refreshRate = (int) (1000/(fps));

      // Get Rotation per Frame
      _theta = (s * (360.0/fps));

      //  Normalize Rotation (0 < theta <= 360)
      while(_theta > 360.0)
      {
         _theta -= 360.0;
      }

      // Check if theta will eclipse 1 Rotation in next Frame
      if(_theta > 180)
      {
         // Get observed turn per frame
         _theta = 360 - _theta;
      }

      // Create Animation Timer
      Timer timer = new Timer(refreshRate, new ActionListener()
      {
         @Override
         public void actionPerformed(final ActionEvent e)
         {
            // Is Temporally and Spatially AntiAliased
            if(isTemporalAntiAliased && isAntiAliased)
            {
               BufferedImage image = rotateTheImage(_radialImage, _theta);
               BufferedImage temporalImage = ImageDisplay.temporalFilterImage(image);
               _rotatedImage = ImageDisplay.filterImage(temporalImage);
            }
            // Is Temporally AntiAliased
            else if(isTemporalAntiAliased)
            {
               BufferedImage image = rotateTheImage(_radialImage, _theta);
               _rotatedImage = ImageDisplay.temporalFilterImage(image);
            }
            // Is Spatially AntiAliased
            else if(isAntiAliased)
            {
               BufferedImage image = rotateTheImage(_radialImage, _theta);
               _rotatedImage = ImageDisplay.filterImage(image);
            }
            // Allow liasing
            else
            {
               _rotatedImage = rotateTheImage(_radialImage, _theta);
            }

            // Update Display
            repaint();
         }
      });
      timer.start();
   }

   /**
    * rotateTheImage - Rotates the Initial Line of the Radial Image, and rotates 
    *                  all other lines based off the first rotated line
    *
    * @param img            - The image to be rotate
    * @param angle          - Degrees Image should be rotated by
    * @return BufferedImage - The Rotated Image
    */
   public BufferedImage rotateTheImage(final BufferedImage img, final double angle)
   {
      // Find Degree of Rotation
      final double theta = Math.toRadians(angle);

      // Translate so initial point is at origin
      final double translateX = _initX - (_width/2.0);
      final double translateY = _initY - (_height/2.0);

      // Rotate around center point
      final double rotateX = (translateX * Math.cos(theta)) - (translateY * Math.sin(theta));
      final double rotateY = (translateY * Math.cos(theta)) + (translateX * Math.sin(theta));

      // Undo Translation
      _initX = rotateX + (_width/2.0);
      _initY = rotateY + (_height/2.0);

      // Generate Radial Image
      final BufferedImage rotatedImage = generateImage(_spokes, _initX, _initY);

      return rotatedImage;
   }

   /**
    * drawLine - Draws a black line on a given buffered image from the pixel
    *            defined by (x1, y1) to (x2, y2)
    *
    * @param image
    * @param x1
    * @param y1
    * @param x2
    * @param y2
    */
   private void drawLine(final BufferedImage image, final int x1, final int y1, final int x2, final int y2, final Color c, final int s)
   {
      Graphics2D g = image.createGraphics();
      g.setColor(c);
      g.setStroke(new BasicStroke(s));
      g.drawLine(x1, y1, x2, y2);
      g.drawImage(image, 0, 0, null);
   }

   /**
    * calculateSlope - Calculates the Slope of the Spoke (x1,y1) to (x2,y2)
    *
    * @param x1
    * @param y1
    * @param x2
    * @param y2    
    * @return double - The Slope of the LIne
    */
   private double calculateSlope(double x1, double y1, double x2, double y2)
   {
      // Initialize Slope Value
      double m = 0;
      double deltaX = x2 - x1;
      double deltaY = y1 - y2; // Reversed due to Java Coordinate Space

      // Set Undefined Slope (divide by 0)
      if(Math.abs(deltaX) > 0.00001)
      {
         m = deltaY/deltaX;
      }
      else
      {
         m = Double.NaN;
      }

      return m;
   }

   /**
    * getYIntercept - Gets the Y Intercept of the Line
    *
    * @param x1      - The X value of the Function
    * @param y1      - The Y value of the Function
    * @param m       - The Slope of the Line
    * @return double - The Y Intercept
    */
   private double getYIntercept(double x1, double y1, double m)
   {
      // Calculate Y Intercept
      double b = y1 + (m*x1);

      return b;
   }
}