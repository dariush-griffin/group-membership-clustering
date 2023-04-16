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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MembershipMappingTest
{
  @Test
  public void testCRUD() {
    MembershipMapping testMapping = new MembershipMapping();

    testMapping.createMember("test-member-zero");
    testMapping.createMember("test-member-one");
    testMapping.createMember("test-member-two");
    testMapping.createGroup("test-group-zero");
    testMapping.createGroup("test-group-one");
    testMapping.createGroup("test-group-two");

    assertThat(testMapping.getGroups(),
        Matchers.containsInAnyOrder(new Group("test-group-zero"), new Group("test-group-one"), new Group("test-group-two")));
    assertThat(testMapping.getMembers(),
        Matchers.containsInAnyOrder(new Member("test-member-zero", 0), new Member("test-member-one", 1),
            new Member("test-member-two", 2)));

    assertEquals(new Group("test-group-zero"), testMapping.getGroup("test-group-zero"));
    assertEquals(new Member("test-member-zero", 0), testMapping.getMember("test-member-zero"));

    testMapping.addMemberToGroup("test-member-zero", "test-group-zero", 0.5F);
    testMapping.addMemberToGroup("test-member-one", "test-group-zero", 0.25F);
    testMapping.addMemberToGroup("test-member-two", "test-group-zero", 0.25F);

    assertThat(testMapping.getGroup("test-group-zero").getMembers().keySet(),
        Matchers.containsInAnyOrder(new Member("test-member-zero", 0), new Member("test-member-one", 1),
            new Member("test-member-two", 2)));

    testMapping.removeMemberFromGroup("test-member-two", "test-group-zero");

    assertThat(testMapping.getGroup("test-group-zero").getMembers().keySet(),
        Matchers.containsInAnyOrder(new Member("test-member-zero", 0), new Member("test-member-one", 1)));
  }

  @Test
  public void testAddMemberToGroup_CreatesGroupsAndMembersWhenTheyDoNotExist() {
    MembershipMapping testMapping = new MembershipMapping();

    assertFalse(testMapping.hasMember("test-member-zero"));
    assertFalse(testMapping.hasGroup("test-group-zero"));
    testMapping.addMemberToGroup("test-member-zero", "test-group-zero", 1.0F);
    assertTrue(testMapping.hasGroup("test-group-zero"));
    assertTrue(testMapping.hasMember("test-member-zero"));

    assertFalse(testMapping.hasMember("test-member-one"));
    testMapping.addMemberToGroup("test-member-one", "test-group-zero", 0.5F);
    assertTrue(testMapping.hasMember("test-member-one"));

    assertFalse(testMapping.hasGroup("test-group-one"));
    testMapping.addMemberToGroup("test-member-zero", "test-group-one", 0.5F);
    assertTrue(testMapping.hasGroup("test-group-one"));
  }

  @Test
  public void testRemoveMemberFromAllGroups() {
    MembershipMapping testMapping = new MembershipMapping();

    testMapping.addMemberToGroup("test-member-zero", "test-group-zero", 1.0F);
    testMapping.addMemberToGroup("test-member-zero", "test-group-one", 1.0F);
    testMapping.addMemberToGroup("test-member-one", "test-group-one", 0F);

    testMapping.removeMemberFromAllGroups("test-member-zero");

    testMapping.getGroups().forEach(group -> {assertFalse(group.getMembers().containsKey(testMapping.getMember("test-member-zero")));});
  }

  @Test
  public void testValidation() {
    MembershipMapping testMapping = new MembershipMapping();

    testMapping.createMember("test-member-zero");
    testMapping.createGroup("test-group-zero");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testMapping.createMember("test-member-zero"));
    assertEquals("Unable to create member as a member with the name 'test-member-zero' already exists. Existing member: Member{name='test-member-zero', vectorIndex=0}.", exception.getMessage());
    exception = assertThrows(IllegalArgumentException.class, () -> testMapping.createGroup("test-group-zero"));
    assertEquals("Unable to create group as a group with the name 'test-group-zero' already exists. Existing group: Group{membersToWeights={}, name='test-group-zero'}.", exception.getMessage());
  }
}
