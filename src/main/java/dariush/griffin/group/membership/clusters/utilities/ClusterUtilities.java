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

public class ClusterUtilities
{
  public static Map<Member, Float> calculateAverageVector(Set<Group> groups) {
    Map<Member, Float> result = new TreeMap<>();

    for (Group group : groups) {
      for (Entry<Member, Float> groupEntry : group.getMembers().entrySet()) {
        Float currentAverageWeight = result.get(groupEntry.getKey());
        currentAverageWeight = (currentAverageWeight != null) ? currentAverageWeight : 0F;

        result.put(groupEntry.getKey(), currentAverageWeight + groupEntry.getValue());
      }
    }

    for(Entry<Member, Float> memberEntry : result.entrySet()) {
      result.put(memberEntry.getKey(), memberEntry.getValue() / groups.size());
    }

    return result;
  }

  public static float calculateSquaredEuclideanDistance(Group g1, Group g2) {
    return calculateSquaredEuclideanDistance(pairVectorWeights(g1, g2));
  }

  public static float calculateSquaredEuclideanDistance(Map<Member, Float> v1, Map<Member, Float> v2) {
    return calculateSquaredEuclideanDistance(pairVectorWeights(v1, v2));
  }

  public static Map<Member, WeightPair> pairVectorWeights(Group g1, Group g2) {
    return pairVectorWeights(g1.getMembers(), g2.getMembers());
  }

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

  private static float calculateSquaredEuclideanDistance(Map<Member, WeightPair> weights) {
    float result = 0F;

    for (WeightPair weightPair : weights.values()) {
      float diff = weightPair.first - weightPair.second;
      result += (diff * diff);
    }

    return result;
  }
}
