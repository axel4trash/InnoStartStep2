package edu.innotech.Task3;

import edu.innotech.Task3.Fraction;
import edu.innotech.Task3.Fractionable;
import edu.innotech.Task3.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static java.lang.Thread.sleep;

public class CacheTests {
    @Test
    @DisplayName("Test 1: Тест для проверки времени жизни закэшированного значения")
    public void checkCacheLifeTime() throws InterruptedException {
        double result;

        Fraction fr = new Fraction(10,2);
        Fractionable chachableFr = Utils.cache(fr);

        // метод вернет ожидаемое значение, а результат закэшируется на время = 2000
        result = chachableFr.doubleValue();
        System.out.println("CheckPoint1: result="+result);
        Assertions.assertEquals(5, result);

        // Изменим denum методом, который не сбрасывает кэш
        chachableFr.setDenumNoCache(4);
        System.out.println(" изменили denum, ждем 2.5, легли спать 1000");
        // Состояние объекта изменилось, но из кэша должно быть прочитано старое значение, так как время жизни еще не истекло
        sleep (1000);
        result = chachableFr.doubleValue();
        System.out.println("CheckPoint2: result="+result);
        Assertions.assertEquals(5, result);
        System.out.println(" изменили поле, легли спать 5000");
        sleep(5000);

        result = chachableFr.doubleValue();
        System.out.println("CheckPoint3: result="+result);
        Assertions.assertEquals(2.5, result);
    }


    @Test
    @DisplayName("Test 2: Тест для проверки обновления кэша после вызова мутатора, время обновления кэша не наступило")
    public void checkMutator() throws InterruptedException {
        double result;

        Fraction fr = new Fraction(12,4);
        Fractionable chachableFr = Utils.cache(fr);

        // метод вернет ожидаемое значение, а результат закэшируется на время = 2000
        result = chachableFr.doubleValue();
        System.out.println("CheckPoint1: result="+result);
        Assertions.assertEquals(3, result);

        // Изменим denum методом-мутатором
        chachableFr.setDenum(2);
        System.out.println(" изменили denum, ждем 6, легли спать 1000");
        // Состояние объекта изменилось, кэш должен обновиться
        sleep (1000);

        result = chachableFr.doubleValue();
        System.out.println("CheckPoint2: result="+result);
        Assertions.assertEquals(6, result);
    }
}
