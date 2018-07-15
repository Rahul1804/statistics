package com.n26.statistics.model;

import java.time.Instant;

public class Transaction {
	
	private double amount;
	private Instant time;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Instant getTime() {
		return time;
	}
	public void setTime(Instant time) {
		this.time = time;
	}
	
	

}
