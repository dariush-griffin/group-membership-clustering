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
package dariush.griffin.group.membership.clusters.algorithms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.MembershipMapping;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static dariush.griffin.group.membership.clusters.utilities.ImageUtilities.loadMembershipMapping;
import static dariush.griffin.group.membership.clusters.utilities.ImageUtilities.writeClusters;

@Tag("visual")
public class VisualAgglomerativeClusterAlgorithmTest
{
  private final static String CLUSTER_IMAGE_FILE_PATH_4_100_100 =
      "src/test/resources/test-images/4_clusters_100_100.bmp";

  @Test
  public void testVisualClustering_4_clusters() throws IOException {
    MembershipMapping imageMembershipMapping = loadMembershipMapping(new File(CLUSTER_IMAGE_FILE_PATH_4_100_100));

    Set<Cluster> clusters = new AgglomerativeClusterAlgorithm(imageMembershipMapping, .01F).cluster();

    writeClusters(clusters, 100, 100,
        Paths.get("target", "visual-tests", this.getClass().getSimpleName(), "testVisualClustering_4_clusters.bmp")
            .toFile());
  }
}
