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

import java.util.Objects;

/**
 * <p>Represents a member of a group. Each member has a vector index which should be assigned to them by the
 * {@link MembershipMapping}. Comparison between members is done on their vector index and then their name.</p>
 *
 * @author Dariush Griffin
 */
public class Member
    implements Comparable<Member>
{
  /**
   * A unique name for this member.
   */
  private final String name;

  /**
   * This member's corresponding vector in the membership mapping, used by others to map a contribution back to a user.
   */
  private final int vectorIndex;

  /**
   * <p>Constructs a Member with the provided name and vector index.</p>
   *
   * @param name        The unique name of this group.
   * @param vectorIndex The index in the membership mapping that represents this group.
   */
  public Member(String name, int vectorIndex) {
    this.name = name;
    this.vectorIndex = vectorIndex;
  }

  /**
   * @return The unique name for this member.
   */
  public String getName() {
    return name;
  }

  /**
   * @return The vector index for this member, assigned to them by the membership mapping.
   */
  public int getVectorIndex() {
    return vectorIndex;
  }

  @Override
  public int compareTo(final Member o) {
    int indexDiff = this.vectorIndex - o.vectorIndex;
    return (indexDiff != 0) ? indexDiff : name.compareTo(o.name);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member member = (Member) o;
    return vectorIndex == member.vectorIndex && Objects.equals(name, member.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, vectorIndex);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Member{");
    sb.append("name='").append(name).append('\'');
    sb.append(", vectorIndex=").append(vectorIndex);
    sb.append('}');
    return sb.toString();
  }
}
