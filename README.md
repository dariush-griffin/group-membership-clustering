# Description
This library clusters groups based on group member's contributions. For example, suppose we have a collection of forums
and users of those forums. If we want to see which forums are potentially related, we can cluster them based on their
user contributions to each forum.

In this example each forum would correspond to a group and each user would be a member of one or more groups. We could
use their message post frequency, or thread creation, as a way to create a contribution weight for each group. The
agglomerative algorithm would then produce clusters of groups, or forums, who had similar members contributing with
relative frequency.

# Example Usage
Groups and members must be placed into a `MembershipMapping` and passed to the various clustering algorithms. For
agglomerative clustering a minimum squared Euclidean distance must be supplied as well.

```java
MembershipMapping membershipMapping = new MembershipMapping();

membershipMapping.addMemberToGroup("member-zero", "group-zero", .2F);
membershipMapping.addMemberToGroup("member-one", "group-zero", .8F);
membershipMapping.addMemberToGroup("member-one", "group-one", .9F);
membershipMapping.addMemberToGroup("member-two", "group-one", .1F);
membershipMapping.addMemberToGroup("member-two", "group-two", .9F);
membershipMapping.addMemberToGroup("member-three", "group-two", .1F);

AgglomerativeClusterAlgorithm algorithm = new AgglomerativeClusterAlgorithm(membershipMapping, 0.6F);

Set<Cluster> clusters = algorithm.cluster();
```

This will result in two clusters. A cluster that contains `group-zero` and `group-one` and a second cluster that
contains just `group-two`.

# Group Membership Mapping and Clustering Briefly Explained

# Requirements
- Java 8 or greater.
