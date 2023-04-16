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

public class GroupCache
{
  private final Map<Member, Set<Group>> memberToGroups;

  public GroupCache() {
    this.memberToGroups = new HashMap<>();
  }

  public void addGroup(Group group) {
    for(Member member : group.getMembers().keySet()) {
      if(!memberToGroups.containsKey(member)) {
        memberToGroups.put(member, new TreeSet<>());
      }

      memberToGroups.get(member).add(group);
    }
  }

  public void addGroups(Collection<Group> groups) {
    groups.forEach(group -> {addGroup(group);});
  }

  public void addGroups(Cluster cluster){
    cluster.getGroups().forEach(group -> {addGroup(group);});
  }

  public Set<Group> getGroups(Member member) {
    return memberToGroups.get(member);
  }

  public Set<Group> getSimilarGroups(Group group) {
    Set<Group> result = new TreeSet<>();
    getSimilarGroups(group, result);
    return result;
  }

  public Set<Group> getSimilarGroups(Cluster cluster) {
    return getSimilarGroups(cluster.getGroups());
  }

  public Set<Group> getSimilarGroups(Collection<Group> groups) {
    Set<Group> result = new TreeSet<>();
    for(Group group : groups) {
      getSimilarGroups(group, result);
    }
    return result;
  }

  private void getSimilarGroups(Group group, Set<Group> accumulator) {
    for(Member member : group.getMembers().keySet()) {
      if(memberToGroups.containsKey(member)) {
        accumulator.addAll(memberToGroups.get(member));
      }
    }
  }
}
