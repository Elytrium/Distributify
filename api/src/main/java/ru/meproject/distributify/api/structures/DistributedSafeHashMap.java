package ru.meproject.distributify.api.structures;

/**
 * Blocking implementation of {@link DistributedHashMap}.
 * Key values are hardcoded as Strings.
 * @param <V> - value to put in storage
 */
public interface DistributedSafeHashMap<V> {

    void put(String key, V value);

    V get(String key);

    boolean remove(String key);

    boolean containsKey(String key);

    void putExpiring(String key, V value, long seconds);
}
