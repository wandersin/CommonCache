package vip.mrtree.cache.redis;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import vip.mrtree.cache.interfact.CacheHelper;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Qualifier("cacheHelper")
@ConditionalOnExpression("${cache.switch:false} and ${cache.redis:false}")
public class RedisHelper implements CacheHelper {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void put(String cacheName, String key, Object value) {
        put(cacheName, key, value, DEFAULT_CACHE_CYCLE);
    }

    @Override
    public void put(String cacheName, String key, Object value, long duration) {
        RBucket<Object> bucket = redissonClient.getBucket(generateCacheKey(cacheName, key));
        bucket.set(value, duration, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String cacheName, String key) {
        return get(cacheName, key, Object.class);
    }

    @Override
    public <T> T get(String cacheName, String key, Class<T> tClass) {
        RBucket<T> bucket = redissonClient.getBucket(generateCacheKey(cacheName, key));
        return bucket.get();
    }

    @Override
    public void remove(String cacheName, String key) {
        RKeys keys = redissonClient.getKeys();
        keys.delete(generateCacheKey(cacheName, key));
    }
}
