package ru.meproject.distributify.drivers.jedis;

import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.DistributifyDriverConfig;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;
import ru.meproject.distributify.api.structures.DistributedSetList;

import java.util.Set;
import java.util.function.Consumer;

// distributify:<plugin-id>:<structure-name>:<partent-id>:<unique-id>
public class JedisDistributedSet<V> implements DistributedSetList<V> {
    private final JedisPool jedisPool;
    private final JedisDriverConfig config;
    private final Serializer<V> serializer;
    private final Deserializer<V> deserializer;
    private final Consumer<Exception> exceptionHandler;
    private final String structureId;

    public JedisDistributedSet(JedisPool jedisPool,
                               DistributifyDriverConfig config,
                               Serializer<V> serializer,
                               Deserializer<V> deserializer,
                               Consumer<Exception> exceptionHandler,
                               String structureId
    ) {
        this.jedisPool = jedisPool;
        this.config = (JedisDriverConfig) config;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.exceptionHandler = exceptionHandler;
        this.structureId = structureId;
    }
    @Override
    public void snapshotSubSet(String key, Set<V> set) {
        try (var jedis = jedisPool.getResource()) {
            for (V value: set) {
                var serializedValue = serializer.serialize(value);
                if (config.expireSeconds() != 0L) {
                    var pipeline = jedis.pipelined();
                    pipeline.sadd(getRedisKey(key), key, serializedValue);
                    pipeline.expire(getRedisKey(key), config.expireSeconds());
                    return;
                }
                jedis.sadd(getRedisKey(key), key, serializedValue);
            }
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    @Override
    public Set<V> getSubSet() {
        return null;
    }

    @Override
    public Set<V> getUnionSet() {
        return null;
    }

    private String getRedisKey(String key) {
        return config.keyPattern() + ":set:" + key;
    }
}
