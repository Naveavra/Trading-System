package junit;

import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemTests extends ProjectTest{

    private static final int ERROR = -1;

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

//Remove external Service - Supplier:

    @Test
    public void testReplaceExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status > 0);
        status = this.replaceExternalSupplierService(adminId, 0, 1);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceUnExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalSupplierService(adminId, ERROR, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceExternalSupplierServiceToUnexist(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status > 0);
        status = this.replaceExternalSupplierService(adminId, 0, ERROR);
        assertTrue(status < 0);
    }
    @Test
    public void testNotAdminReplaceExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.removeExternalSupplierService(uid, 0);
        assertTrue(status < 0);
    }

    //Remove external Service - Supplier:

    @Test
    public void testRemoveExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status > 0);
        status = this.removeExternalSupplierService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testRemoveUnExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.removeExternalSupplierService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminRemoveExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.removeExternalSupplierService(uid, 0);
        assertTrue(status < 0);
    }

    //Add external Service - Supplier:

    @Test
    public void testAddExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status > 0);
    }

    @Test
    public void testAddExistExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status > 0);
        status = this.addExternalSupplierService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testAddIllegalExternalSupplierService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalSupplierService(adminId, ERROR);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminAddExternalSupplierService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.addExternalSupplierService(uid, 0);
        assertTrue(status < 0);
    }

    //Remove external Service - Payment:

    @Test
    public void testReplaceExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status > 0);
        status = this.replaceExternalPaymentService(adminId, 0, 1);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceUnExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.replaceExternalPaymentService(adminId, ERROR, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testReplaceExternalPaymentServiceToUnexist(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status > 0);
        status = this.replaceExternalPaymentService(adminId, 0, ERROR);
        assertTrue(status < 0);
    }
    @Test
    public void testNotAdminReplaceExternalPaymentService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.removeExternalPaymentService(uid, 0);
        assertTrue(status < 0);
    }

    //Remove external Service - Payment:

    @Test
    public void testRemoveExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status > 0);
        status = this.removeExternalPaymentService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testRemoveUnExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.removeExternalPaymentService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminRemoveExternalPaymentService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.removeExternalPaymentService(uid, 0);
        assertTrue(status < 0);
    }

    //Add external Service - Payment:

    @Test
    public void testAddExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status > 0);
    }

    @Test
    public void testAddExistExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status > 0);
        status = this.addExternalPaymentService(adminId, 0);
        assertTrue(status < 0);
    }

    @Test
    public void testAddIllegalExternalPaymentService(){
        int adminId = this.mainAdmin.getAdminId();
        int status = this.addExternalPaymentService(adminId, ERROR);
        assertTrue(status < 0);
    }

    @Test
    public void testNotAdminAddExternalPaymentService(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int status = this.addExternalPaymentService(uid, 0);
        assertTrue(status < 0);
    }

    //Init System:
    @Test
    public void testInitSystem() {
        assertTrue(true);
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        assertTrue(login(ui.getEmail(), ui.getPassword()) > 0);
    }
}
