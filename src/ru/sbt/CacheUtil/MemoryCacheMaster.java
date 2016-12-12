package ru.sbt.CacheUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Acer on 04.12.2016.
 */
public class MemoryCacheMaster implements CacheMaster {
    private final Map<List<Object>, Object> cacheMap = new HashMap<>();

    @Override
    public Object checkCache(List<Object> key, Cache annotation) {
        return cacheMap.get(key);
    }

    @Override
    public void updateCache(List<Object> key, Object result, Cache annotation) {
        cacheMap.put(key, result);
    }

}
