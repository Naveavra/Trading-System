package junit;

import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    private void assertGoodLogin(UserInfo ui)
    {
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
    }

    @Test
    private void assertGoodAddExternalSupplierService(int adminId, String esName)
    {
        assertNotAvailableSupplierService(esName);
        assertPossibleSupplierService(adminId, esName);
        assertTrue(addExternalSupplierService(adminId, esName));
        assertAvailableSupplierService(esName);
    }

    @Test
    private void assertGoodRemoveExternalSupplierService(int adminId, String esName)
    {
        assertAvailableSupplierService(esName);
        assertTrue(removeExternalSupplierService(adminId, esName));
        assertNotAvailableSupplierService(esName);
    }

    @Test
    private void assertBadRemoveExternalSupplierService(int adminId, String esName)
    {
        assertAvailableSupplierService(esName);
        assertFalse(removeExternalSupplierService(adminId, esName));
        assertAvailableSupplierService(esName);
    }

    @Test
    public void testReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertTrue(replaceExternalSupplierService(adminId, externalSupplierServices[1]));
    }

    @Test
    private void assertAvailableSupplierService(String esName)
    {
        List<String> availableServices = getAvailableExternalSupplierService();
        assertNotNull(availableServices);
        assertTrue(availableServices.contains(esName));
    }

    @Test
    private void assertNotAvailableSupplierService(String esName)
    {
        List<String> availableServices = getAvailableExternalSupplierService();
        assertNotNull(availableServices);
        assertFalse(availableServices.contains(esName));
    }

    @Test
    private void assertPossibleSupplierService(int adminId, String esName)
    {
        List<String> possibleServices = getPossibleExternalSupplierService(adminId);
        assertNotNull(possibleServices);
        assertTrue(possibleServices.contains(esName));
    }

    @Test
    private void assertNotPossibleSupplierService(int adminId, String esName)
    {
        List<String> possibleServices = getPossibleExternalSupplierService(adminId);
        assertNotNull(possibleServices);
        assertFalse(possibleServices.contains(esName));
    }

    @Test
    public void testReplaceExternalSupplierServiceToNotAvailable(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotAvailableSupplierService(MOCK_ES_NAME);
        assertFalse(replaceExternalSupplierService(adminId, MOCK_ES_NAME));
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnExist(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotPossibleSupplierService(adminId, ERROR);
        assertFalse(replaceExternalSupplierService(adminId, ERROR));
    }

    @Test
    public void testNotAdminReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertGoodLogin(ui);
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertFalse(replaceExternalSupplierService(ui.getUserId(), MOCK_ES_NAME));
    }

    //Remove external Service - Supplier:
    @Test
    public void testRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertAvailableSupplierService(MOCK_ES_NAME);
        assertTrue(removeExternalSupplierService(adminId, MOCK_ES_NAME));
        assertNotAvailableSupplierService(MOCK_ES_NAME);
    }

    @Test
    public void testRemoveAllExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        List<String> availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        for (int i = 0; i < availableSupplierServices.size() - 1; i++)
        {
            assertGoodRemoveExternalSupplierService(adminId, availableSupplierServices.get(i));
        }
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertEquals(1, availableSupplierServices.size());
        assertBadRemoveExternalSupplierService(adminId, availableSupplierServices.get(0));
    }

    @Test
    public void testRemoveUnExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertPossibleSupplierService(adminId, MOCK_ES_NAME);
        assertNotAvailableSupplierService(MOCK_ES_NAME);
        assertFalse(removeExternalSupplierService(adminId, MOCK_ES_NAME));
    }

    @Test
    public void testNotAdminRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertBadRemoveExternalSupplierService(ui.getUserId(), MOCK_ES_NAME);
    }

    //Add external Service - Supplier:

    @Test
    public void testAddExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
    }

    @Test
    public void testAddExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertFalse(addExternalSupplierService(adminId, MOCK_ES_NAME));
        assertAvailableSupplierService(MOCK_ES_NAME);
    }

    @Test
    public void testAddIllegalExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotPossibleSupplierService(adminId, ERROR);
        assertFalse(addExternalSupplierService(adminId, ERROR));
        assertNotPossibleSupplierService(adminId, ERROR);
        assertNotAvailableSupplierService(ERROR);
    }

    @Test
    public void testNotAdminAddExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertPossibleSupplierService(adminId, MOCK_ES_NAME);
        assertNotAvailableSupplierService(MOCK_ES_NAME);
        assertFalse(addExternalSupplierService(ui.getUserId(), MOCK_ES_NAME));
        assertNotAvailableSupplierService(MOCK_ES_NAME);
    }


    //Remove external Service - Payment:
    //TODO: Continue
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
