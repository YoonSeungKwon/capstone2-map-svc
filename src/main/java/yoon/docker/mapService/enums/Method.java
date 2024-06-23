package yoon.docker.mapService.enums;

import lombok.Getter;

@Getter
public enum Method {

    CREDIT_CARD("신용카드"),
    DEBIT_CARD("체크카드"),
    BANK_TRANSFER("계좌이체"),
    CASH("현금");

    private final String value;

    Method(String value){
        this.value = value;
    }

    public static Method fromString(String method) {
        for (Method m : Method.values()) {
            if (m.value.equalsIgnoreCase(method)) {
                return m;
            }
        }
        return CASH;
    }
}
