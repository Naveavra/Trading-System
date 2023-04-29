package utils.marketRelated;

public class Response<T> {
    private T value;
    private String errorTitle;
    private String errorMessage;

    public Response(T res ,String errorTi , String errorMsg){
        value = res;
        errorTitle = errorTi;
        errorMessage = errorMsg;
    }

    public boolean errorOccurred(){
        return errorMessage != null;
    }


    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getValue() {
        return value;
    }
}
