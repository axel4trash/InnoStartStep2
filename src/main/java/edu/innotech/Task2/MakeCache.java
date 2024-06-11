package edu.innotech.Task2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*Метод invoke возвращает такую версию данного объекта, у которого вызовы всех методов, помеченных аннотацией @Cache, кэшируются.
При вызове кэшируемого метода необходимо проанализировать состояние объекта:
•       Если метод вызывается впервые после создания кэшируемого объекта - то он вызывается обычным образом.
•       Если метод вызывается повторно и с момента прошлого вызова внесены изменения в состояние объекта – то он вызывается обычным образом.
•       Если метод вызывается повторно и объект не был изменен с момента прошлого вызова – то вместо вызова метода необходимо вернуть то же значение, что возвращал метод при предыдущем вызове.
*/

public class MakeCache implements InvocationHandler {
    Object obj;
    //сам кэш
    Map<String, Object> cacheMap = new HashMap<>(); //ключ - параметр аннотаций keyCache

    public <T> MakeCache(T objectIncome) {
        this.obj = objectIncome;
    }

    //перехватывает вызовы к целевому объекту
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //вытащить аннотацию метода:
        // мутатор -> сброс кэша
        // кэшируемый -> либо вызвать метод, либо взять из кэша
        // иначе -> просто method.invoke
        //method.isAnnotationPresent(Mutator.class) - не найдет аннотацию, т.к. вызывается метод класса, а не интерфейса, надо из this.obj
        //аннотации на интерфейсе не интересуют, т.к. не позволяют настройть индивидуально конкретный объект
        if (method.getDeclaringClass().toString().contains("Fractionable")) {
            //ищем метод от объекта this.obj
            for (Method objMethod: this.obj.getClass().getDeclaredMethods())
            {
                if (objMethod.getName().equals(method.getName())) {             //проверка по имени метода
                    if (objMethod.isAnnotationPresent(Mutator.class)) { //мутатор - сброс кэша и выполнить метод
                        Mutator annotation = objMethod.getAnnotation(Mutator.class);
                        String cacheKey = annotation.keyCache();
                        if (this.cacheMap.containsKey(cacheKey)) {
                            //действия по обновлению кэша
                            this.cacheMap.remove(cacheKey);
                            System.out.println("Кэш очищен");
                        }else{
                            System.out.println("Кэш пустой для метода, пропускаем!");
                        }
                    } else if (objMethod.isAnnotationPresent(Cache.class)) {// кэшируемый - вызвать метод, либо взять из кэша
                        Cache annotation = objMethod.getAnnotation(Cache.class);
                        String cacheKey = annotation.keyCache();

                        if (this.cacheMap.containsKey(cacheKey)) {
                            System.out.println("Значение найдено в кэше!");
                            return this.cacheMap.get(cacheKey);
                        }

                        Object result = objMethod.invoke(this.obj, args);
                        this.cacheMap.put(cacheKey, result);
                        System.out.println("Рассчитанное значение поместили в кэш");
                        return result;
                    }
                }
            }
        }
        return method.invoke(this.obj, args);
}
}
