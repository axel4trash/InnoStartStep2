package edu.innotech.Task1;

import lombok.ToString;

@ToString
public enum ECurrency {
    RUB("RUB", "Российский рубль", "643"),
    USD("USD", "Доллар США", "840"),
    EUR("EUR", "Евро", "978"),
    AED("AED", "Дирхам", "784"),
    JPY("JPY", "Японская йена", "392"),
    XAG("TRY", "Турецкая лира", "949");

    private final String code;
    private final String name;
    private final String iso;

    ECurrency(String code, String name, String iso){
        this.code = code;
        this.name = name;
        this.iso = iso;
    }

    public String getCode() {
        return code;
    }

    public String getIso() {
        return iso;
    }

}
