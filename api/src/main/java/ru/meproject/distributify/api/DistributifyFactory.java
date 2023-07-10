package ru.meproject.distributify.api;

import ru.meproject.distributify.api.platform.PlatformType;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;
import ru.meproject.distributify.api.structures.DistributedHashMap;
import ru.meproject.distributify.api.structures.DistributedLongCounter;
import ru.meproject.distributify.api.structures.DistributedSafeHashMap;

import java.util.function.Consumer;

/**
 * This interface should be implemented by any driver that wishes to expose database functionality in Java-like manner
 */
public interface DistributifyFactory<C extends DistributifyDriverConfig> {

    PlatformType platform();

    String instanceName();

    <V> DistributedHashMap<V> hashMap(DistributifyDriverConfig config, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler);

    <V> DistributedSafeHashMap<V> safeHashMap(DistributifyDriverConfig config, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler);

    DistributedLongCounter longCounter(DistributifyDriverConfig config);
}
