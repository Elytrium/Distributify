package ru.meproject.distributify.api.serialization;

@FunctionalInterface
public interface Deserializer<V> {
    V deserialize(String value) throws Exception;
}
