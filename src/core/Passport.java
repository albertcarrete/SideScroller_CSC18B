package core;

public class Passport {
	
	
	// Player Credentials
	private String username;
	private String gameId;
	private int position;
	
	public Passport(){
		username = "";
		gameId = "";
	}
	
	public void setPosition(int p){
		this.position = p;
	}
	public void setUsername(String u){
		this.username = u;
	}
	public void setGameId(String i){
		this.gameId = i;
	}
	public String getUsername(){
		return this.username;
	}
	public String getGameId(){
		return this.gameId;
	}
	public int getPosition(){
		return this.position;
	}
	
}
