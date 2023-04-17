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
 * <p>This algorithm clusters {@link Group}.</p>
 *
 * @author Dariush Griffin
 */
public class AgglomerativeClusterAlgorithm implements ClusterAlgorithm
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

  public Set<Cluster> cluster() {
    Map<Group, Cluster> workingGroups = createInitialClusters();
    return clusterHelper(workingGroups);
  }

  private Set<Cluster> clusterHelper(Map<Group, Cluster> workingGroups) {
    boolean merged = false;

    for(Cluster sourceCluster : workingGroups.values()) {
      Group closestGroup = getClosestGroup(workingGroups, sourceCluster);
      if(closestGroup != null) {
        Cluster closestCluster = workingGroups.get(closestGroup);
        mergeClusters(closestCluster, sourceCluster);
        remapClosestCluster(workingGroups, closestCluster, sourceCluster);
        merged = true;
      }
    }

    // If we haven't merged any clusters then we have confirmed that all clustering is done.
    if(!merged) {
      return new HashSet<>(workingGroups.values());
    }

    return clusterHelper(workingGroups);
  }

  private void remapClosestCluster(Map<Group, Cluster> workingGroups, Cluster closestCluster, Cluster target) {
    for(Group group : closestCluster.getGroups()) {
      workingGroups.put(group, target);
    }
  }

  private void mergeClusters(Cluster source, Cluster target) {
    target.addGroups(source.getGroups());
  }

  private Group getClosestGroup(Map<Group, Cluster> workingGroups, Cluster sourceCluster) {
    for(Group sourceGroup : sourceCluster.getGroups()) {
      for(Group similarGroup : groupCache.getSimilarGroups(sourceCluster)) {
        // Make sure we aren't calculating the distance to ourselves or finding a group in our cluster.
        if(!workingGroups.get(similarGroup).equals(sourceCluster) && !sourceGroup.equals(similarGroup)){
          float distance = calculateSquaredEuclideanDistance(sourceGroup, similarGroup);
          if(distance < minSquaredEuclideanDistance) {
            return similarGroup;
          }
        }
      }
    }

    return null;
  }

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
