package service.ExternalService.Supplier;

import service.ExternalService.Request;

public class SupplyRequest extends Request {
    public SupplyRequest(String name, String address, String city, String country, String zip) {
        super("supply");
        setAdditional_parameters(makeParamsToString(name, address, city, country, zip));
    }



    private String makeParamsToString(String name, String address, String city, String country, String zip)
    {
        String additionalParameters =
                "&name=" + name +
                "&address=" + address +
                "&city=" + city +
                "&country=" + country +
                "&zip=" + zip;
        return additionalParameters;
    }
}
