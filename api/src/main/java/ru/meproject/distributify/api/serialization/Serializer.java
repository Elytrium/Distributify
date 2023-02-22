package ru.meproject.distributify.api.serialization;

@FunctionalInterface
public interface Serializer<V> {
    String serialize(V value) throws Exception;
}
