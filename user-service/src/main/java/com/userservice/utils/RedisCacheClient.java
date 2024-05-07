package com.userservice.utils;
// 导入必要的库
import org.springframework.cache.annotation.Cacheable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

// 创建一个全局的Jedis连接池
public class RedisCacheClient {

    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 根据实际情况配置连接池参数
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(50);
        poolConfig.setMinIdle(10);

        // 连接到Redis服务器
        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
    }

    // 获取缓存对象
    public static Jedis getResource() {
        return jedisPool.getResource();
    }

    // 释放资源
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    // 存入缓存
    public static void setToCache(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            // 错误处理
            e.printStackTrace();
        }
    }

    //从缓存中获取数据
    public static String getFromCache(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            // 错误处理
            e.printStackTrace();
            return null;
        }
    }

}

