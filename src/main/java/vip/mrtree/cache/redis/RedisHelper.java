package vip.mrtree.cache.redis;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import vip.mrtree.cache.interfact.CacheHelper;
import vip.mrtree.utils.CollectionUtils;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Qualifier("cacheHelper")
@ConditionalOnExpression("${cache.switch:false} and ${cache.redis:false}")
public class RedisHelper implements CacheHelper {

    @Resource
    private RedissonClient redissonClient;

    public RedisHelper() {

    }

    public RedisHelper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void put(String cacheName, String key, @NotNull Object value) {
        put(cacheName, key, value, DEFAULT_CACHE_CYCLE);
    }

    @Override
    public void put(String cacheName, String key, @NotNull Object value, long duration) {
        RBucket<Object> bucket = redissonClient.getBucket(generateCacheKey(cacheName, key));
        if (duration > 0) {
            bucket.set(value, duration, TimeUnit.SECONDS);
        } else {
            bucket.set(value);
        }
    }

    @Override
    public Object get(String cacheName, String key) {
        return get(cacheName, key, Object.class);
    }

    @Override
    public <T> T get(String cacheName, String key, @NotNull Class<T> tClass) {
        RBucket<T> bucket = redissonClient.getBucket(generateCacheKey(cacheName, key));
        return bucket.get();
    }

    @Override
    public void remove(String cacheName, String key) {
        RKeys keys = redissonClient.getKeys();
        keys.delete(generateCacheKey(cacheName, key));
    }

    @Override
    public void addSet(String cacheName, String key, String value) {
        addSet(cacheName, key, value, 0);
    }

    @Override
    public void addSet(String cacheName, String key, String value, long second) {
        RSet<String> set = redissonClient.getSet(generateCacheKey(cacheName, key));
        set.add(value);
        if (second > 0) {
            set.expire(Instant.now().plusSeconds(second));
        }
    }

    @Override
    public Set<String> getSet(String cacheName, String key) {
        RSet<String> set = redissonClient.getSet(generateCacheKey(cacheName, key));
        return set.readAll();
    }

    @Override
    public String getSetRandomItem(String cacheName, String key) {
        Set<String> set = getSet(cacheName, key);
        if (CollectionUtils.isEmpty(set)) {
            return null;
        }
        return ((RSet<String>) set).random();
    }

    @Override
    public void deleteSetItem(String cacheName, String key, String value) {
        RSet<String> set = redissonClient.getSet(generateCacheKey(cacheName, key));
        set.remove(value);
    }

    @Override
    public boolean contains(String cacheName, String key, @NotNull String value) {
        return getSet(cacheName, key).contains(value);
    }

    /**
     * 获取Redis分布式锁
     * <br>
     *
     * @author wangyunshu
     */
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * 解锁
     * <br>
     *
     * @author wangyunshu
     */
    public void unlock(RLock lock) {
        if (lock == null) {
            return;
        }
        if (lock.isLocked()) {
            lock.unlock();
        }
    }
}
