package ru.meproject.distributify.api.jedis;

import ru.meproject.distributify.api.DistributedLongCounter;
import redis.clients.jedis.JedisPool;

public class JedisDistributedLongCounter implements DistributedLongCounter {
    private final JedisPool jedisPool;
    private final String redisLongAdderKey;

    public JedisDistributedLongCounter(JedisPool jedisPool, String redisLongAdderKey) {
        this.jedisPool = jedisPool;
        this.redisLongAdderKey = redisLongAdderKey;
    }

    @Override
    public void add(long x) {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            transaction.incrBy(redisLongAdderKey, x);
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
            return Long.parseLong(jedis.get(redisLongAdderKey));
        }
    }

    @Override
    public void reset() {
        try (var transaction = jedisPool.getResource().multi()) {
            transaction.set(redisLongAdderKey, "0");
            transaction.exec();
        }
    }

    @Override
    public long sumAndReset() {
        try (var jedis = jedisPool.getResource()) {
            var transaction = jedis.multi();
            final var sum = transaction.get(redisLongAdderKey);
            transaction.set(redisLongAdderKey, "0");
            transaction.exec();
            return Long.parseLong(sum.get());
        }

    }
}
