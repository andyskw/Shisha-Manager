package hu.harmakhis.shisha.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {

	private static final long serialVersionUID = -8926352849954629453L;
	private List<Player> players;
	private Integer rounds;
	private Integer currentPos;
	private Integer warnTimeOut;
	
	public Session() {
		players = new ArrayList<Player>();
		currentPos = 0;
		rounds = 0;
	}

	public Player next(long timeElapsed) {
		if (currentPos+1 > players.size()) {currentPos = 0; rounds++;}
		Player p = players.get(currentPos);
		p.piped(rounds, timeElapsed);
		players.set(currentPos, p);
		return players.get(currentPos++);
	}
	
	public void addPlayer(String name) {
		Player np = new Player();
		np.setName(name);
		players.add(np);
	}

	public void setWarnTimeOut(Integer warnTimeOut) {
		this.warnTimeOut = warnTimeOut;
	}

	public Integer getWarnTimeOut() {
		return warnTimeOut;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
}