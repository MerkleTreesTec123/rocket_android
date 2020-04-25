package com.muye.rocket.entity.exchange;

import java.util.List;

public class ExcOrderEntity {
    private List<ExcOrder> entrutsHis;
    private List<ExcOrder> entrutsCur;

    public List<ExcOrder> getEntrutsHis() {
        return entrutsHis;
    }

    public void setEntrutsHis(List<ExcOrder> entrutsHis) {
        this.entrutsHis = entrutsHis;
    }

    public List<ExcOrder> getEntrutsCur() {
        return entrutsCur;
    }

    public void setEntrutsCur(List<ExcOrder> entrutsCur) {
        this.entrutsCur = entrutsCur;
    }


}
