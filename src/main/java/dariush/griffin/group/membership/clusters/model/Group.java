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

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * <p>A Group represents a sparse vector of {@link  Member}s mapped to their weights. The weight represents that
 * members contribution to this group. Comparison is done on the name of the group, if another group shares the same
 * name as this group that will result in an {@link IllegalStateException} as the {@link MembershipMapping} or
 * {@link Cluster}should not have duplicate groups.</p>
 *
 * @author Dariush Griffin
 */
public class Group
    implements Comparable<Group>
{
  /**
   * A map of {@link Member} to their weight, or contribution to this group.
   */
  private final Map<Member, Float> membersToWeights;

  /**
   * The unique name for this group.
   */
  private final String name;

  /**
   * <p>Constructs a group with an empty member vector and the given name.</p>
   *
   * @param name The unique name of this group.
   */
  public Group(String name) {
    this.name = name;
    this.membersToWeights = new TreeMap<>();
  }

  /**
   * <p>Adds the given {@link Member} to this group and stores their contribution weight within this groups member
   * vector. If a {@link Member} already exists with an identical index it is replaced within the group.</p>
   *
   * @param member The member to be added to the group.
   * @param weight The weight of the provided member's contributions.
   */
  public void addMember(Member member, float weight) {
    membersToWeights.put(member, weight);
  }

  /**
   * <p> Removes the {@link Member} from the group.</p>
   *
   * @param member The {@link Member} to be removed from this group.
   */
  public void removeMember(Member member) {
    membersToWeights.remove(member);
  }

  /**
   * @return An unmodifiable map of {@link Member} to their weight, or contribution. Combined with the {@link Member}'s
   * vector index this is the sparse vector of contributions that can be used for cluster calculations.
   */
  public Map<Member, Float> getMembers() {
    return Collections.unmodifiableMap(membersToWeights);
  }

  /**
   * <p>Returns the weight of the provided {@link Member}.</p>
   *
   * @param member The {@link Member}'s weight to be return.
   * @return The {@link Member}'s weight, or null if the {@link Member} does not exist in this group.
   */
  public Float getWeight(Member member) {
    return membersToWeights.get(member);
  }

  /**
   * @return The unique name for this group.
   */
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return membersToWeights.equals(group.membersToWeights) && Objects.equals(name, group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(membersToWeights, name);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Group{");
    sb.append("membersToWeights=").append(membersToWeights);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int compareTo(final Group o) {
    int result = 0;
    if (this == o) {
      return result;
    }

    result = name.compareTo(o.name);
    // TODO This seems crude, consider something more elegant.
    if (result == 0 && !this.equals(o)) {
      throw new IllegalStateException(
          String.format("Group within a graph must be uniquely named. This group: '%s', compared to group '%s'.", this,
              o));
    }

    return result;
  }
}
