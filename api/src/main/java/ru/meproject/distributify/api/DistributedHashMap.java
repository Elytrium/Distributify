package ru.meproject.distributify.api;

/**
 * Not blocking API for HashMap.
 * Key values are hardcoded as Strings
 * @param <V> - value to put in storage
 */
public interface DistributedHashMap<V> {

    void put(String key, V value);

    V get(String key);

    boolean remove(String key);

    boolean containsKey(String key);
}
