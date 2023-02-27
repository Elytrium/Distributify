package ru.meproject.distributify.api.jedis;

import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.DistributedHashMap;
import ru.meproject.distributify.api.DistributifyDriverConfig;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;

import java.util.function.Consumer;

public class JedisDistributedHashMap<V> implements DistributedHashMap<V> {
    private final JedisPool jedisPool;
    private final JedisDriverConfig config;
    private final Serializer<V> serializer;
    private final Deserializer<V> deserializer;
    private final Consumer<Exception> exceptionHandler;

    public JedisDistributedHashMap(JedisPool jedisPool, DistributifyDriverConfig config, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler) {
        this.jedisPool = jedisPool;
        this.config = (JedisDriverConfig) config;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Sets value to a key. Overwrites any existing key. Value will remain in Redis until explicitly removed or it expires.
     * @see <a href="https://redis.io/commands/hset/">HSET</a>
     * @param key
     * @param value
     */
    @Override
    public void put(String key, V value) {
        try (var jedis = jedisPool.getResource()) {
            final var serializedValue =  serializer.serialize(value);
            if (config.expireSeconds() != 0L) {
                var pipeline = jedis.pipelined();
                pipeline.hset(getRedisKey(key), key, serializedValue);
                pipeline.expire(getRedisKey(key), config.expireSeconds());
                return;
            }
            jedis.hset(getRedisKey(key), key, serializedValue);
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    /**
     * Gets value from storage. Returns null if value is not present.
     * @see <a href="https://redis.io/commands/hget/">HGET</a>
     * @param key
     * @return the value of object corresponding with key or null
     */
    @Override
    public V get(String key) {
        try (var jedis = jedisPool.getResource()) {
            final var notDeserialized = jedis.hget(getRedisKey(key), key);
            return deserializer.deserialize(notDeserialized);
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
        return null;
    }

    /**
     * Deletes specified key from storage.
     * @see <a href="https://redis.io/commands/del/">DEL</a>
     * @param key
     * @return True or False if value was removed
     */
    @Override
    public boolean remove(String key) {
        try (var jedis = jedisPool.getResource()) {
            return jedis.del(getRedisKey(key)) >= 1;
        }
    }

    /**
     * Checks if specified key exists. Doesn't actually check contents of said key, just the existence of it.
     * @see <a href="https://redis.io/commands/hexists/">HEXISTS</a>
     * @param key
     * @return True or False if key exists
     */
    @Override
    public boolean containsKey(String key) {
        try (var jedis = jedisPool.getResource()) {
            return jedis.hexists(getRedisKey(key), key);
        }
    }

    private String getRedisKey(String key) {
        return config.keyPattern() + ":" + key;
    }
}
