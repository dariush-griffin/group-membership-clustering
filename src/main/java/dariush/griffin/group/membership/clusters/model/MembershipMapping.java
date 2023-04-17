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

/**
 * <p>A MembershipMapping stores {@link Member}s and {@link Group}s. {@link Member}s are added to {@link Group}s with a
 * weight representing their contributions to that group. As each {@link Member} is added to the mapping they are given
 * a unique vector index. By doing this we can create an N-dimensional space where {@link Group}s represent points in
 * that space whose position is defined by their {@link Member}'s weight.</p><br>
 * <p>For example, a mapping that has two members (A, B) would be analogous to a cartesian plane. If member A
 * contributed to group 1 with a weight of 2 and member B contributed to group 1 with a weight of 3, group 1 would be
 * placed on that cartesian plane at (2,3). If there was a second group, 2, and member A contributed to that group with
 * weight 1 and member B contributed to the group with a weight of 10, group 2 would be at (1,10) on that plane.</p>
 *
 * @author Dariush Griffin
 */
public class MembershipMapping
{
  private static final String EXISTING_GROUP_ERROR_MESSAGE =
      "Unable to create group as a group with the name '%s' already exists. Existing group: %s.";
  private static final String EXISTING_MEMBER_ERROR_MESSAGE =
      "Unable to create member as a member with the name '%s' already exists. Existing member: %s.";

  /**
   * A mapping of group name to {@link Group}.
   */
  private Map<String, Group> groups;

  /**
   * A mapping of member name to {@link Member}.
   */
  private Map<String, Member> members;

  /**
   * The number of members we've added to this mapping, each member is a new dimension.
   */
  private int vectorComponentCounter;

  /**
   * <p>Constructs an empty MembershipMapping.</p>
   */
  public MembershipMapping() {
    this.groups = new HashMap<>();
    this.members = new HashMap<>();
    this.vectorComponentCounter = 0;
  }

  /**
   * <p>Creates a {@link Group} with the provided name.</p>
   *
   * @param groupName The unique name for this group.
   * @throws IllegalArgumentException If a {@link Group} already exists in this mapping with the provided name.
   */
  public void createGroup(String groupName) {
    checkDoesNotExist(groupName, this::hasGroup, EXISTING_GROUP_ERROR_MESSAGE, groupName,
        groups.get(groupName));

    groups.put(groupName, new Group(groupName));
  }

  /**
   * <p>Creates a {@link Member} with the provided name.</p>
   *
   * @param memberName The unique name for this member.
   * @throws IllegalArgumentException If a {@link Member} already exists in this mapping with the provided name.
   */
  public void createMember(String memberName) {
    checkDoesNotExist(memberName, this::hasMember,
        EXISTING_MEMBER_ERROR_MESSAGE, memberName,
        members.get(memberName));

    members.put(memberName, buildMember(memberName));
  }

  /**
   * @param groupName The name of the {@link Group}.
   * @return True, if the {@link Group} exists within this mapping.
   */
  public boolean hasGroup(String groupName) {
    return groups.containsKey(groupName);
  }

  /**
   * @param memberName The name of the {@link Member}.
   * @return True, if the {@link Member} exists within this mapping.
   */
  public boolean hasMember(String memberName) {
    return members.containsKey(memberName);
  }

  /**
   * @param groupName The name of the {@link Group}.
   * @return The {@link Group} if it exists in this mapping, or null.
   */
  public Group getGroup(String groupName) {
    return groups.get(groupName);
  }

  /**
   * @param memberName The name of the {@link Member}.
   * @return The {@link Member} if it exists in this mapping, or null.
   */
  public Member getMember(String memberName) {
    return members.get(memberName);
  }

  /**
   * @return An unmodifiable collection of {@link Group}s in this mapping.
   */
  public Collection<Group> getGroups() {
    return Collections.unmodifiableCollection(groups.values());
  }

  /**
   * @return An unmodifiable collection of {@link Member}s in this mapping.
   */
  public Collection<Member> getMembers() {
    return Collections.unmodifiableCollection(members.values());
  }

  /**
   * <p>Adds the {@link Member} with the given name to the {@link Group} with the given name. <b>If the member or group
   * does not exist in the mapping they are created.</b></p>
   *
   * @param memberName The name of the {@link Member}.
   * @param groupName  The name of the {@link Group}.
   * @param weight     The contribution of this {@link Member} to this {@link Group}.
   */
  public void addMemberToGroup(String memberName, String groupName, float weight) {
    if (!hasMember(memberName)) {
      createMember(memberName);
    }
    if (!hasGroup(groupName)) {
      createGroup(groupName);
    }
    groups.get(groupName).addMember(members.get(memberName), weight);
  }

  /**
   * <p>Removes the named {@link Member} from the named {@link Group}. If the member or the group does not exist
   * nothing
   * is removed.</p>
   *
   * @param memberName The name of the {@link Member}.
   * @param groupName  The name of the {@link Group}.
   */
  public void removeMemberFromGroup(String memberName, String groupName) {
    if (!hasGroup(groupName) || !hasMember(memberName)) {
      return;
    }
    groups.get(groupName).removeMember(members.get(memberName));
  }

  /**
   * <p>Removes the named {@link Member} from all {@link Group}s. If the {@link Member} does not exist in this mapping
   * then nothing is removed.</p>
   *
   * @param memberName The name of the {@link Member} to remove from all {@link Group}.
   */
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

  /**
   * <p>Checks if the provided name exists using the function.</p>
   *
   * @param name             The name of the object we will pass to the exist method.
   * @param existMethod      The method to call with the given name.
   * @param errorMessage     The error message to use if the object exists in this mapping.
   * @param messageArguments Objects to be formatted into the error message.
   * @throws IllegalArgumentException If the exist method returns true.
   */
  private void checkDoesNotExist(
      String name,
      Function<String, Boolean> existMethod,
      String errorMessage,
      Object... messageArguments)
  {
    if (existMethod.apply((name))) {
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
   * @return A {@link Member} which represents a unique name and index into the mapping's member vector.
   */
  private Member buildMember(String memberName) {
    return new Member(memberName, vectorComponentCounter++);
  }
}
