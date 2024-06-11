package edu.innotech.Task2;

import lombok.Getter;

public class Fraction implements Fractionable{
    @Getter private int num;
    @Getter private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator(keyCache = "doubleValue")
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator(keyCache = "doubleValue")
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(keyCache = "doubleValue")
    public double doubleValue() {
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

