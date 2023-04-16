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
package dariush.griffin.group.membership.clusters.model;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class ClusterTest
{
  @Test
  public void testCRUD() {
    Cluster testCluster = new Cluster();

    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);

    testGroupZero.addMember(testMemberZero, .5F);
    testGroupZero.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberTwo, .5F);

    testCluster.addGroup(testGroupOne);
    testCluster.addGroup(testGroupZero);

    assertThat(testCluster.getGroups(), Matchers.containsInAnyOrder(testGroupOne, testGroupZero));
  }

  @Test
  public void testAddGroups() {

    Cluster testCluster = new Cluster();

    Group testGroupZero = new Group("test-group-zero");
    Group testGroupOne = new Group("test-group-one");

    Member testMemberZero = new Member("test-member-zero", 0);
    Member testMemberOne = new Member("test-member-one", 1);
    Member testMemberTwo = new Member("test-member-two", 2);

    testGroupZero.addMember(testMemberZero, .5F);
    testGroupZero.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberOne, .5F);
    testGroupOne.addMember(testMemberTwo, .5F);

    testCluster.addGroups(Arrays.asList(testGroupOne, testGroupZero));
    assertThat(testCluster.getGroups(), Matchers.containsInAnyOrder(testGroupOne, testGroupZero));
  }

  @Test
  public void testAddGroup_DuplicateGroup() {
    Cluster testCluster = new Cluster();
    Group testGroupZero = new Group("test-group-zero");
    testCluster.addGroup(testGroupZero);
    testCluster.addGroup(testGroupZero);
    assertThat(testCluster.getGroups(), containsInAnyOrder(testGroupZero));
    assertThat(testCluster.getGroups(), hasSize(1));
  }
}
