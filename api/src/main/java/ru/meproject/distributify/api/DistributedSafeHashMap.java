package ru.meproject.distributify.api;

/**
 * Structures implementing this interface act as DistributedHashMap.
 * Persistence is not guaranteed by LimboSync nor underlying storage.
 */
public interface DistributedSafeHashMap<V> {

    void put(String key, V value);

    V get(String key);

    boolean remove(String key);

    boolean containsKey(String key);

    void putExpiring(String key, V value, long seconds);
}
