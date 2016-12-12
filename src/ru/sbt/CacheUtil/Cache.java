package ru.sbt.CacheUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Acer on 02.12.2016.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Cache {

    CacheType cacheType() default CacheType.IN_MEMORY;

    String fileNamePrefix() default "";//"" = MethodName

    boolean zip() default false;

    Class[] identityBy() default {}; //{} = all Arguments

    long listList() default 100_000;
}
