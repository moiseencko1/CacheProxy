package ru.sbt.CacheUtil;

import java.util.List;

/**
 * Created by Acer on 04.12.2016.
 */
public interface CacheMaster {
    Object checkCache(List<Object> key, Cache annotation);

    void updateCache(List<Object> key, Object result, Cache annotation);
}
