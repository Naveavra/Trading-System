package server;

import market.Market;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectedThread  extends Thread{
    private final ConcurrentHashMap<Integer, Boolean> connected;
    private API api = Server.api;

    public ConnectedThread(ConcurrentHashMap<Integer, Boolean> connected) {
        this.connected = connected;
    }

    @Override
    public void run() {
        while (true) {
            for (int key : connected.keySet()) {
                if (connected.get(key)) {
                    connected.put(key, false);
                    System.out.println(key + " becomes false");
                } else {
                    connected.remove(key);
                    if(key%2==0){
                        api.exitGuest(key);
                    }
                    else if (key % 2 == 1){
                        //TODO: logout
                        api.logout(key);
                    }
                    else
                        api.adminLogout(key);
                    System.out.println(key + " removed !!!!!");
                }
            }

            try {
                Thread.sleep(20000); // sleep for 15 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}