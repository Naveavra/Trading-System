package service.ExternalService;

public class Request {
    private final String action_type;
    private String additional_parameters;

    public Request(String actionType) {
        action_type = actionType;
        additional_parameters = null;
    }

    protected Request(String actionType, String additionalParams) {
        action_type = actionType;
        additional_parameters = additionalParams;
    }

    public String toString()
    {
        String req = "action_type=" + action_type;
        if (additional_parameters != null)
        {
            req += additional_parameters;
        }
        return req;
    }

    public byte[] toBytes()
    {
        return toString().getBytes();
    }

    public void setAdditional_parameters(String additional_parameters) {
        this.additional_parameters = additional_parameters;
    }

    public boolean isHandshake() {
        return action_type.equals("handshake");
    }
}
