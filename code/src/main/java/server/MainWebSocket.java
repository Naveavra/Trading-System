package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class MainWebSocket {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        System.out.println("logging out");
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println(message);
        JSONObject j = new JSONObject(message);
        String email = j.get("email").toString();
        JSONObject ans = new JSONObject();
        ans.put("errorOccurred", "false");
        ans.put("msg", "null");

        JSONObject token= new JSONObject();
        token.put("token", "aaaa");
        token.put("userId","1");
        token.put("userName",email);
        ans.put("token", token.toString());
        session.getRemote().sendString(ans.toString());
    }
}
