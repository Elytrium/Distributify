package ru.meproject.distributify.api.jedis;

import ru.meproject.distributify.api.DistributedLongCounter;
import redis.clients.jedis.JedisPool;
import ru.meproject.distributify.api.DistributifyDriverConfig;

public class JedisDistributedLongCounter implements DistributedLongCounter {
    private final JedisPool jedisPool;
    private final JedisDriverConfig config;

    public JedisDistributedLongCounter(JedisPool jedisPool, DistributifyDriverConfig config) {
        this.jedisPool = jedisPool;
        this.config = (JedisDriverConfig) config;
    }

    @Override
    public void add(long x) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            transaction.incrBy(config.keyPattern(), x);
            transaction.exec();
        }
    }

    @Override
    public void increment() {
        add(1);
    }

    @Override
    public void decrement() {
        add(-1);
    }

    @Override
    public long sum() {
        try (var jedis = jedisPool.getResource()) {
            return Long.parseLong(jedis.get(config.keyPattern()));
        }
    }

    @Override
    public void reset() {
        try (var transaction = jedisPool.getResource().multi()) {
            transaction.set(config.keyPattern(), "0");
            transaction.exec();
        }
    }

    @Override
    public long sumAndReset() {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            final var sum = transaction.get(config.keyPattern());
            transaction.set(config.keyPattern(), "0");
            transaction.exec();
            return Long.parseLong(sum.get());
        }

    }
}
