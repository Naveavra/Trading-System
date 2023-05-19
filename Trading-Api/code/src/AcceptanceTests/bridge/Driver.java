package bridge;

public abstract class Driver {

    public static Bridge getBridge() {
        ProxyBridge bridge = new ProxyBridge();

        // Uncomment this line
        //bridge.setRealBridge(new BridgeImplement());

        return bridge;
    }
}
