package ru.sbt.CacheUtil;

import java.io.NotSerializableException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Acer on 02.12.2016.
 */
public class CacheProxy implements InvocationHandler {
    private final Object delegate;
    private final List<Object> fileLine = new ArrayList<>();
    private final Map<CacheType, CacheMaster> cacheMasters = new HashMap<>();

    private CacheProxy(Object delegate) {
        cacheMasters.put(CacheType.FILE, new FileCacheMaster());
        cacheMasters.put(CacheType.IN_MEMORY, new MemoryCacheMaster());
        this.delegate = delegate;
    }

    public static <T> T cache(T delegate) {
        return (T) Proxy.newProxyInstance(
                delegate.getClass().getClassLoader(),
                delegate.getClass().getInterfaces(),
                new CacheProxy(delegate));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Cache annotation = method.getAnnotation(Cache.class);
        if (annotation == null)
            return method.invoke(delegate, args);
        List<Object> key = getKey(method, args, annotation);
        CacheMaster cacheMaster = cacheMasters.get(annotation.cacheType());
        Object result = null;
        try {
            result = cacheMaster.checkCache(key, annotation);
            if (result == null) {
                result = method.invoke(delegate, args);
                if (Arrays.asList(result.getClass().getInterfaces()).contains(List.class))
                    result = ((List) result).stream().limit(annotation.listList()).collect(Collectors.toList());
                cacheMaster.updateCache(key, result, annotation);
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return method.invoke(delegate, args);
        }
        return result;
    }

    private List<Object> getKey(Method method, Object[] args, Cache annotation) {
        ArrayList<Object> key = new ArrayList<>();
        String fileNamePrefix = annotation.fileNamePrefix();
        key.add(fileNamePrefix.equals("") ? method.getName() : fileNamePrefix);
        Class[] classes = annotation.identityBy();
        if (classes.length == 0) {
            key.addAll(Arrays.asList(args));
        } else {
            List<Object> argsList = new ArrayList<>(Arrays.asList(args));
            for (Class aClass : classes) {
                argsList.removeIf(o -> {
                    if (o.getClass().equals(aClass)) {
                        key.add(o);
                        return true;
                    }
                    return false;
                });
            }
        }
        return key;
    }

}
