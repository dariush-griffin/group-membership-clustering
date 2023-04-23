/*
 * Copyright (c) 2023 Dariush Griffin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dariush.griffin.group.membership.clusters.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.MembershipMapping;

/**
 * <p>A utility class to help use images in visual tests. In particular it provides methods to convert a 2D image into
 * a {@link MembershipMapping} with two members, X and Y, and it provides methods to write {@link Cluster}s out to an
 * image.</p><br><p>Typical usage should be:<ol><li>{@code loadMembershipMapping(...)}</li><li>Run a clustering
 * algorithm.</li><li>{@code writeClusters(...)}</li></ol></p>
 *
 * @author Dariush Griffin
 */
public class ImageUtilities
{
  private final static Random RANDOM = new Random();

  private final static Color WHITE = new Color(255, 255, 255, 255);

  /**
   * <p>Converts an image into a {@link MembershipMapping}. Two
   * {@link dariush.griffin.group.membership.clusters.model.Member}s are created, named "X" and "Y". Each non-white
   * pixel (255, 255, 255) in the image is converted into a {@link Group}. Member contribution weights are the pixel
   * coordinate divided by the total width or height respectively. This means that for a 100 x 100 images values will be
   * between 0 and 1.</p>
   *
   * @param imageFile The image file to convert into a {@link MembershipMapping}.
   * @return A {@link MembershipMapping} representing the image.
   * @throws IOException Thrown if there is an IO issue accessing the provided file.
   */
  public static MembershipMapping loadMembershipMapping(File imageFile) throws IOException {
    MembershipMapping result = new MembershipMapping();

    BufferedImage image = ImageIO.read(imageFile);
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        Color color = new Color(image.getRGB(x, y), true);
        if (!color.equals(WHITE)) {
          result.addMemberToGroup("x", x + "_" + y, ((float) x) / image.getWidth());
          result.addMemberToGroup("y", x + "_" + y, ((float) y) / image.getHeight());
        }
      }
    }

    return result;
  }

  /**
   * <p>Writes the set of {@link Cluster}s to an image. Clusters are assigned a random color with some bias towards
   * red, green, or blue. The {@link Group} names of the groups in the clusters must be in the form of "X_Y" (e.g.
   * 23_57), as created by the {@code  loadMembershipMapping} method.</p>
   *
   * @param clusters    The clusters to draw on the 2D image.
   * @param pixelWidth  The width of the image, should match the image used to create the membership mapping.
   * @param pixelHeight The height of the image, should match the image used to create the membership mapping.
   * @param outputFile  The path to the output file.
   * @throws IOException Thrown if an error occurs while writing the image.
   */
  public static void writeClusters(Set<Cluster> clusters, int pixelWidth, int pixelHeight, File outputFile)
      throws IOException
  {
    if (!outputFile.exists()) {
      outputFile.mkdirs();
    }

    BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);

    for (Cluster cluster : clusters) {
      Color clusterColor = getRandomColor();
      for (Group group : cluster.getGroups()) {
        if (!group.getName().contains("_")) {
          throw new IllegalArgumentException(
              "This method expects group names to be in the form of x_y (e.g. 103_203), as created by loadMembershipMapping.");
        }
        String[] stringCoords = group.getName().split("_");
        int x = Integer.valueOf(stringCoords[0]);
        int y = Integer.valueOf(stringCoords[1]);
        image.setRGB(x, y, clusterColor.getRGB());
      }
    }

    ImageIO.write(image, "bmp", outputFile);
  }

  /**
   * @return Returns a random color with a random bias towards Red, Green, or Blue.
   */
  private static Color getRandomColor() {
    switch (RANDOM.nextInt(3)) {
      case 0: // Favor Red
        return new Color(150 + Math.max(RANDOM.nextInt(256 - 150), 0), RANDOM.nextInt(256), RANDOM.nextInt(256));
      case 1: // Favor Green
        return new Color(150 + RANDOM.nextInt(256 - 150), 150 + Math.max(RANDOM.nextInt(256 - 150), 0),
            RANDOM.nextInt(256));
      default: // Favor Blue
        return new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), 150 + Math.max(RANDOM.nextInt(256 - 150), 0));
    }
  }
}
