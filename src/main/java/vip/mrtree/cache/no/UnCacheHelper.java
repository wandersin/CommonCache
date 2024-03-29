package vip.mrtree.cache.no;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import vip.mrtree.cache.interfact.CacheHelper;

import java.util.Set;

@Component
@Qualifier("cacheHelper")
@ConditionalOnExpression("!${cache.switch:false}")
public class UnCacheHelper implements CacheHelper {

    @Override
    public void put(String cacheName, String key, @NotNull Object value) {

    }

    @Override
    public void put(String cacheName, String key, @NotNull Object value, long duration) {

    }

    @Override
    public Object get(String cacheName, String key) {
        return null;
    }

    @Override
    public <T> T get(String cacheName, String key, @NotNull Class<T> tClass) {
        return null;
    }

    @Override
    public void remove(String cacheName, String key) {

    }

    @Override
    public void addSet(String cacheName, String key, Object value) {

    }

    @Override
    public void addSet(String cacheName, String key, Object value, long second) {

    }

    @Override
    public Set<Object> getSet(String cacheName, String key) {
        return null;
    }

    @Override
    public Object getSetRandomItem(String cacheName, String key) {
        return null;
    }

    @Override
    public void deleteSetItem(String cacheName, String key, Object value) {

    }
}
