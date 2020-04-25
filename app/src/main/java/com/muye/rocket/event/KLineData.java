package com.muye.rocket.event;

import java.math.BigDecimal;
import java.util.List;

public class KLineData {
    private String symbol;
    private String second;
    private List<List<BigDecimal>> bigDecimal;

    public KLineData(String symbol, String second, List<List<BigDecimal>> bigDecimal) {
        this.symbol = symbol;
        this.second = second;
        this.bigDecimal = bigDecimal;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public List<List<BigDecimal>> getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(List<List<BigDecimal>> bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
