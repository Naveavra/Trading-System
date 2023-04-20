package utils;

public class Response<T> {
    private String errorMessage;

    private T value;
    public Response(T res){value = res;}

    public Response(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public boolean errorOccurred(){
        return errorMessage != null;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public T getValue() {
        return value;
    }
}
