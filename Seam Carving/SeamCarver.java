/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {
        private Picture picture;
        // instance variables = actual width/height
        private int width;
        private int height;
        private double [][] energy;
    // create a seam carver object based on the given picture
        public SeamCarver(Picture picture)
        {
            if (picture == null) throw new IllegalArgumentException();
            this.picture = new Picture (picture);
            this.width = picture.width();
            this.height = picture.height();
            // System.out.println(height + " " + width);
            calculateEnergy();
        }

        // calculates energy for the picture
        private void calculateEnergy()
        {
            energy = new double [height][width];
            for (int i = 0; i < width; i++)
            {
                energy [0][i] = 1000;
                energy [height - 1][i] = 1000;
            }
            for (int i = 0; i < height; i++)
            {
                energy[i][0] = 1000;
                energy[i][width - 1] = 1000;
            }

            for (int i = 1; i < width - 1; i++)
            {
                for (int j = 1; j < height - 1; j++)
                    energy[j][i] = Math.sqrt(energyX(i, j) + energyY(i, j));

            }
        }

        // current picture
        public Picture picture()
        {
            return new Picture (picture);
        }

        // width of current picture
        public int width()
        {
            return width;
        }

        // height of current picture
        public int height()
        {
           return height;
        }

        // decode the 32-bit int to rgb representation
        private int decodeRed(int rgb)
        {
            return (rgb >> 16) & 0xFF;

        }

        private int decodeGreen(int rgb)
        {
            return (rgb >> 8) & 0xFF;
        }

        private int decodeBlue(int rgb)
        {
            return rgb & 0xFF;
        }
        // energy of pixel at column x and row y
        public double energy(int x, int y)
        {
            if (x < 0 || x > width-1 || y < 0 || y > height-1) throw new IllegalArgumentException();
            return energy [y][x];
        }

        // methods to calculate the energy for top/bottom and left/right pixels
        private double energyX(int x, int y)
        {
            int bottomPixelRGB;
            int topPixelRGB;
            topPixelRGB  = picture.getRGB(x, y+1);
            bottomPixelRGB = picture.getRGB(x, y-1);
            return calcGradient(topPixelRGB, bottomPixelRGB);
        }

        private double energyY(int x, int y)
        {
            int leftPixelRGB;
            int rightPixelRGB;
            rightPixelRGB  = picture.getRGB(x+1, y);
            leftPixelRGB = picture.getRGB(x-1, y);
            return calcGradient(rightPixelRGB, leftPixelRGB);
        }

        // methods that finds the difference between RGB representation in pixels
        private double calcGradient(int firstPixel, int secondPixel)
        {
            double redDifference = Math.abs(decodeRed(firstPixel) - decodeRed(secondPixel));
            double greenDifference = Math.abs(decodeGreen(firstPixel) - decodeGreen(secondPixel));
            double blueDifference = Math.abs(decodeBlue(firstPixel) - decodeBlue(secondPixel));
            return redDifference * redDifference + greenDifference * greenDifference + blueDifference * blueDifference;
        }

        // sequence of indices for horizontal seam
        public int[] findHorizontalSeam()
        {
            int [][] edgeTo = new int [height+2][width];
            double [][] distTo = new double [height+2][width];
            for (int i = 0; i < height+2; i++)
            {
                for (int j = 1; j < width; j++)
                {
                    distTo [i][j] = Double.POSITIVE_INFINITY;
                }
            }
            for (int i = 0; i < height+2; i++)
            {
                distTo [i][0] = 1000;
                edgeTo [i][0] = i;
            }


            for (int j = 1; j < width; j++)
            {
                for (int i = 1; i < height+1; i++)
                {
                   // relaxing left edge
                   if (distTo[i][j] > energy [i-1][j] + distTo [i][j-1])
                   {
                       distTo [i][j] = energy[i-1][j] + distTo [i][j-1];
                       edgeTo [i][j] = i-1;
                   }
                   // relax down-left edge
                   if (distTo[i][j] > energy [i-1][j] + distTo [i+1][j-1])
                   {
                       distTo[i][j] = energy [i-1][j] + distTo [i+1][j-1];
                       edgeTo[i][j] = i;
                   }
                   // relax top-left edge
                   if (distTo [i][j] > energy [i-1][j] + distTo [i-1][j-1])
                   {
                       distTo[i][j] = energy [i-1][j] + distTo [i-1][j-1];
                       edgeTo[i][j] = i-2;
                   }
                }
            }

            double min = Double.POSITIVE_INFINITY;
            int minIndex = -1;

            for (int i = 1; i < height+1; i++)
            {
                if (min > distTo[i][width-1])
                {
                    min = distTo [i][width-1];
                    minIndex = i-1;
                }
            }
     //       System.out.println("Row with minimal energy value: " + minIndex);
            Stack<Integer> path = new Stack<>();
            path.push(minIndex);
            int currentWidth = width-1;
            int currentStep = edgeTo [minIndex+1][currentWidth];
            while (currentWidth > 0)
            {
                path.push (currentStep);
                currentStep = edgeTo [currentStep+1][--currentWidth];
            }
          //  System.out.println("Seam that was found:"+path);
            int [] seam = new int [width];
            int index = 0;
            while (!path.isEmpty())
            {
                seam[index++] = path.pop();
            }
            return seam;
        }
        // sequence of indices for vertical seam
        public int[] findVerticalSeam()
        {
            int [][] edgeTo = new int [height][width+2];
            double [][] distTo = new double [height][width+2];
            for (int i = 1; i < height; i++)
            {
                for (int j = 0; j < width+2; j++)
                {
                    distTo [i][j] = Double.POSITIVE_INFINITY;
                }
            }

            for (int i = 0; i < width+2; i++)
            {
                distTo[0][i] = 1000;
                edgeTo[0][i] = i;
            }

            for (int i = 1; i < height; i++)
            {
                for (int j = 1; j < width+1; j++)
                {
                    // relax top edge
                    if (distTo[i][j] > energy[i][j-1] + distTo[i-1][j])
                    {
                        edgeTo[i][j] = j-1;
                        distTo[i][j] = energy[i][j-1] + distTo[i-1][j];
                    }
                    // relax top-left edge
                    if (distTo[i][j] > energy [i][j-1] + distTo [i-1][j-1])
                    {
                        edgeTo[i][j] = j-2;
                        distTo[i][j] = energy [i][j-1] + distTo [i-1][j-1];
                    }
                    // relax top-right edge
                    if (distTo[i][j] > energy [i][j-1] + distTo [i-1][j+1])
                    {
                        edgeTo[i][j] = j;
                        distTo[i][j] = energy [i][j-1] + distTo [i-1][j+1];
                    }
                }
            }
            int minIndex = 0;
            double min = Double.POSITIVE_INFINITY;
            for (int i = 1; i < width+1; i++)
            {
                if (min > distTo[height-1][i])
                {
                    min = distTo [height-1][i];
                    minIndex = i;
                }
            }
            Stack<Integer> path = new Stack<>();
            path.push (minIndex-1);
            int currentHeight = height - 1;
            int currentStep = edgeTo [currentHeight][minIndex];
            while (currentHeight > 0)
            {
                path.push (currentStep);
                currentStep = edgeTo [--currentHeight][currentStep+1];
            }
            int [] seam = new int [height];
            int currentItem = 0;
            while (!path.isEmpty())
            {
                seam [currentItem++] = path.pop();
            }
            return seam;
        }
        // remove horizontal seam from current picture
        public void removeHorizontalSeam(int[] seam)
        {
            if (seam == null) throw new IllegalArgumentException();
            if (seam.length != width) throw new IllegalArgumentException();
            if (height == 1) throw new IllegalArgumentException ();
            for (int i = 0; i < seam.length-1; i++)
            {
                if (Math.abs(seam[i] - seam [i+1]) > 1) throw new IllegalArgumentException();
            }
            for (int i = 0; i < seam.length; i++)
            {
                if (seam[i] < 0 || seam [i] >= height) throw new IllegalArgumentException();
            }
            updatePictureHorizontalSeam(seam);
            calculateEnergy();
        }
        private void updatePictureHorizontalSeam(int[] seam)
        {
            Picture newPicture = new Picture (width, --height);
            for (int j = 0; j < width; j++)
            {
                int offset = 0;
                int i = 0;
                while (i < height)
                {
                    if (seam[j] == i) offset = 1;
                    newPicture.setRGB(j, i, picture.getRGB(j, i+offset));
                    i++;
                }
            }
            picture = new Picture(newPicture);
        }
        // remove vertical seam from current picture
        public void removeVerticalSeam(int[] seam)
        {
            if (seam == null) throw new IllegalArgumentException();
            if (seam.length != height) throw new IllegalArgumentException();
            if (width() == 1) throw new IllegalArgumentException();
            for (int i = 0; i < seam.length-1; i++)
            {
                if (Math.abs (seam[i] - seam [i+1]) > 1) throw new IllegalArgumentException();
            }
            for (int i = 0; i < seam.length; i++)
            {
                if (seam [i] < 0 || seam [i] >= width) throw new IllegalArgumentException();
            }
            updatePictureVerticalSeam(seam);
            calculateEnergy();
        }
        // removes a seam from the picture (non-transposed)
        private void updatePictureVerticalSeam (int[] seam)
        {
         Picture newPicture = new Picture(--width, height);
         for (int i = 0; i < height; i++)
         {
             int offset = 0;
             int j = 0;
             while (j < width)
             {
                 if (seam [i] == j) offset++;
                 newPicture.setRGB(j, i, picture.getRGB (j+offset, i));
                 j++;
             }
         }
         picture = new Picture (newPicture);
        }
        //  unit testing (optional)
        public static void main(String[] args)
        {
        }
}