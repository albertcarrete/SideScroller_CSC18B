package socket;

import java.net.URISyntaxException;

//import org.json.JSONArray;
import org.json.JSONObject;

//import appstate.AppState;
import appstate.GameState;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

//import layers.GeneralGraphicsLayer;

public class SMSocket {
	/* Connect to URL */
//	private String URL;
	final Socket socket; // Current socket
	private GameState game;
	private long startTime = System.nanoTime(); 
	private long estimatedTime; 

	public SMSocket() throws URISyntaxException{
		
		// Socket connection
//		socket = IO.socket("http://localhost:8080");
		socket = IO.socket("https://msgameserver.herokuapp.com/");
		// When the socket connects
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {  
				System.out.println("socket connected");
      	  	}
      	  

      	}).on("user:joined", new Emitter.Listener(){
      		
      		@Override
      		public void call(Object... args){
      			System.out.println(args[0] + " has joined!");
      			try{
              		JSONObject obj = (JSONObject)args[0];
              		String username = (String)obj.get("username");      				
              		game.addNetworkedPlayer(username);

      			}catch(Exception e){
      				
      			}
      		}
      		
      	}).on("event", new Emitter.Listener() {

      	  @Override
      	  public void call(Object... args) {   
      		  System.out.println("Message was received");
      		  System.out.println(args[0]);
      		  updateFrameMessages(args[0].toString());
      		  
//      		JSONObject obj = (JSONObject)args[0];
      		  
      	  }

      	}).on("user:replication", new Emitter.Listener(){
      		
      		
      		/* Update NetPlayer Location */
      		@Override
      		public void call(Object... args){
      			System.out.println("Coordinates received:");
          		JSONObject obj = (JSONObject)args[0];
          		          		
          		try{
          			/* You absolutely need this conversion for the values to work */
          			String username = (String)obj.get("n");
              		double xpos = ((Number)obj.get("x")).doubleValue();
              		double ypos = ((Number)obj.get("y")).doubleValue();
              		System.out.println("{ " + username + "," + ypos + "," + xpos + " }");
              		game.findAndUpdateNetPlayer(username, xpos, ypos);
          		}catch(Exception e){
          			
          		}
          		
      		}
      	}).on("from_server", new Emitter.Listener(){
      		
      		@Override
      		public void call(Object... args){
      			estimatedTime = System.nanoTime() - startTime;
      			System.out.println("Recieving data " + args[0] + " : TLP -  " + estimatedTime);	
      			startTime = System.nanoTime();
      		}
      	});
    	socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
        	  @Override
        	  public void call(Object... args) {
        		  
        		System.out.println("disconnected");
//        	    socket.emit("message", "user connected");
        	  }

        	});
    	socket.connect();
		socket.open();
		
	}
	public void socketCapture(){
		socket.on("event", new Emitter.Listener() {

	      	  @Override
	      	  public void call(Object... args) {
	      		  System.out.println(args[0]);
//	      		JSONObject obj = (JSONObject)args[0];
	      		  
	      	  }
		});
	}
	public void sendMessage(String message){
		System.out.println("Attempting to send socket message: " + message);
		socket.emit("message",message);
	}
	public void sendPlayerCoordinates(String username,double x, double y){
	      JSONObject obj = new JSONObject();
	      try{
	    	obj.put("username", username);
	      	obj.put("xpos", x);
	      	obj.put("ypos", y);
	      }catch(Exception e){
	      	e.printStackTrace();
	      }
	      
		socket.emit("coordinates", obj);
	}
	public void userJoin(String username){
		socket.emit("user:join",username);
	}
	public void setGame(GameState game){
		System.out.println("Set frame in socket");
		this.game = game;
	}
	public void updateFrameMessages(String message){
//		frame.updateMessages(message);
	}
}
