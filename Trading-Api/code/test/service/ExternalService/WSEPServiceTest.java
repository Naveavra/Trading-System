package service.ExternalService;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WSEPServiceTest {

    private WSEPService wsepService;
    private JSONObject paymentContent;
    private JSONObject supplierContent;
    private final String TRANSACTION_ID = "10000";

    private JSONObject createPaymentJson()
    {
        JSONObject payment = new JSONObject();
        payment.put("payment_service", "WSEP");
        payment.put("cardNumber", "000000000");
        payment.put("month", "01");
        payment.put("year", "30");
        payment.put("holder", "Israel Visceral");
        payment.put("ccv", "984");//984
        payment.put("id", "123456789");
        return payment;
    }

    private JSONObject createSupplierJson()
    {
        JSONObject supplier = new JSONObject();
        supplier.put("supply_service", "WSEP");
        supplier.put("name", "Israel Visceral");
        supplier.put("address", "Reger 17");
        supplier.put("city", "Beer Sheva");
        supplier.put("country", "Israel");
        supplier.put("zip", "984");
        return supplier;
    }

    @BeforeEach
    void setUp() {
        try {
            wsepService = new WSEPService();
        } catch (Exception e) {
            System.out.println("Cant connect to external service");
        }
        paymentContent = createPaymentJson();
        supplierContent = createSupplierJson();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void makePurchase() {
        try {
            wsepService.makePurchase(paymentContent, 50);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        assert true;
    }

    @Test
    void cancelPurchase() {
        try {
            wsepService.cancelPurchase(TRANSACTION_ID);
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    @Test
    void orderSupplies() {
        try {
            wsepService.orderSupplies(supplierContent, null);
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(true);
    }

    @Test
    void cancelSupply() {
        try {
            wsepService.cancelSupply(TRANSACTION_ID);
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(true);
    }
}