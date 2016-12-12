package ru.sbt.MyService;

import ru.sbt.CacheUtil.Cache;
import ru.sbt.CacheUtil.CacheType;
import ru.sbt.Person;

import java.util.List;

/**
 * Created by Acer on 02.12.2016.
 */
public interface Service {
    @Cache(cacheType = CacheType.IN_MEMORY, identityBy = String.class)
    int doHardWork(String s, int i);

    @Cache(fileNamePrefix = "hello", cacheType = CacheType.FILE, listList = 30)
    List<Object> doSuperWork(int i);

    @Cache(cacheType = CacheType.FILE, zip = true, identityBy = String.class)
    String doMagicWork(String s, boolean b);

    @Cache(cacheType = CacheType.FILE)
    Person doPersonWork(String name);
}
