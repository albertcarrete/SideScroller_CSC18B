package socket;

import appstate.AppState;
import appstate.GameState;
import layers.GeneralGraphicsLayer;


public class SocketController {
	private SMSocket socket;
	private GameState game;
	
	public SocketController(SMSocket socket, GameState game){
		this.socket = socket;
		this.game = game;
	}
	
	public void linkUp(){
		socket.setGame(game);
//		frame.setSocket(socket);
	}
}
