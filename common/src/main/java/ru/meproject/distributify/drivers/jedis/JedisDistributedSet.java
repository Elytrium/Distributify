package ru.meproject.distributify.drivers.jedis;

import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.DistributifyDriverConfig;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;
import ru.meproject.distributify.api.structures.DistributedSetList;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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
    public void snapshotSubSet(String parentKey, Set<V> set) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            var serializedSetKey = getRedisKey(parentKey) + UUID.randomUUID();
            for (V value: set) {
                transaction.sadd(serializedSetKey, serializer.serialize(value));
            }
            transaction.sadd(getRedisKey(parentKey), serializedSetKey);
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    @Override
    public Set<V> getSubSet(String parentKey, String key) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            var unserializedSet = transaction.smembers(getRedisKey(parentKey) + ":" + key).get();
            var serializedSet = new HashSet<V>();

            for (String item: unserializedSet) {
                serializedSet.add(deserializer.deserialize(item));
            }

            return serializedSet;
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return null;
        }
    }

    @Override
    public Set<V> getUnionSet(String parentKey) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            var setWithKeys = transaction.smembers(getRedisKey(parentKey)).get();
            var resultUnserializedSet = new HashSet<String>();

            for (String key: setWithKeys) {
                resultUnserializedSet.addAll(transaction.smembers(getRedisKey(parentKey) + ":" + key).get());
            }

            var serializedSet = new HashSet<V>();

            for (String item: resultUnserializedSet) {
                serializedSet.add(deserializer.deserialize(item));
            }

            return serializedSet;
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return null;
        }
    }

    private String getRedisKey(String key) {
        return config.keyPattern() + ":"+structureId+":" + key;
    }
}
