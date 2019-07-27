package com.scmanis.mdds.Redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {
    private static JedisPool jedisPool;
    private static JedisFactory instance;

    private JedisFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        jedisPool = new JedisPool(System.getenv("REDIS_HOST"), Integer.parseInt(System.getenv("REDIS_PORT")));
        jedisPool.getResource();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public static JedisFactory getInstance() {
        if (instance == null) {
            instance = new JedisFactory();
        }

        return instance;
    }
}

