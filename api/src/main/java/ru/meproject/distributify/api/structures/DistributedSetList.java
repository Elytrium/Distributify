package ru.meproject.distributify.api.structures;

import java.util.Set;

// distributify:<plugin-id>:<structure-name>:<partent-id>:<unique-id>

/**
 * DistributedSetList is for creating union of sets that can perform operations on it.
 * Each instance keeps track of data points in its own set. The union of said sets is set of all data points in a system.
 * The lowest data point in this structure must be a set. Meaning we operate with subsets only.
 * Also meaning we don't support adding and discarding players one by one.
 * The snapshot of instance set should be taken at fixed rate (or at some event if you want) and sent to the structure.
 * Implementation directives:
 * 1. If it's allowed for underlying driver, we should use transactional operations (ACID, reliable operations) to avoid cases when instance tries to read set union and one of subsets is missing because of its snapshot being "in-progress"
 * 2. If it's allowed for underlying driver, we should use expiry functionality or create clean up task (with heartbeat pattern or driver-side task) for cases where instance is down in irregular manner (e.g was killed) and its set was not snapshotted for a while.
 * Examples:
 * Cases like whole system player list.
 */
public interface DistributedSetList<E> {

    /**
     * Send snapshot of a subset to database
     * @param set - set of values to be sent
     */
    void snapshotSubSet(String key, Set<E> set);

    /**
     * Get latest subset from database
     * @return
     */
    Set<E> getSubSet();

    Set<E> getUnionSet();

}
