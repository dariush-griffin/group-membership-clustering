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

import java.util.Arrays;
import java.util.Set;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.MembershipMapping;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class AgglomerativeClusterAlgorithmTest
{
  @Test
  public void testCluster() {
    MembershipMapping membershipMapping = new MembershipMapping();

    membershipMapping.addMemberToGroup("test-member-zero", "test-group-zero", .2F);
    membershipMapping.addMemberToGroup("test-member-one", "test-group-zero", .8F);
    membershipMapping.addMemberToGroup("test-member-one", "test-group-one", .9F);
    membershipMapping.addMemberToGroup("test-member-two", "test-group-one", .1F);
    membershipMapping.addMemberToGroup("test-member-two", "test-group-two", .9F);
    membershipMapping.addMemberToGroup("test-member-three", "test-group-two", .1F);

    AgglomerativeClusterAlgorithm algorithm = new AgglomerativeClusterAlgorithm(membershipMapping, 0.6F);

    Set<Cluster> clusters = algorithm.cluster();
    assertThat(clusters, hasSize(2));
    Cluster expectedClusterOne = new Cluster();
    expectedClusterOne.addGroup(membershipMapping.getGroup("test-group-two"));
    Cluster expectedClusterZero = new Cluster();
    expectedClusterZero.addGroups(Arrays.asList(membershipMapping.getGroup("test-group-zero"), membershipMapping.getGroup("test-group-one")));
    assertThat(clusters, containsInAnyOrder(expectedClusterZero, expectedClusterOne));
  }
}
