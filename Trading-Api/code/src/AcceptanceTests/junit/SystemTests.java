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

//    private final String MOCK_ES_NAME = "Mock";
    private final String ERROR = "ERROR";
//    private PaymentAdapter paymentMockAdapter;
//    private SupplierAdapter supplyMockAdapter;

    @Override
    @BeforeEach
    public void setUp(){
        super.setUp();
//        paymentMockAdapter = new MockPaymentService();
//        supplyMockAdapter = new MockSupplyService();
//        assertTrue(addExternalPaymentService(mainAdmin.getAdminId(), MOCK_ES_NAME, paymentMockAdapter));
//        assertTrue(removeExternalPaymentService(mainAdmin.getAdminId(), MOCK_ES_NAME));
//        assertTrue(addExternalSupplierService(mainAdmin.getAdminId(), MOCK_ES_NAME, supplyMockAdapter));
//        assertTrue(removeExternalSupplierService(mainAdmin.getAdminId(), MOCK_ES_NAME));
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
    private void assertGoodAddExternalPaymentService(int adminId, String esName)
    {
        assertNotAvailablePaymentService(esName);
        assertPossiblePaymentService(adminId, esName);
        assertTrue(addExternalPaymentService(adminId, esName));
        assertAvailablePaymentService(esName);
    }

    @Test
    private void assertGoodRemoveExternalPaymentService(int adminId, String esName)
    {
        assertAvailablePaymentService(esName);
        assertTrue(removeExternalPaymentService(adminId, esName));
        assertNotAvailablePaymentService(esName);
    }

    @Test
    private void assertBadRemoveExternalPaymentService(int adminId, String esName)
    {
        assertAvailablePaymentService(esName);
        assertFalse(removeExternalPaymentService(adminId, esName));
        assertAvailablePaymentService(esName);
    }

    @Test
    private void assertAvailablePaymentService(String esName)
    {
        List<String> availableServices = getAvailableExternalPaymentService();
        assertNotNull(availableServices);
        assertTrue(availableServices.contains(esName));
    }

    @Test
    private void assertNotAvailablePaymentService(String esName)
    {
        List<String> availableServices = getAvailableExternalPaymentService();
        assertNotNull(availableServices);
        assertFalse(availableServices.contains(esName));
    }

    @Test
    private void assertPossiblePaymentService(int adminId, String esName)
    {
        List<String> possibleServices = getPossibleExternalPaymentService(adminId);
        assertNotNull(possibleServices);
        assertTrue(possibleServices.contains(esName));
    }

    @Test
    private void assertNotPossiblePaymentService(int adminId, String esName)
    {
        List<String> possibleServices = getPossibleExternalPaymentService(adminId);
        assertNotNull(possibleServices);
        assertFalse(possibleServices.contains(esName));
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
    public void testReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalSupplierService(adminId, MOCK_ES_NAME);
        assertTrue(replaceExternalSupplierService(adminId, MOCK_ES_NAME));
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


//-------------------------------------------------------------------------------------
    //Remove external Service - Payment:
    @Test
    public void testReplaceExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        assertTrue(replaceExternalPaymentService(adminId, MOCK_ES_NAME));
    }

    @Test
    public void testReplaceExternalPaymentServiceToNotAvailable(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotAvailablePaymentService(MOCK_ES_NAME);
        assertFalse(replaceExternalPaymentService(adminId, MOCK_ES_NAME));
    }

    @Test
    public void testReplaceExternalPaymentServiceToUnExist(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotPossiblePaymentService(adminId, ERROR);
        assertFalse(replaceExternalPaymentService(adminId, ERROR));
    }

    @Test
    public void testNotAdminReplaceExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertGoodLogin(ui);
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        assertFalse(replaceExternalPaymentService(ui.getUserId(), MOCK_ES_NAME));
    }

    //Remove external Service - Payment:
    @Test
    public void testRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        assertAvailablePaymentService(MOCK_ES_NAME);
        assertTrue(removeExternalPaymentService(adminId, MOCK_ES_NAME));
        assertNotAvailablePaymentService(MOCK_ES_NAME);
    }

    @Test
    public void testRemoveAllExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        List<String> availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        for (int i = 0; i < availablePaymentServices.size() - 1; i++)
        {
            assertGoodRemoveExternalPaymentService(adminId, availablePaymentServices.get(i));
        }
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertEquals(1, availablePaymentServices.size());
        assertBadRemoveExternalPaymentService(adminId, availablePaymentServices.get(0));
    }

    @Test
    public void testRemoveUnExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertPossiblePaymentService(adminId, MOCK_ES_NAME);
        assertNotAvailablePaymentService(MOCK_ES_NAME);
        assertFalse(removeExternalPaymentService(adminId, MOCK_ES_NAME));
    }

    @Test
    public void testNotAdminRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        assertBadRemoveExternalPaymentService(ui.getUserId(), MOCK_ES_NAME);
    }

    //Add external Service - Payment:

    @Test
    public void testAddExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
    }

    @Test
    public void testAddExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertGoodAddExternalPaymentService(adminId, MOCK_ES_NAME);
        assertFalse(addExternalPaymentService(adminId, MOCK_ES_NAME));
        assertAvailablePaymentService(MOCK_ES_NAME);
    }

    @Test
    public void testAddIllegalExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        assertNotPossiblePaymentService(adminId, ERROR);
        assertFalse(addExternalPaymentService(adminId, ERROR));
        assertNotPossiblePaymentService(adminId, ERROR);
        assertNotAvailablePaymentService(ERROR);
    }

    @Test
    public void testNotAdminAddExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        assertPossiblePaymentService(adminId, MOCK_ES_NAME);
        assertNotAvailablePaymentService(MOCK_ES_NAME);
        assertFalse(addExternalPaymentService(ui.getUserId(), MOCK_ES_NAME));
        assertNotAvailablePaymentService(MOCK_ES_NAME);
    }

    //Init System:
    @Test
    public void testInitSystem() {
        assertTrue(true);
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        assertTrue(login(ui.getEmail(), ui.getPassword()) > 0);
    }
}
