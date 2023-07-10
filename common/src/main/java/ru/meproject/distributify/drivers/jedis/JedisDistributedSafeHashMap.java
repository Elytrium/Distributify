package ru.meproject.distributify.drivers.jedis;

import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.DistributedSafeHashMap;
import ru.meproject.distributify.api.DistributifyDriverConfig;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;

import java.util.function.Consumer;

public class JedisDistributedSafeHashMap<V> implements DistributedSafeHashMap<V> {
    private final JedisPool jedisPool;
    private final JedisDriverConfig config;
    private final Serializer<V> serializer;
    private final Deserializer<V> deserializer;
    private final Consumer<Exception> exceptionHandler;

    public JedisDistributedSafeHashMap(JedisPool jedisPool, DistributifyDriverConfig config, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler) {
        this.jedisPool = jedisPool;
        this.config = (JedisDriverConfig) config;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void put(String key, V value) {
        try (var jedis = jedisPool.getResource()) {
            final var serializedValue = serializer.serialize(value);
            var transaction = jedis.multi();
            transaction.hset(getRedisKey(key), key, serializedValue);
            transaction.exec();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    @Override
    public V get(String key) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            final var notDeserialized = transaction.hget(getRedisKey(key), key);
            transaction.exec();
            return deserializer.deserialize(notDeserialized.get());
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
        return null;
    }

    @Override
    public boolean remove(String key) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            var removeResult = transaction.del(getRedisKey(key));
            transaction.exec();
            return removeResult.get() >= 1;
        }
    }

    @Override
    public boolean containsKey(String key) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            var containsKeyResult = transaction.hexists(getRedisKey(key), key);
            transaction.exec();
            return containsKeyResult.get();
        }
    }

    @Override
    public void putExpiring(String key, V value, long seconds) {
        try (var jedis = jedisPool.getResource()) {
            final var serializedValue = serializer.serialize(value);
            var transaction = jedis.multi();
            transaction.hset(getRedisKey(key), key, serializedValue);
            transaction.expire(getRedisKey(key), seconds);
            transaction.exec();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    private String getRedisKey(String key) {
        return config.keyPattern() + ":" + key;
    }
}
