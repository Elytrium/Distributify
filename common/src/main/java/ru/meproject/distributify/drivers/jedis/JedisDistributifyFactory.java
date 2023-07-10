package ru.meproject.distributify.drivers.jedis;

import redis.clients.jedis.JedisPoolConfig;
import ru.meproject.distributify.api.*;
import ru.meproject.distributify.api.platform.PlatformType;
import ru.meproject.distributify.api.serialization.Deserializer;
import ru.meproject.distributify.api.serialization.Serializer;
import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.structures.DistributedHashMap;
import ru.meproject.distributify.api.structures.DistributedLongCounter;
import ru.meproject.distributify.api.structures.DistributedSafeHashMap;

import java.util.function.Consumer;

public class JedisDistributifyFactory implements DistributifyFactory {

    private final JedisPool jedisPool;

    public JedisDistributifyFactory(String hostname, int port, int timeout, String password, int maxTotal, int maxIdle) {
        var jedisConfig = new JedisPoolConfig();
        jedisConfig.setMaxTotal(maxTotal);
        jedisConfig.setMaxIdle(maxIdle);
        jedisPool = new JedisPool(jedisConfig, hostname, port, timeout, password);
    }

    @Override
    public PlatformType platform() {
        return null;
    }

    @Override
    public String instanceName() {
        return null;
    }

    @Override
    public DistributedHashMap hashMap(DistributifyDriverConfig config, Serializer serializer, Deserializer deserializer, Consumer exceptionHandler) {
        return null;
    }

    @Override
    public DistributedSafeHashMap safeHashMap(DistributifyDriverConfig config, Serializer serializer, Deserializer deserializer, Consumer exceptionHandler) {
        return new JedisDistributedSafeHashMap<>(jedisPool, config, serializer, deserializer, exceptionHandler);
    }

    @Override
    public DistributedLongCounter longCounter(DistributifyDriverConfig config) {
        return null;
    }

}
