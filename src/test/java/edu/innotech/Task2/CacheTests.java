package edu.innotech.Task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CacheTests {
    @Test
    @DisplayName("Test 1: Кэшируемый метод вызывается 1 раз")
    public void checkCountCache(){
        TestFraction tstfraction = new TestFraction(2, 3);
        Fractionable froxy = Utils.cache(tstfraction);
        froxy.doubleValue();
        froxy.doubleValue();

        Assertions.assertEquals(1, tstfraction.count);
    }

    @Test
    @DisplayName("Test 2: Сравнение результата из кэша с результатом метода")
    public void checkValueFromCache(){
        Fraction fraction = new Fraction(2, 3);
        Fractionable froxy = Utils.cache(fraction);
        double dRes = froxy.doubleValue();
        double dResCache = froxy.doubleValue();
        Assertions.assertEquals(dRes, dResCache);
        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 3: Сравнение результата из кэша с эталоном")
    public void checkValueFromCacheEta(){
        Fraction fraction = new Fraction(1, 2);
        Fractionable froxy = Utils.cache(fraction);
        froxy.doubleValue();
        double dResCache = froxy.doubleValue();
        Assertions.assertEquals(0.5, dResCache);
        System.out.println("OK");
    }

    @Test
    @DisplayName("Test 4: Проверка сброса кэша после вызова мутатора")
    public void checkClearCache(){
        TestFraction tstFraction = new TestFraction(1, 2);
        Fractionable froxy = Utils.cache(tstFraction);
        froxy.doubleValue();
        double dResCache = froxy.doubleValue();
        froxy.setNum(5);

        Assertions.assertEquals(0, tstFraction.count);
        System.out.println("OK");
    }
}
