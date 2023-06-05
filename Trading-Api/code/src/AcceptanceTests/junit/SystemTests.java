package junit;

import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTests extends ProjectTest{

    private final String ERROR = "ERROR";
    private String[] externalPaymentServices = {"Google Pay", "Apple Pay"};
    private String[] externalSupplierServices = {"DHL", "UPS"};
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
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
    public void testReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        status = this.replaceExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnAvailable(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnExist(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalSupplierService(adminId, "ERROR");
        assertTrue(status < 0);
    }
    @Test
    public void testNotAdminReplaceExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        status = this.replaceExternalSupplierService(uid, externalSupplierServices[1]);
        assertTrue(status < 0);
    }

    //Remove external Service - Supplier:

    @Test
    public void testRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        status = this.removeExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
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
        int status = this.removeExternalSupplierService(adminId, externalSupplierServices[0]);
        assertTrue(status < 0);
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
        int status = this.removeExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        status = this.removeExternalSupplierService(uid, externalSupplierServices[1]);
        assertTrue(status < 0);
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
    }

    //Add external Service - Supplier:

    @Test
    public void testAddExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
    }

    @Test
    public void testAddExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status > 0);
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(availableSupplierServices.contains(externalSupplierServices[1]));
        status = this.addExternalSupplierService(adminId, externalSupplierServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testAddIllegalExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(ERROR));
        int status = this.addExternalSupplierService(adminId, ERROR);
        assertTrue(status < 0);
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
        int status = this.addExternalSupplierService(uid, externalSupplierServices[1]);
        assertTrue(status < 0);
        availableSupplierServices = this.getAvailableExternalSupplierService();
        assertNotNull(availableSupplierServices);
        assertTrue(!availableSupplierServices.contains(externalSupplierServices[1]));
    }


    //Remove external Service - Payment:

    @Test
    public void testReplaceExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        status = this.replaceExternalPaymentService(adminId, externalPaymentServices[1]);
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
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        status = this.replaceExternalPaymentService(uid, externalPaymentServices[1]);
        assertTrue(status < 0);
    }

    //Remove external Service - Payment:

    @Test
    public void testRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(externalPaymentServices[1]));
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        status = this.removeExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
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
        int status = this.removeExternalPaymentService(adminId, externalPaymentServices[0]);
        assertTrue(status < 0);
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
        int status = this.removeExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        status = this.removeExternalPaymentService(uid, externalPaymentServices[1]);
        assertTrue(status < 0);
        availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
    }

    //Add external Service - Payment:

    @Test
    public void testAddExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        List<String> availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
    }

    @Test
    public void testAddExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status > 0);
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertTrue(availablePaymentServices.contains(externalPaymentServices[1]));
        status = this.addExternalPaymentService(adminId, externalPaymentServices[1]);
        assertTrue(status < 0);
    }

    @Test
    public void testAddIllegalExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        List<String>  availablePaymentServices = this.getAvailableExternalPaymentService();
        assertNotNull(availablePaymentServices);
        assertTrue(!availablePaymentServices.contains(ERROR));
        int status = this.addExternalPaymentService(adminId, ERROR);
        assertTrue(status < 0);
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
        int status = this.addExternalPaymentService(uid, externalPaymentServices[1]);
        assertTrue(status < 0);
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
