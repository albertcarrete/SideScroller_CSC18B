package socket;

import appstate.CharacterSelectState;
import appstate.GameState;
//import layers.GeneralGraphicsLayer;


public class SocketController {
	private SMSocket socket;
	private GameState game;
	private CharacterSelectState characterSelect;
	
	public SocketController(SMSocket socket, GameState game){
		this.socket = socket;
		this.game = game;
	}
	public SocketController(SMSocket socket, CharacterSelectState characterSelect){
		this.socket = socket;
		this.characterSelect = characterSelect;
	}
	
	// TODO Combine these two functions together
	public void linkUp(){
		socket.setGame(game);
//		frame.setSocket(socket);
	}
	public void linkUpCSS(){
		socket.setCharacterSelect(characterSelect);
//		frame.setSocket(socket);
	}
}
