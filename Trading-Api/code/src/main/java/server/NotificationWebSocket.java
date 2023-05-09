package server;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Session;
@WebSocket

public class NotificationWebSocket {
    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Server.nextUser++;
        Server.userMap.put(user, username);
        System.out.println("user" + Server.nextUser + "connect");
       // Server.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = Server.userMap.get(user);
        Server.userMap.remove(user);
        System.out.println("user" +username + "close");
      //  Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("hi" + message);
    }
}
