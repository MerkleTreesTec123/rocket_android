package com.muye.rocket.entity.exchange;

import java.io.Serializable;

/**
 * 交易对
 */
public class ExcTradingPair implements Serializable {
    private String id;
    private String buyCoinId;
    private String buyName;
    private String buyShortName;
    private String buyAppLogo;
    private String sellCoinId;
    private String sellName;
    private String sellShortName;
    private String sellAppLogo;
    private String buyFee;
    private String sellFee;
    private String tradeBlock;
    private String typeName;
    private String blockName;
    private String symbol;
    private String chg;
    private String p_new;
    private String buy;
    private String sell;
    private String sellSymbol;
    private String total;
    private String buySymbol;
    private String high;
    private String p_open;
    private String low;
    private String block;
    private String tradeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyCoinId() {
        return buyCoinId;
    }

    public void setBuyCoinId(String buyCoinId) {
        this.buyCoinId = buyCoinId;
    }

    public String getBuyName() {
        return buyName;
    }

    public void setBuyName(String buyName) {
        this.buyName = buyName;
    }

    public String getBuyShortName() {
        return buyShortName;
    }

    public void setBuyShortName(String buyShortName) {
        this.buyShortName = buyShortName;
    }

    public String getBuyAppLogo() {
        return buyAppLogo;
    }

    public void setBuyAppLogo(String buyAppLogo) {
        this.buyAppLogo = buyAppLogo;
    }

    public String getSellCoinId() {
        return sellCoinId;
    }

    public void setSellCoinId(String sellCoinId) {
        this.sellCoinId = sellCoinId;
    }

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public String getSellShortName() {
        return sellShortName;
    }

    public void setSellShortName(String sellShortName) {
        this.sellShortName = sellShortName;
    }

    public String getSellAppLogo() {
        return sellAppLogo;
    }

    public void setSellAppLogo(String sellAppLogo) {
        this.sellAppLogo = sellAppLogo;
    }

    public String getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(String buyFee) {
        this.buyFee = buyFee;
    }

    public String getSellFee() {
        return sellFee;
    }

    public void setSellFee(String sellFee) {
        this.sellFee = sellFee;
    }

    public String getTradeBlock() {
        return tradeBlock;
    }

    public void setTradeBlock(String tradeBlock) {
        this.tradeBlock = tradeBlock;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getChg() {
        return chg;
    }

    public void setChg(String chg) {
        this.chg = chg;
    }

    public String getP_new() {
        return p_new;
    }

    public void setP_new(String p_new) {
        this.p_new = p_new;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSellSymbol() {
        return sellSymbol;
    }

    public void setSellSymbol(String sellSymbol) {
        this.sellSymbol = sellSymbol;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBuySymbol() {
        return buySymbol;
    }

    public void setBuySymbol(String buySymbol) {
        this.buySymbol = buySymbol;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getP_open() {
        return p_open;
    }

    public void setP_open(String p_open) {
        this.p_open = p_open;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
