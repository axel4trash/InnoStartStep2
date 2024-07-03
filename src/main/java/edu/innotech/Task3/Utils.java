package edu.innotech.Task3;

import java.lang.reflect.Proxy;

public class Utils {
    @SuppressWarnings("unchecked")
    public static <T> T cache(T objectIncome) {
        return (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                new CachingHandler(objectIncome)
                //new CachingHandlerCopy(objectIncome)
        );
    }
}
