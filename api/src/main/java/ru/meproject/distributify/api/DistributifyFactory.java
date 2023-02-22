package ru.meproject.distributify.api;

import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;

import java.util.function.Consumer;

/**
 * This interface should be implemented by any driver that wishes to expose database functionality in Java-like manner
 */
public interface DistributifyFactory {

    <V> DistributedHashMap<V> hashMap(DistributifyDriverConfig config, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler);

    DistributedLongCounter longCounter(DistributifyDriverConfig config);
}
