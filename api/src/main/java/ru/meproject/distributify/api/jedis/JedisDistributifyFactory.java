package ru.meproject.distributify.api.jedis;

import ru.meproject.distributify.api.DistributedHashMap;
import ru.meproject.distributify.api.DistributedLongCounter;
import ru.meproject.distributify.api.DistributifyFactory;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;

public class JedisDistributifyFactory implements DistributifyFactory {

    private final JedisPool jedisPool;

    public JedisDistributifyFactory() {
        // TODO: config values
        jedisPool = new JedisPool("localhost", 6379);
    }

    @Override
    public <V> DistributedHashMap<V> hashMap(String redisHashMapPattern, Serializer<V> serializer, Deserializer<V> deserializer, Consumer<Exception> exceptionHandler) {
        return new JedisDistributedHashMap<>(jedisPool, redisHashMapPattern, serializer, deserializer, exceptionHandler);
    }

    @Override
    public DistributedLongCounter longCounter(String redisLongAddrPattern) {
        return new JedisDistributedLongCounter(jedisPool, redisLongAddrPattern);
    }

}
