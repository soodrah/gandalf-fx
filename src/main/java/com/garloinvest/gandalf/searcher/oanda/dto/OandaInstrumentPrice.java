package com.garloinvest.gandalf.searcher.oanda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OandaInstrumentPrice {

  private boolean tradeable;
  private LocalDateTime time;
  private BigDecimal sell; //Bid
  private Long liquiditySell;
  private BigDecimal buy; //Ask
  private Long liquidityBuy;
  private String instrument;

  public OandaInstrumentPrice() {
  }

  public boolean isTradeable() {
      return tradeable;
  }

  public void setTradeable(boolean tradeable) {
      this.tradeable = tradeable;
  }

  public LocalDateTime getTime() {
      return time;
  }

  public void setTime(LocalDateTime time) {
      this.time = time;
  }

  public BigDecimal getSell() {
      return sell;
  }

  public void setSell(BigDecimal sell) {
      this.sell = sell;
  }

  public Long getLiquiditySell() {
      return liquiditySell;
  }

  public void setLiquiditySell(Long liquiditySell) {
      this.liquiditySell = liquiditySell;
  }

  public BigDecimal getBuy() {
      return buy;
  }

  public void setBuy(BigDecimal buy) {
      this.buy = buy;
  }

  public Long getLiquidityBuy() {
      return liquidityBuy;
  }

  public void setLiquidityBuy(Long liquidityBuy) {
      this.liquidityBuy = liquidityBuy;
  }

  public String getInstrument() {
	return instrument;
}

public void setInstrument(String instrument) {
	this.instrument = instrument;
}

@Override
public String toString() {
	return "OandaInstrumentPrice [tradeable=" + tradeable + ", time=" + time + ", sell=" + sell + ", liquiditySell="
			+ liquiditySell + ", buy=" + buy + ", liquidityBuy=" + liquidityBuy + ", instrument=" + instrument + "]";
}
}
