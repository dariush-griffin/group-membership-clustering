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

public class Group
    implements Comparable<Group>
{
  private final Map<Member, Float> membersToWeights;

  private final String name;

  public Group(String name) {
    this.name = name;
    this.membersToWeights = new TreeMap<>();
  }

  public void addMember(Member member, float weight) {
    membersToWeights.put(member, weight);
  }

  public void removeMember(Member member) {
    membersToWeights.remove(member);
  }

  public Map<Member, Float> getMembers() {
    return Collections.unmodifiableMap(membersToWeights);
  }

  public Float getWeight(Member member) {
    return membersToWeights.get(member);
  }

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
    if (result == 0 && !this.equals(o)) {
      throw new IllegalStateException(
          String.format("Group within a graph must be uniquely named. This group: '%s', compared to group '%s'.", this,
              o));
    }

    return result;
  }
}
