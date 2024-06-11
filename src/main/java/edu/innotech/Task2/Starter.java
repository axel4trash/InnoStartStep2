package edu.innotech.Task2;

public class Starter {
    public static void main(String... args) {
        /*
        System.out.println("=================1=======================");
        Fraction fr= new Fraction(2,3);
        Fractionable num = Utils.cache(fr);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит
        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        */
        /*System.out.println("==============2==========================");
        Fractionable fraction = new Fraction(5,2);
        System.out.println(fraction.doubleValue());

        fraction = new FractionCache(fraction); //расширяем объект возможностью кэширования
        System.out.println(fraction.doubleValue());
        System.out.println(fraction.doubleValue());
        System.out.println(fraction.doubleValue());
        fraction.setNum(10);
        System.out.println(fraction.doubleValue());
        System.out.println(fraction.doubleValue());
        System.out.println(fraction.doubleValue());*/

        System.out.println("==============3==========================");
        Fraction fr= new Fraction(2,3);
        Fractionable fraction2 = Utils.cache(fr);
        System.out.println(fraction2.doubleValue());
        System.out.println(fraction2.doubleValue());
        System.out.println(fraction2.doubleValue());
        fraction2.setNum(10);
        System.out.println(fraction2.doubleValue());
        System.out.println(fraction2.doubleValue());

    }


}
