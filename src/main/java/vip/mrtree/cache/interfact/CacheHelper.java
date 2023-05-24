package vip.mrtree.cache.interfact;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vip.mrtree.utils.StringUtils;

/**
 * CacheHelper, define some cache APIs
 * <br>
 *
 * @author wangyunshu
 */
public interface CacheHelper {
    long DEFAULT_CACHE_CYCLE = 14400L;

    /**
     * put cache
     * <br>
     *
     * @author wangyunshu
     */
    void put(@NotBlank String cacheName, @NotBlank String key, @NotNull Object value);

    /**
     * put cache with duration
     * <br>
     *
     * @author wangyunshu
     */
    void put(@NotBlank String cacheName, @NotBlank String key, @NotNull Object value, long duration);

    /**
     * get cache
     * <br>
     *
     * @author wangyunshu
     */
    Object get(@NotBlank String cacheName, @NotBlank String key);

    /**
     * get cache with cache type
     * <br>
     *
     * @author wangyunshu
     */
    <T> T get(@NotBlank String cacheName, @NotBlank String key, @NotNull Class<T> tClass);

    /**
     * remove key
     * <br>
     *
     * @author wangyunshu
     */
    void remove(@NotBlank String cacheName, @NotBlank String key);

    /**
     * generate cache key
     * <br>
     *
     * @author wangyunshu
     */
    default String generateCacheKey(String cacheName, String key) {
        return StringUtils.join(cacheName, ":", key);
    }
}
