package edu.innotech.Task3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CachingHandler<T> implements InvocationHandler {
    private final Object curObj;
    //ключом внешней мапы является состояние объекта, ключом внутренней мапы - метод, а значение - результат метода
    private final Map<State, Map<Method, Result>> states = new HashMap<>();
    //private Map<Method, Object> cacheMap = new ConcurrentHashMap<>();
    //вспомогательные поля:
    //значение внешней мапы = результат метода (будут удаляться значения --> разделяемый ресурс)
    private Map<Method, Result> cacheResults = new ConcurrentHashMap<>();
    //текущее состояние мапы
    private State curState = new State();

    public CachingHandler(T objectIncome) {
        this.curObj = objectIncome;
        states.put(curState, cacheResults);

        //запуск очистки в один поток
        ScheduledExecutorService cacheCleaner = Executors.newSingleThreadScheduledExecutor(it -> {
            Thread th = new Thread(it);
            th.setDaemon(true);
            return th;
        });

        //реализация очистки кэша каждую 1 секунду
        cacheCleaner.scheduleAtFixedRate(() -> {
            for(Map<Method, Result> map : states.values()){
                for (Method met : map.keySet()){
                    Result result = map.get(met);
                    if (result.expireTime == 0) continue;
                    //очищаем кэш метода, если наступило время очистки
                    if (result.expireTime < System.currentTimeMillis()){
                        System.out.println("CacheClean: GO!");
                        map.remove(met);
                    } else {System.out.println("CacheClean: NOT YET!");}
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method curMethod;
        Object objectResult;

        //получение метода по имени и параметрам
        curMethod = this.curObj.getClass().getMethod(method.getName(), method.getParameterTypes());
        System.out.println(" method="+curMethod.getName()+ " returns=" + curMethod.getReturnType().getName());
        System.out.println("   annotations: " + Arrays.toString(curMethod.getAnnotations()));

        if (curMethod.isAnnotationPresent(Cache.class))
        {
            //значение времени жизни кэша для текущего метода
            long lifeTime = curMethod.getAnnotation(Cache.class).lifeTime();
            Result cacheResult = cacheResults.get(curMethod);
            if (cacheResult!=null) {
                //продлеваем время жизни
                System.out.println("Cache: продлеваем текущее время жизни expireTime = " + cacheResult.expireTime + " lifeTime="+lifeTime);
                cacheResult.expireTime = System.currentTimeMillis() + lifeTime;
                System.out.println("Cache: новое время жизни = " + cacheResult.expireTime);
                if (lifeTime == 0) cacheResult.expireTime = 0L;
                //кладем заново на случай, если его удалили в другом потоке
                cacheResults.put(curMethod, cacheResult);
                System.out.println("Cache: 1 value="+ cacheResult.value);
                return cacheResult.value;
            }
            System.out.println("Cache: метод не закэширован -> вызываем его, кэшируем результат и возвращаем его");
            //метод не закэширован -> вызываем его, кэшируем результат и возвращаем его
            objectResult = method.invoke(curObj, args);
            //сохранили результат с ожидаемым временем жизни
            cacheResult = new Result(System.currentTimeMillis() + lifeTime, objectResult);
            if (lifeTime == 0) cacheResult.expireTime = 0L;
            cacheResults.put(curMethod, cacheResult);
            System.out.println("Cache: 2 value="+ cacheResult.value);
            return objectResult;
        }

        if (curMethod.isAnnotationPresent(Mutator.class)){ //мутатор
            System.out.println("Mutator: curMethod="+ curMethod.getName());
            curState = new State(curState, curMethod, args);
            System.out.println("Mutator: curState="+ curState);
            if (states.containsKey(curState)){
                cacheResults = states.get(curState);
            }else{
                cacheResults = new ConcurrentHashMap<>();
                states.put(curState, cacheResults);
            }

            //действия по сбросу кэша перенесены в ScheduledExecutorService
            //new CacheCleaner().start();
        }
        System.out.println(" Просто вызываем метод");
        return method.invoke(this.curObj, args);

    }
}


