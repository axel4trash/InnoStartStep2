package edu.innotech.Task2;

//класс-обертка (прокси) для расширения функциональности класса без его изменения
public class FractionCache implements Fractionable{
    Fractionable fractionImpl;
    double tmp;
    boolean isChanged=true; //кэш актуальный или нет

    public FractionCache (Fractionable fractionable){
        this.fractionImpl = fractionable;
    }

    @Override
    public double doubleValue() {
        System.out.println(".FractionCache.doubleValue: isChanged="+isChanged+" tmp="+tmp);
        if (isChanged){tmp = fractionImpl.doubleValue();}
        isChanged = false;
        return tmp;
    }

    @Override
    public void setNum(int num) {
        isChanged = true;
        fractionImpl.setNum(num);
    }

    @Override
    public void setDenum(int denum) {
        isChanged = true;
        fractionImpl.setDenum(denum);
    }
}
