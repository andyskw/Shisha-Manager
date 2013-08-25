package com.vilagmegvaltas.shisha.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {

	private static final long serialVersionUID = 5242718581320769055L;

	private String name;
	private long sumPipeTime;
	private long rounds;
	private Map<Integer, Long> history;
	
	public Player() {
		sumPipeTime = 0;
		rounds = 0;
		history = new HashMap<Integer, Long>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getSumPipeTime() {
		return sumPipeTime;
	}
	public long getRounds() {
		return rounds;
	}

	public void piped(int round, long thismuch) {
		// :D
		history.put(round, thismuch);
		sumPipeTime += thismuch;
		rounds++;
	}
	
	public void resetPlayerStats() {
		sumPipeTime = 0;
		rounds = 0;
		history.clear();
	}
	
	public Map<Integer, Long> getHistory() {
		return history;
	}
}