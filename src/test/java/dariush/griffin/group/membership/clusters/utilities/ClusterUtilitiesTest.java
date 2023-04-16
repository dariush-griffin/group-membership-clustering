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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.Member;
import dariush.griffin.group.membership.clusters.model.WeightPair;
import org.junit.jupiter.api.Test;

import static dariush.griffin.group.membership.clusters.utilities.ClusterUtilities.calculateAverageVector;
import static dariush.griffin.group.membership.clusters.utilities.ClusterUtilities.calculateSquaredEuclideanDistance;
import static dariush.griffin.group.membership.clusters.utilities.ClusterUtilities.pairVectorWeights;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClusterUtilitiesTest
{

  @Test
  public void testPairVectorWeights_Groups() {
    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);

    testGroupZero.addMember(testMemberZero, .5F);
    testGroupZero.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberTwo, .5F);

    Map<Member, WeightPair> pairs = pairVectorWeights(testGroupZero, testGroupOne);
    assertThat(pairs.keySet(), containsInRelativeOrder(testMemberZero, testMemberOne, testMemberTwo));
    assertThat(pairs.values(),
        containsInRelativeOrder(new WeightPair(0.5F, 0F), new WeightPair(0.5F, 0.5F), new WeightPair(0F, 0.5F)));
  }

  @Test
  public void testCalculateAverageVector_Groups() {
    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);

    testGroupZero.addMember(testMemberZero, .5F);
    testGroupZero.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberTwo, .5F);

    Set<Group> groups = new TreeSet<>();
    groups.addAll(Arrays.asList(testGroupZero, testGroupOne));
    Map<Member, Float> midpointVector = calculateAverageVector(groups);

    assertThat(midpointVector.keySet(), containsInRelativeOrder(testMemberZero, testMemberOne, testMemberTwo));
    assertThat(midpointVector.values(), containsInRelativeOrder(0.25F, 0.5F, 0.25F));
  }

  @Test
  public void testCalculateSquaredEuclideanDistance_Clusters() {
    Cluster testClusterZero = new Cluster();
    Cluster testClusterOne = new Cluster();

    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");
    Group testGroupTwo = new Group("test-group-two");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);
    Member testMemberThree = new Member("test-member-three", 3);

    testGroupZero.addMember(testMemberZero, .2F);
    testGroupZero.addMember(testMemberOne, .8F);
    testGroupOne.addMember(testMemberOne, .9F);
    testGroupOne.addMember(testMemberTwo, .1F);
    testGroupTwo.addMember(testMemberTwo, .9F);
    testGroupTwo.addMember(testMemberThree, .1F);

    testClusterZero.addGroup(testGroupOne);
    testClusterZero.addGroup(testGroupZero);
    testClusterOne.addGroup(testGroupTwo);

    float distance = calculateSquaredEuclideanDistance(calculateAverageVector(testClusterOne.getGroups()), calculateAverageVector(testClusterZero.getGroups()));
    // (.1 - 0)^2 + (.85 - 0)^2 + (.05 - .9)^2) + (0 - .1)^2 = 1.4649
    assertEquals(1.4649F, distance, .0001);
    // Testing that reversing the order has no bearing on the squared distance.
    float distanceReverseClusterOrder = calculateSquaredEuclideanDistance(calculateAverageVector(testClusterZero.getGroups()), calculateAverageVector(testClusterOne.getGroups()));
    assertEquals(distance, distanceReverseClusterOrder);
  }

  @Test
  public void testCalculateSquaredEuclideanDistance_Groups() {

    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);

    testGroupZero.addMember(testMemberZero, .2F);
    testGroupZero.addMember(testMemberOne, .8F);
    testGroupOne.addMember(testMemberOne, .9F);
    testGroupOne.addMember(testMemberTwo, .1F);

    float distance = calculateSquaredEuclideanDistance(testGroupZero, testGroupOne);
    // (.2 - 0)^2 + (.8 - .9)^2 + (0 - .1)^2) = 0.0599
    assertEquals(0.0599F, distance, .0001);
    // Testing that reversing the order has no bearing on the squared distance.
    assertEquals(calculateSquaredEuclideanDistance(testGroupZero, testGroupOne),
        calculateSquaredEuclideanDistance(testGroupOne, testGroupZero));
  }
}
