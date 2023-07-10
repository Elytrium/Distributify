package ru.meproject.distributify.drivers.jedis;

import ru.meproject.distributify.api.DistributifyDriverConfig;

/**
 * keyPattern:
 * The key that will be used to generate keys for structure and/or pattern for such key.
 * 1. For example HashMaps will treat keyPattern as pattern.
 * If you supply "distributify:cache" as key pattern, it will construct following key for "thisismykey":
 * "distributify:cache:thisismykey"
 * 2. For example LongCounter will treat keyPattern as key.
 * If you supply "distributify:counter" as keyPattern, it will keep the counter in Redis accessible by said key.
 *
 * expireSeconds:
 * Some Redis based structures can benefit by automatic expiry of entries. Some structures do not use this value at all.
 * This behaviour can be configured as following:
 * To disable expiry, set value to 0L.
 * To enable expiry, set value to seconds that value need to be present in storage
 */
public class JedisDriverConfig implements DistributifyDriverConfig {
    private String keyPattern = null;
    private long expireSeconds = 0L;

    public JedisDriverConfig() {}

    public String keyPattern() {
        return keyPattern;
    }

    public JedisDriverConfig keyPattern(String keyPattern) {
        this.keyPattern = keyPattern;
        return this;
    }

    public long expireSeconds() {
        return expireSeconds;
    }

    public JedisDriverConfig expireSeconds(long expireSeconds) {
        this.expireSeconds = expireSeconds;
        return this;
    }
}
