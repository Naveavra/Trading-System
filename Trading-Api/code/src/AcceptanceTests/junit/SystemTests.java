package junit;

import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ExternalService.Payment.MockPaymentService;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.MockSupplyService;
import service.ExternalService.Supplier.SupplierAdapter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTests extends ProjectTest{

    private final String MOCK_ES_NAME = "Mock";
    private final String ERROR = "ERROR";
    private PaymentAdapter paymentMockAdapter;
    private SupplierAdapter supplyMockAdapter;
    private String[] externalPaymentServices = {"WSEP", "Mock"};
    private String[] externalSupplierServices = {"WSEP", "Mock"};
    @Override
    @BeforeEach
    public void setUp(){
        super.setUp();
        paymentMockAdapter = new MockPaymentService();
        supplyMockAdapter = new MockSupplyService();
        assertTrue(addExternalPaymentService(mainAdmin.getAdminId(), MOCK_ES_NAME, paymentMockAdapter));
        assertTrue(removeExternalPaymentService(mainAdmin.getAdminId(), MOCK_ES_NAME));
        assertTrue(addExternalSupplierService(mainAdmin.getAdminId(), MOCK_ES_NAME, supplyMockAdapter));
        assertTrue(removeExternalSupplierService(mainAdmin.getAdminId(), MOCK_ES_NAME));
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }

    /**
     *      System Tests:
     * -----------------------
     * Init System - V
     * Add external Service - Payment - V
     * Remove external Service - Payment - V
     * Replace external Service - Payment - V
     * Add external Service - Supplier - V
     * Remove external Service - Supplier - V
     * Replace external Service - Supplier -V
     * External System Payment - In Guest & Member
     * External System Supplies Order - In Guest & Member
     * Real Time Messages
     * Awaiting Messages
     **/

    @Test
    private void assertGoodExternalSupplierService(int adminId, String esName)
    {
        List<String> availableServices = getAvailableExternalSupplierService();
        assertNotNull(availableServices);
        assertFalse(availableServices.contains(esName));
        assertTrue(addExternalSupplierService(adminId, esName));
        availableServices = getAvailableExternalSupplierService();
        assertNotNull(availableServices);
        assertTrue(availableServices.contains(MOCK_ES_NAME));
    }

    @Test
    public void testReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodExternalSupplierService(adminId, MOCK_ES_NAME);
        assertTrue(replaceExternalSupplierService(adminId, externalSupplierServices[1]));
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnAvailable(){
        int adminId = this.mainAdmin.getAdminId();
        assertFalse(replaceExternalSupplierService(adminId, externalSupplierServices[1]));
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnExist(){
        int adminId = this.mainAdmin.getAdminId();
        assertFalse(replaceExternalSupplierService(adminId, "ERROR"));
    }

    @Test
    public void testNotAdminReplaceExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(addExternalSupplierService(adminId, externalSupplierServices[1]));
        assertFalse(replaceExternalSupplierService(uid, externalSupplierServices[1]));
    }

    //Remove external Service - Supplier:

    @Test
    public void testRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
        assertTrue(addExternalSupplierService(adminId, externalSupplierServices[1]));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        assertTrue(this.removeExternalSupplierService(adminId, externalSupplierServices[1]));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
    }

    @Test
    public void testRemoveAllExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[0]));
        assertEquals(1, availableSupplierServices.size());
        assertFalse(this.removeExternalSupplierService(adminId, externalSupplierServices[0]));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[0]));
    }

    @Test
    public void testRemoveUnExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
        assertFalse(this.removeExternalSupplierService(adminId, externalSupplierServices[1]));
    }

    @Test
    public void testNotAdminRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        assertTrue(addExternalSupplierService(adminId, externalSupplierServices[1]));
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        assertFalse(this.removeExternalSupplierService(uid, externalSupplierServices[1]));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
    }

    //Add external Service - Supplier:

    @Test
    public void testAddExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(addExternalSupplierService(adminId, externalSupplierServices[1]));
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
    }

    @Test
    public void testAddExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(addExternalSupplierService(adminId, externalSupplierServices[1]));
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        assertFalse(addExternalSupplierService(adminId, externalSupplierServices[1]));
    }

    @Test
    public void testAddIllegalExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(ERROR));
        assertFalse(this.addExternalSupplierService(adminId, ERROR));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(ERROR));
    }

    @Test
    public void testNotAdminAddExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
        assertFalse(addExternalSupplierService(uid, externalSupplierServices[1]));
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
    }


    //Remove external Service - Payment:

    @Test
    public void testReplaceExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String> paymentES = this.getPaymentServicesPossibleOptions(adminId);
        assertNotNull(paymentES);
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        int status = this.replaceExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
    }

    @Test
    public void testReplaceExternalPaymentServiceToUnAvailable(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceExternalPaymentServiceToUnExist(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalPaymentService(adminId, "ERROR");
        assertTrue(status < 0);
    }
    @Test
    public void testNotAdminReplaceExternalPaymentService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        int status = this.replaceExternalPaymentService(uid, externalPaymentServices[1]);
        assertTrue(status < 0);
    }

    //Remove external Service - Payment:

    @Test
    public void testRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(externalPaymentServices[1]));
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        assertTrue(this.removeExternalPaymentService(adminId, externalPaymentServices[1]));
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(externalPaymentServices[1]));
    }

    @Test
    public void testRemoveAllExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[0]));
        assertEquals(1, availablePaymentServices.size());
        assertFalse(this.removeExternalPaymentService(adminId, externalPaymentServices[0]));
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[0]));
    }

    @Test
    public void testRemoveUnExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(externalPaymentServices[1]));
        assertTrue(this.removeExternalPaymentService(adminId, externalPaymentServices[1]));
    }

    @Test
    public void testNotAdminRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        assertFalse(this.removeExternalPaymentService(uid, externalPaymentServices[1]));
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
    }

    //Add external Service - Payment:

    @Test
    public void testAddExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        List<String> availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
    }

    @Test
    public void testAddExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertTrue(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        assertFalse(this.addExternalPaymentService(adminId, externalPaymentServices[1]));
    }

    @Test
    public void testAddIllegalExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertFalse(availablePaymentServices.contains(ERROR));
        assertFalse(this.addExternalPaymentService(adminId, ERROR));
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertFalse(availablePaymentServices.contains(ERROR));
    }

    @Test
    public void testNotAdminAddExternalPaymentService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(externalPaymentServices[1]));
        assertFalse(this.addExternalPaymentService(uid, externalPaymentServices[1]));
//        assertTrue(status < 0);
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertFalse(availablePaymentServices.contains(externalPaymentServices[1]));
    }

    //Init System:
    @Test
    public void testInitSystem() {
        assertTrue(true);
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        assertTrue(login(ui.getEmail(), ui.getPassword()) > 0);
    }
}
