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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.Member;
import dariush.griffin.group.membership.clusters.model.WeightPair;

/**
 * <p>Utility methods that can be used when clustering {@link Group}s.</p>
 *
 * @author Dariush Griffin
 */
public class ClusterUtilities
{
  /**
   * <p>Calculates the average vector from a Set of {@link Group}s. The average vector is the average of all
   * {@link Member}s contribution weight.</p>
   *
   * @param groups The {@link Group}s whose average vector will be calculated.
   * @return The average vector for the provided {@link Group}s.
   */
  public static Map<Member, Float> calculateAverageVector(Set<Group> groups) {
    Map<Member, Float> result = new TreeMap<>();

    // Go through each Group and Member and add their weights into the resulting map.
    for (Group group : groups) {
      for (Entry<Member, Float> groupEntry : group.getMembers().entrySet()) {
        Float currentAverageWeight = result.get(groupEntry.getKey());
        currentAverageWeight = (currentAverageWeight != null) ? currentAverageWeight : 0F;

        result.put(groupEntry.getKey(), currentAverageWeight + groupEntry.getValue());
      }
    }

    // Divide each result entry by the number of groups, calculating the average.
    for (Entry<Member, Float> memberEntry : result.entrySet()) {
      result.put(memberEntry.getKey(), memberEntry.getValue() / groups.size());
    }

    return result;
  }

  /**
   * <p>Calculates the squared Euclidean distance between two {@link Group}s. Squared Euclidean distance: sum of (g1
   * sub i minus g2 sub i) squared.</p>
   *
   * @param g1 A {@link Group}.
   * @param g2 A {@link Group}.
   * @return The squared Euclidean distance between the two {@link Group}s.
   */
  public static float calculateSquaredEuclideanDistance(Group g1, Group g2) {
    return calculateSquaredEuclideanDistance(pairVectorWeights(g1, g2));
  }

  /**
   * <p>Calculates the squared Euclidean distance between two vectors, represented by a mapping of {@link Member} to
   * contribution weight. Squared Euclidean distance: sum of ((g1 sub i minus g2 sub i) squared).</p>
   *
   * @param v1 A vector.
   * @param v2 A vector.
   * @return The squared Euclidean distance between two vectors.
   */
  public static float calculateSquaredEuclideanDistance(Map<Member, Float> v1, Map<Member, Float> v2) {
    return calculateSquaredEuclideanDistance(pairVectorWeights(v1, v2));
  }

  /**
   * <p>Given two {@link Group}s, creates a vector that is represented by a map of {@link Member} to a
   * {@link WeightPair}. This makes it easier to perform mathematical operations on vectors represented by this sparse
   * mapping where the lack of an entry in the mapping is (0,0).</p>
   *
   * @param g1 A {@link Group}.
   * @param g2 A {@link Group}.
   * @return A vector that is represented by a map of {@link Member} to a {@link WeightPair}, {@link Member}s not in the
   * mapping are mathematically (0,0).
   */
  public static Map<Member, WeightPair> pairVectorWeights(Group g1, Group g2) {
    return pairVectorWeights(g1.getMembers(), g2.getMembers());
  }

  /**
   * <p>Given two vectors, creates a vector that is represented by a map of {@link Member} to a
   * {@link WeightPair}. This makes it easier to perform mathematical operations on vectors represented by this sparse
   * mapping where the lack of an entry in the mapping is (0,0).</p>
   *
   * @param v1 A sparse vector represented by a mapping of {@link Member} to their contribution weight.
   * @param v2 A sparse vector represented by a mapping of {@link Member} to their contribution weight.
   * @return A vector that is represented by a map of {@link Member} to a {@link WeightPair}, {@link Member}s not in the
   * mapping are mathematically (0,0).
   */
  public static Map<Member, WeightPair> pairVectorWeights(Map<Member, Float> v1, Map<Member, Float> v2) {
    Map<Member, WeightPair> weights = new TreeMap<>();

    for (Entry<Member, Float> clusterEntry : v1.entrySet()) {
      weights.put(clusterEntry.getKey(), new WeightPair(clusterEntry.getValue(), 0F));
    }

    for (Entry<Member, Float> clusterEntry : v2.entrySet()) {
      WeightPair weightPair = weights.get(clusterEntry.getKey());
      if (weightPair != null) {
        weightPair.second = clusterEntry.getValue();
      }
      else {
        weights.put(clusterEntry.getKey(), new WeightPair(0F, clusterEntry.getValue()));
      }
    }

    return weights;
  }

  /**
   * <p>Calculates the squared Euclidean distance from a sparse vector of {@link Member} to {@link WeightPair}s.</p>
   *
   * @param weights {@link WeightPair} representing the weighted contributions of this {@link Member} to each vector or
   *                {@link Group}.
   * @return The squared Euclidean distance.
   */
  private static float calculateSquaredEuclideanDistance(Map<Member, WeightPair> weights) {
    float result = 0F;

    for (WeightPair weightPair : weights.values()) {
      float diff = weightPair.first - weightPair.second;
      result += (diff * diff);
    }

    return result;
  }
}
