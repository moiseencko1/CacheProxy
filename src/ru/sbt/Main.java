package ru.sbt;

import ru.sbt.CacheUtil.CacheProxy;
import ru.sbt.MyService.Service;
import ru.sbt.MyService.ServiceImpl;

/**
 * Created by Acer on 02.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        Service service = CacheProxy.cache(new ServiceImpl());

        System.out.println(service.doPersonWork("Alex"));
        System.out.println(service.doPersonWork("Bob"));

        System.out.println(service.doMagicWork("high", true));
        System.out.println(service.doMagicWork("mood", true));
        System.out.println(service.doMagicWork("low", true));
        System.out.println(service.doMagicWork("mood", true));
        System.out.println(service.doMagicWork("live", true));
        System.out.println(service.doMagicWork("mood", false));
        System.out.println(service.doMagicWork("live", false));

        System.out.println(service.doSuperWork(19));
        System.out.println(service.doSuperWork(33));

        System.out.println(service.doHardWork("hi", 2));
        System.out.println(service.doHardWork("hi", 1));
        System.out.println(service.doHardWork("hi", 7));
        System.out.println(service.doHardWork("i", 2));

    }
}
