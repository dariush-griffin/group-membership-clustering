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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import dariush.griffin.group.membership.clusters.model.Cluster;
import dariush.griffin.group.membership.clusters.model.Group;
import dariush.griffin.group.membership.clusters.model.Member;

/**
 * <p>A collection of {@link Group}s used to find {@link Group} that contain a {@link Member} or "similar"
 * {@link Group}s. {@link Group}s are similar if they contain at least one shared {@link Member}.</p>
 *
 * @author Dariush Griffin
 */
public class GroupCache
{
  /**
   * A mapping of {@link Member} to {@link Group}s it belongs to.
   */
  private final Map<Member, Set<Group>> memberToGroups;

  public GroupCache() {
    this.memberToGroups = new HashMap<>();
  }

  /**
   * <p>Adds the {@link Group} to the cache.</p>
   *
   * @param group The {@link Group} that will be added to the cache.
   */
  public void addGroup(Group group) {
    for (Member member : group.getMembers().keySet()) {
      if (!memberToGroups.containsKey(member)) {
        memberToGroups.put(member, new TreeSet<>());
      }

      memberToGroups.get(member).add(group);
    }
  }

  /**
   * <p>Adds all {@link Group}s to the cache.</p>
   *
   * @param groups The {@link Group}s that will be added to the cache.
   */
  public void addGroups(Collection<Group> groups) {
    groups.forEach(group -> {
      addGroup(group);
    });
  }

  /**
   * <p>Adds all {@link Group}s in the {@link Cluster} to the cache.</p>
   *
   * @param cluster The {@link Cluster} whose {@link Group}s will be added to the cache.
   */
  public void addGroups(Cluster cluster) {
    cluster.getGroups().forEach(group -> {
      addGroup(group);
    });
  }

  /**
   * <p>Gets all {@link Group}s that contain the given {@link Member}.</p>
   *
   * @param member The {@link Member} whose {@link Group} groups we are attempting to find.
   * @return A set of {@link Group} that contains the provided {@link Member}.
   */
  public Set<Group> getGroups(Member member) {
    return memberToGroups.get(member);
  }

  /**
   * <p>A "similar" {@link Group} is a {@link Group} that contains at least one shared {@link Member}.</p>
   *
   * @param group The {@link Group} whose {@link Member}s we will use to find "similar" {@link Group}s.
   * @return A set of {@link  Group}s that contain a {@link Member} shared with the provided {@link  Group}.
   */
  public Set<Group> getSimilarGroups(Group group) {
    Set<Group> result = new TreeSet<>();
    getSimilarGroups(group, result);
    return result;
  }

  /**
   * <p>For each {@link Group} in the {@link Cluster} find "similar" {@link Group}s. A "similar" {@link Group} is a
   * group that contains at least one shared {@link Member}.</p>
   *
   * @param cluster The {@link Cluster} whose {@link Group}s we will use to find "similar" {@link Group}s.
   * @return A set of {@link Group}s that are "similar" to the {@link Group}s in the provided {@link Cluster}.
   */
  public Set<Group> getSimilarGroups(Cluster cluster) {
    return getSimilarGroups(cluster.getGroups());
  }

  /**
   * <p>For each provided {@link Group}, find "similar" {@link Group}s. A "similar" {@link Group} is a group that
   * contains at least one shared {@link Member}.</p>
   *
   * @param groups A collection of {@link Group}s we will use to find "similar" {@link Group}s.
   * @return A set of {@link Group}s that are "similar" to the provided {@link Group}s.
   */
  public Set<Group> getSimilarGroups(Collection<Group> groups) {
    Set<Group> result = new TreeSet<>();
    for (Group group : groups) {
      getSimilarGroups(group, result);
    }
    return result;
  }

  /**
   * <p>Finds "similar" {@link Group} to the provided {@link Group} but places the "similar" {@link Group} in the
   * provided Set.</p>
   *
   * @param group       The {@link Group} we will use to find "similar" {@link Group}s.
   * @param accumulator The set we will place the "similar" {@link Group}s in.
   */
  private void getSimilarGroups(Group group, Set<Group> accumulator) {
    for (Member member : group.getMembers().keySet()) {
      if (memberToGroups.containsKey(member)) {
        accumulator.addAll(memberToGroups.get(member));
      }
    }
  }
}
