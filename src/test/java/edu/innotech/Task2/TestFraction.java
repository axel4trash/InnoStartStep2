package edu.innotech.Task2;

import lombok.Getter;

public class TestFraction implements Fractionable{
    @Getter private int num;
    @Getter private int denum;
    int count;
    public TestFraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator(keyCache = "doubleValue")
    public void setNum(int num) {
        count= Math.max((count-1), 0);
        this.num = num;
    }

    @Mutator(keyCache = "doubleValue")
    public void setDenum(int denum) {
        count= Math.max((count-1), 0);
        this.denum = denum;
    }

    @Override
    @Cache(keyCache = "doubleValue")
    public double doubleValue() {
        count++;
        System.out.println("invoke double value");
        return (double) num/denum;
    }

    @Override
    public String toString() {
        System.out.println("to string");
        return "Fraction{" +
                "num=" + num +
                ", denum=" + denum +
                '}';
    }
}

