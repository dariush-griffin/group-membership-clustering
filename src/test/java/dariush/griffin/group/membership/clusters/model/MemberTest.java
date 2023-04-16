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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemberTest
{
  @Test
  public void testMemberComparison() {
    Member zeroMember = new Member("Zero Member", 0);
    Member oneMember = new Member("One Member", 1);

    assertEquals(-1, zeroMember.compareTo(oneMember));
    assertEquals(1, oneMember.compareTo(zeroMember));
  }

  @Test
  public void testAccessors() {
    Member twoMember = new Member("Two Member", 2);

    assertEquals("Two Member", twoMember.getName());
    assertEquals(2, twoMember.getVectorIndex());
  }

  @Test
  public void testEqualsAndHashCode() {
    Member twoMember = new Member("Two Member", 2);
    Member otherTwoMember = new Member("Two Member", 2);

    assertEquals(true, twoMember.equals(otherTwoMember));
    assertEquals(true, twoMember.hashCode() == otherTwoMember.hashCode());
  }
}
