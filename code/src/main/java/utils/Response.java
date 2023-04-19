package utils;

public class Response {
    private String errorMessage;
    public Response(){}

    public Response(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public boolean errorOccurred(){
        return errorMessage != null;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

}
