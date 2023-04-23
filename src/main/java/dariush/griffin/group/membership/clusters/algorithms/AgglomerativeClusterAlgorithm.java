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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.MembershipMapping;
import dariush.griffin.group.membership.clusters.utilities.GroupCache;

import static dariush.griffin.group.membership.clusters.utilities.ClusterUtilities.calculateSquaredEuclideanDistance;

/**
 * <p>This algorithm clusters {@link Group} based on the squared euclidean distances between them. Unlike a traditional
 * agglomerative algorithm, this algorithm can take advantage of two heuristics because we are clustering groups based
 * on membership.</p><br><p>The first is that we only need to compare groups to other "similar" groups. Groups are
 * "similar" if they contain at least one shared member. The second heuristic is that we can stop finding clusters to
 * merge once we find one that is within our minimum distance. That is because comparison is still done between all
 * groups between clusters so if there are other clusters that are closer they will eventually merge
 * correctly.</p><br><p>The steps are as follows:<ol><li>Create a cluster for every group.</li><li>For each cluster,
 * find groups that are "similar".</li><li>For each group in the cluster compare them to the "similar"
 * groups.</li><li>If a group in this cluster is within ("<") the minimum squared euclidean distance merge this cluster
 * with the cluster that contains that group.</li><li>Repeat steps 2 through 4 until no clusters are
 * merged.</li></ol></p>
 *
 * @author Dariush Griffin
 */
public class AgglomerativeClusterAlgorithm
    implements ClusterAlgorithm
{
  private final MembershipMapping membershipMapping;

  private final GroupCache groupCache;

  private final float minSquaredEuclideanDistance;

  public AgglomerativeClusterAlgorithm(MembershipMapping membershipMapping, float minSquaredEuclideanDistance) {
    this.membershipMapping = membershipMapping;
    this.groupCache = new GroupCache();
    this.groupCache.addGroups(membershipMapping.getGroups());
    this.minSquaredEuclideanDistance = minSquaredEuclideanDistance;
  }

  @Override
  public Set<Cluster> cluster() {
    Map<Group, Cluster> workingGroups = createInitialClusters();
    return clusterHelper(workingGroups);
  }

  /**
   * <p>Loops through each cluster, finds if a "similar" group is within the minimum distance. If there is a close
   * group merges the cluster with the cluster that contains that group.</p>
   *
   * @param workingGroups A mapping of {@link Group} to the {@link Cluster} that contains it.
   * @return A set of {@link Cluster}s.
   */
  private Set<Cluster> clusterHelper(Map<Group, Cluster> workingGroups) {
    boolean merged = false;

    for (Cluster sourceCluster : workingGroups.values()) {
      Group closestGroup = getClosestGroup(workingGroups, sourceCluster);
      if (closestGroup != null) {
        Cluster closestCluster = workingGroups.get(closestGroup);
        // Merge the closest cluster with our source cluster.
        mergeClusters(closestCluster, sourceCluster);
        // Make sure the closest cluster's groups now point to the merged cluster.
        remapClosestCluster(workingGroups, closestCluster, sourceCluster);
        merged = true;
      }
    }

    // If we haven't merged any clusters then we have confirmed that all clustering is done.
    if (!merged) {
      return new HashSet<>(workingGroups.values());
    }

    // Want to use tail recursion here so we don't blow up the stack.
    return clusterHelper(workingGroups);
  }

  /**
   * <p>Once we've merged clusters this method will ensure that we update the working groups so they map to their new
   * cluster.</p>
   *
   * @param workingGroups  A mapping of {@link Group} to the {@link Cluster} that contains it.
   * @param closestCluster The cluster we are eliminating through the merge.
   * @param target         The combined cluster representing all groups plus those in the closestCluster.
   */
  private void remapClosestCluster(Map<Group, Cluster> workingGroups, Cluster closestCluster, Cluster target) {
    for (Group group : closestCluster.getGroups()) {
      workingGroups.put(group, target);
    }
  }

  /**
   * <p>Adds all groups from the source cluster to the target cluster.</p>
   *
   * @param source The {@link Cluster} that is no longer going to be needed after merge.
   * @param target The {@link Cluster} that will contain all groups.
   */
  private void mergeClusters(Cluster source, Cluster target) {
    target.addGroups(source.getGroups());
  }

  /**
   * <p>For each {@link Group} in the source {@link Cluster}, find "similar" {@link Group}s. If that {@link Group}
   * isn't contained in the source {@link Cluster} calculate the distance to that {@link Group}. If that {@link Group}
   * is within our minimum distance, return it as a closest {@link Group}.</p>
   *
   * @param workingGroups A mapping of {@link Group} to the {@link Cluster} that contains it.
   * @param sourceCluster The {@link Cluster} whose {@link Group}s we are checking for a "similar" {@link Group} within
   *                      the minimum distance.
   * @return A {@link Group} that is within the minimum distance to a {@link Group} within the sourceCluster, or
   * {@literal Null} if there is no closest {@link Group}.
   */
  private Group getClosestGroup(Map<Group, Cluster> workingGroups, Cluster sourceCluster) {
    for (Group sourceGroup : sourceCluster.getGroups()) {
      for (Group similarGroup : groupCache.getSimilarGroups(sourceCluster)) {
        float distance = calculateSquaredEuclideanDistance(sourceGroup, similarGroup);
        if (distance < minSquaredEuclideanDistance) {
          return similarGroup;
        }
      }
    }

    return null;
  }

  /**
   * <p>Creates a {@link Cluster} for each {@link Group} and returns them as a mapping between the {@link Group} and
   * the {@link Cluster} that contains it.</p>
   *
   * @return A mapping of {@link Group} to the {@link Cluster} that contains it.
   */
  private Map<Group, Cluster> createInitialClusters() {
    Map<Group, Cluster> clusters = new HashMap<>(membershipMapping.getGroups().size());
    for (Group group : membershipMapping.getGroups()) {
      Cluster cluster = new Cluster();
      cluster.addGroup(group);
      clusters.put(group, cluster);
    }
    return clusters;
  }
}
