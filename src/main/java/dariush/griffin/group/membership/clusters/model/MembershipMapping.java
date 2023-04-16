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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class MembershipMapping
{
  private static final String EXISTING_GROUP_ERROR_MESSAGE =
      "Unable to create group as a group with the name '%s' already exists. Existing group: %s.";
  private static final String EXISTING_MEMBER_ERROR_MESSAGE =
      "Unable to create member as a member with the name '%s' already exists. Existing member: %s.";

  private Map<String, Group> groups;

  private Map<String, Member> members;

  private int vectorComponentCounter;

  public MembershipMapping() {
    this.groups = new HashMap<>();
    this.members = new HashMap<>();
    this.vectorComponentCounter = 0;
  }

  public void createGroup(String groupName) {
    checkDoesNotExist(groupName, this::hasGroup, EXISTING_GROUP_ERROR_MESSAGE, groupName,
        groups.get(groupName));

    groups.put(groupName, new Group(groupName));
  }

  public void createMember(String memberName) {
    checkDoesNotExist(memberName, this::hasMember,
        EXISTING_MEMBER_ERROR_MESSAGE, memberName,
        members.get(memberName));

    members.put(memberName, buildMember(memberName));
  }

  public boolean hasGroup(String groupName) {
    return groups.containsKey(groupName);
  }

  public boolean hasMember(String memberName) {
    return members.containsKey(memberName);
  }

  public Group getGroup(String groupName) {
    return groups.get(groupName);
  }

  public Member getMember(String memberName) {
    return members.get(memberName);
  }

  public Collection<Group> getGroups() {
    return Collections.unmodifiableCollection(groups.values());
  }

  public Collection<Member> getMembers() {
    return Collections.unmodifiableCollection(members.values());
  }

  public void addMemberToGroup(String memberName, String groupName, float contribution) {
    if (!hasMember(memberName)) {
      createMember(memberName);
    }
    if (!hasGroup(groupName)) {
      createGroup(groupName);
    }
    groups.get(groupName).addMember(members.get(memberName), contribution);
  }

  public void removeMemberFromGroup(String memberName, String groupName) {
    if (!hasGroup(groupName) || !hasMember(memberName)) {
      return;
    }
    groups.get(groupName).removeMember(members.get(memberName));
  }

  public void removeMemberFromAllGroups(String memberName) {
    if (!hasMember(memberName)) {
      return;
    }
    for (Group group : groups.values()) {
      removeMemberFromGroup(memberName, group.getName());
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MembershipMapping that = (MembershipMapping) o;
    return vectorComponentCounter == that.vectorComponentCounter && groups.equals(that.groups) &&
        members.equals(that.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groups, members, vectorComponentCounter);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MembershipGraph{");
    sb.append("groups=").append(groups);
    sb.append(", members=").append(members);
    sb.append(", vectorComponentCounter=").append(vectorComponentCounter);
    sb.append('}');
    return sb.toString();
  }

  private void checkDoesNotExist(
      String memberName,
      Function<String, Boolean> existMethod,
      String errorMessage,
      Object... messageArguments)
  {
    if (existMethod.apply((memberName))) {
      throw new IllegalArgumentException(
          String.format(errorMessage, messageArguments));
    }
  }

  /**
   * <p>This could be refactored into a factory class, but since we're relying on a simplistic notion of vector index
   * we just need to increment the vector component counter. If we want to support more complex notions rather than each
   * member being denoted by an index a factory class would be more beneficial.</p>
   *
   * @param memberName The name of the member.
   * @return A Member which represents a unique name and index into the graphs member vector.
   */
  private Member buildMember(String memberName) {
    return new Member(memberName, vectorComponentCounter++);
  }
}
