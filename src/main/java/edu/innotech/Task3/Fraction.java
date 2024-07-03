package edu.innotech.Task3;

import lombok.Getter;

public class Fraction implements Fractionable {
    @Getter private int num;
    @Getter private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator//(methods4refresh = {"toString", "doubleValue"})
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(lifeTime = 2000)
    public double doubleValue() {
        System.out.println("invoke double value");
        return (double) num/denum;
    }
    @Override
    public void setDenumNoCache(int denum){
        this.denum = denum;
    }

    @Override
    @Cache
    public String toString() {
        System.out.println("to string");
        return "Fraction{" +
                "num=" + num +
                ", denum=" + denum +
                '}';
    }
}

