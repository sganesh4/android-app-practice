package com.sushil.stockquoteapp;

public class StockInfo {
	private String daysHigh             = "";
	private String yearLow              = "";
	private String yearHigh             = "";
	private String name                 = "";
	private String lastTradePririceOnly = "";
	private String change               = "";
	private String daysRange            = "";
	public String getDaysLow() {
		return daysLow;
	}
	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}
	private String daysLow             = "";
	
	
	
	
	
	public StockInfo(String daysHigh, String yearLow, String yearHigh,
			String name, String lastTradePririceOnly, String change,
			String daysRange, String daysLow) {
		this.daysHigh = daysHigh;
		this.yearLow = yearLow;
		this.yearHigh = yearHigh;
		this.name = name;
		this.lastTradePririceOnly = lastTradePririceOnly;
		this.change = change;
		this.daysRange = daysRange;
		this.daysLow = daysLow;
	}
	public String getDaysHigh() {
		return daysHigh;
	}
	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}
	public String getYearLow() {
		return yearLow;
	}
	public void setYearLow(String yearLow) {
		this.yearLow = yearLow;
	}
	public String getYearHigh() {
		return yearHigh;
	}
	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastTradePririceOnly() {
		return lastTradePririceOnly;
	}
	public void setLastTradePririceOnly(String lastTradePririceOnly) {
		this.lastTradePririceOnly = lastTradePririceOnly;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getDaysRange() {
		return daysRange;
	}
	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	} 

}
