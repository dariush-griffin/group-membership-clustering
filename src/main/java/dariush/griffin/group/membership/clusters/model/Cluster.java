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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * <p>A cluster is a collection of {@link Group}s. Since, {@link Group}s represents point in space the cluster could
 * also be thought of as a collection of points in space.</p>
 *
 * @author Dariush Griffin
 */
public class Cluster
{
  /**
   * The {@link Group}s of this cluster.
   */
  private final Set<Group> groups;

  /**
   * <p>Constructs a new Cluster with an empty set of {@link Group}s.</p>
   */
  public Cluster() {
    groups = new HashSet<>();
  }

  /**
   * <p>Adds the provided {@link Group} to the Cluster. If the {@link Group} already exists in the cluster it is not
   * added.</p>
   *
   * @param group The {@link Group} to be added to this cluster.
   */
  public void addGroup(Group group) {
    groups.add(group);
  }

  /**
   * <p>Adds all {@link Group}s to the cluster. Duplicates are not added.</p>
   *
   * @param groups A collection of {@link Group}s to be added to the cluster.
   */
  public void addGroups(Collection<Group> groups) {
    for (Group group : groups) {
      addGroup(group);
    }
  }

  /**
   * @return An unmodifiable set of the {@link Group}s in this cluster.
   */
  public Set<Group> getGroups() {
    return Collections.unmodifiableSet(groups);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cluster cluster = (Cluster) o;
    return
        Objects.equals(groups, cluster.groups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groups);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Cluster{");
    sb.append("groups=").append(groups);
    sb.append('}');
    return sb.toString();
  }
}
