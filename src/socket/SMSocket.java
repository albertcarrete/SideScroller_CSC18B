package socket;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.json.JSONArray;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import overlays.Debugger;
//import appstate.AppState;
import appstate.GameState;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sun.jmx.snmp.Timestamp;

//import layers.GeneralGraphicsLayer;

public class SMSocket {
	/* Connect to URL */
//	private String URL;
	final Socket socket; // Current socket
	private GameState game;
	private long startTime = System.nanoTime(); 
	private long estimatedTime; 
	private long coordinateCounter;
	private long secondCounter;
	private long totalTime;
	private long timeSinceLastMessage;
	private boolean clockSockets;
	final Debugger debugger;
	DateTime dateTime;
	
	private String controllerName;
	
	public SMSocket(Debugger d, String controllerName) throws URISyntaxException{
		// Socket connection
//		socket = IO.socket("http://localhost:8080");
		this.debugger = d;
		this.controllerName = controllerName;
		clockSockets = false;
		secondCounter = 0;
		socket = IO.socket("http://52.24.205.124/");
//		socket = IO.socket("https://msgameserver.herokuapp.com/");
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

      	}).on("user:replications", new Emitter.Listener(){
      		
      		
      		/* Update NetPlayer Location */
      		@Override
      		public void call(Object... args){
      			coordinateCounter++;
      			
//      			System.out.println("Coordinates received:");
          		JSONObject obj = (JSONObject)args[0];
//      			estimatedTime = System.nanoTime() - startTime;
          		
      			
      			if(clockSockets){
      				timeSinceLastMessage = System.nanoTime() - startTime;
      				totalTime += timeSinceLastMessage;
      				
          			if(totalTime > 1000000000){
          				secondCounter++;
          				debugger.sendToScreen("SckCrds", coordinateCounter);
          				debugger.sendToScreen("Time", secondCounter);

          				System.out.println("{MsgPerSec: " + coordinateCounter + ", Time: " + secondCounter + " }" );
          				coordinateCounter = 0;
          				totalTime = 0;
          			}else{
              			
          			}      			
          			
      			}
      			

          		try{
          			/* You absolutely need this conversion for the values to work */
          			String username = (String)obj.get("n");
              		double xpos = ((Number)obj.get("x")).doubleValue();
              		double ypos = ((Number)obj.get("y")).doubleValue();
              		System.out.println("{ " + username + "," + ypos + "," + xpos + " }");
//              		System.out.println("{ " + counter + " }");
              		game.findAndUpdateNetPlayer(username, xpos, ypos);
              		
          		}catch(Exception e){
          			e.printStackTrace();
          		}
          		
      			startTime = System.nanoTime();
          		clockSockets = true;
          		
      		}
      	}).on("user:replication", new Emitter.Listener(){
      		
      		
      		/* Update NetPlayer Location */
      		@Override
      		public void call(Object... args){
      			System.out.println("Socket Coordinates");
      			coordinateCounter++;
      			
//      			System.out.println("Coordinates received:");
      			try{
      				JSONParser parser = new JSONParser();
      				JSONObject obj = (JSONObject)(parser.parse((String) args[0]));      				
//      	      		System.out.println(obj);
      	      		
      				for(Iterator<?> iterator = obj.keySet().iterator(); iterator.hasNext();) {
      				    String key = (String) iterator.next();
//      				    System.out.println(key);
//      				    System.out.println(obj.get(key));
      				    JSONObject subObj = (JSONObject)obj.get(key);
//          			String username = (String)obj.get("n");
      				    double xpos = ((Number)subObj.get("xpos")).doubleValue();
      				    double ypos = ((Number)subObj.get("ypos")).doubleValue();
              		System.out.println("{ " + key + " ,x: " + ypos + ", y: " + xpos + " }");
////              		System.out.println("{ " + counter + " }");
              		game.findAndUpdateNetPlayer(key, xpos, ypos);
      				    
      				    
      				}
      	      
      			}
      			catch(Exception e){
      				e.printStackTrace();
      			}
      	
//
//
//      	        // do something here with the value...
//      	    }
               
//      			if(clockSockets){
//      				timeSinceLastMessage = System.nanoTime() - startTime;
//      				totalTime += timeSinceLastMessage;
//      				
//          			if(totalTime > 1000000000){
//          				secondCounter++;
//          				debugger.sendToScreen("SckCrds", coordinateCounter);
//          				debugger.sendToScreen("Time", secondCounter);
//
//          				System.out.println("{MsgPerSec: " + coordinateCounter + ", Time: " + secondCounter + " }" );
//          				coordinateCounter = 0;
//          				totalTime = 0;
//          			}else{
//              			
//          			}      			
//          			
//      			}
      			

//          		try{
//          			/* You absolutely need this conversion for the values to work */
//          			String username = (String)obj.get("n");
//              		double xpos = ((Number)obj.get("x")).doubleValue();
//              		double ypos = ((Number)obj.get("y")).doubleValue();
//              		System.out.println("{ " + username + "," + ypos + "," + xpos + " }");
////              		System.out.println("{ " + counter + " }");
//              		game.findAndUpdateNetPlayer(username, xpos, ypos);
//              		
//          		}catch(Exception e){
//          			e.printStackTrace();
//          		}
          		
      			startTime = System.nanoTime();
          		clockSockets = true;
          		
      		}
      	}).on("from_server", new Emitter.Listener(){
      		
      		@Override
      		public void call(Object... args){
      			estimatedTime = System.nanoTime() - startTime;
      			System.out.println("Recieving data " + args[0] + " : TLP -  " + estimatedTime);	
      			startTime = System.nanoTime();
//      			System.out.println("Time since last message: " + estimatedTime);
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
//			Timestamp timeStamp = new Timestamp(dateTime.getMillis());
			dateTime = new DateTime();
			obj.put("timestamp", dateTime.getMillis());
//	      	obj.put("timestamp", date.now);
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
