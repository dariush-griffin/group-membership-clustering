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

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.Member;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupCacheTest
{
  @Test
  public void testCRUD() {
    GroupCache testCache;

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

    testCache = new GroupCache();
    testCache.addGroup(testGroupOne);
    assertEquals(testGroupOne, testCache.getGroups(testMemberOne).iterator().next());

    testCache = new GroupCache();
    testCache.addGroups(Arrays.asList(testGroupZero));
    assertEquals(testGroupZero, testCache.getGroups(testMemberZero).iterator().next());

    testCache = new GroupCache();
    testCache.addGroups(testClusterOne);
    testCache.addGroups(testClusterZero);
    assertThat(testCache.getSimilarGroups(testGroupOne), containsInAnyOrder(testGroupZero, testGroupTwo));

    testCache = new GroupCache();
    testCache.addGroups(testClusterOne);
    testCache.addGroups(testClusterZero);
    assertThat(testCache.getSimilarGroups(testClusterOne), containsInAnyOrder(testGroupOne));

    testCache = new GroupCache();
    testCache.addGroups(testClusterOne);
    testCache.addGroups(testClusterZero);
    assertThat(testCache.getSimilarGroups(Arrays.asList(testGroupTwo)), containsInAnyOrder(testGroupOne));
  }
}
