package junit;

import data.AdminInfo;
import data.PurchaseInfo;
import data.StoreInfo;
import data.UserInfo;
import junit.ProjectTest;
import market.Admin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest extends ProjectTest {
    private static final int ERROR = -1;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @AfterEach
    public void tearDown() {
        super.tearDown();
    }


    /**     Admin Functionality
     * 1. Get all admins in system - V
     * 2. Get all Stores - V & TODOs
     * 3. Add Admin - V
     * 4. Remove Admin - V
     * Nice To Test:
     * 1. Remove User
     * 2. Remove Store
     * 3. Remove Review
     *
     */

    //Remove Admin:
    @Test
    public void testRemoveAdmin() {
        int adminId = this.mainAdmin.getAdminId();
        List<AdminInfo> adminInfos = this.getAllAdmins(adminId);
        assertNotNull(adminInfos);
        AdminInfo ad2 = admins_dict.get(admins[0][ADMIN_USER]);
        ad2.setAdminId(adminLogin(ad2.getEmail(), ad2.getPassword()));
        assertTrue(ad2.getAdminId() < 0);
        int status = this.removeAdmin(ad2.getAdminId());
        assertTrue(status > 0);
        int pre = adminInfos.size();
        adminInfos = this.getAllAdmins(adminId);
        assertNotNull(adminInfos);
        assertTrue(adminInfos.size() > 0);
        assertEquals(pre - 1, adminInfos.size());
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }


    @Test
    public void testNotAdminTryRemoveAdmin() {
        UserInfo uid = users_dict.get(users[0][USER_EMAIL]);
        int status = this.removeAdmin(uid.getUserId());
        assertTrue(status < 0);
    }

    @Test
    public void testTryRemoveAllAdmins() {
        int adminId = this.mainAdmin.getAdminId();
        int status = 0;
        for(Map.Entry<String, AdminInfo> admin: admins_dict.entrySet())
        {
            AdminInfo ai = admin.getValue();
            if(ai.getAdminId() != mainAdmin.getAdminId())
            {
                ai.setAdminId(adminLogin(ai.getEmail(), ai.getPassword()));
                assertTrue(ai.getAdminId() < 0);
                status = this.removeAdmin(ai.getAdminId());
                assertTrue(status > 0);
            }
        }
        status = this.removeAdmin(adminId);
        assertTrue(status < 0);
    }


    @Test
    public void testTryRemoveAllAdminsAndLogut() {
        int adminId = this.mainAdmin.getAdminId();
        int status = 0;
        for(Map.Entry<String, AdminInfo> admin: admins_dict.entrySet())
        {
            AdminInfo ai = admin.getValue();
            if(ai.getAdminId() != mainAdmin.getAdminId())
            {
                ai.setAdminId(adminLogin(ai.getEmail(), ai.getPassword()));
                assertTrue(ai.getAdminId() < 0);
                status = this.removeAdmin(ai.getAdminId());
                assertTrue(status > 0);
            }
        }
        status = this.removeAdmin(adminId);
        assertTrue(status < 0);
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }

    //Add Admin:
    @Test
    public void testAddAdmin() {
        int adminId = this.mainAdmin.getAdminId();
        List<AdminInfo> adminInfos = this.getAllAdmins(adminId);
        assertNotNull(adminInfos);
        int status = this.addAdmin(adminId, "admin2@gmail.com", "admin2");
        assertTrue(status > 0);
        int pre = adminInfos.size();
        adminInfos = this.getAllAdmins(adminId);
        assertNotNull(adminInfos);
        assertEquals(pre + 1, adminInfos.size());
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }


    @Test
    public void testNotAdminTryAddAdmin() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int status = login(ui.getEmail(), ui.getPassword());
        assertTrue(status > 0);
        ui.setUserId(status);
        status = this.addAdmin(ui.getUserId(), "admin2@gmail.com", "admin2");
        assertTrue(status < 0);
        status = logout(ui.getUserId());
        assertTrue(status > 0);
    }

//    Get all Stores:

    @Test
    public void testGetAllStoresInSystem() {
        int adminId = this.mainAdmin.getAdminId();
        List<StoreInfo> allStores = this.getAllStores();
        assertNotNull(allStores);
        assertEquals(stores.size(), allStores.size());
        int status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }

    @Test
    public void testGetAllStoresInSystemAfterAddStore() {
        int adminId = this.mainAdmin.getAdminId();
        int status = 1;
        List<StoreInfo> allStores = this.getAllStores();
        assertNotNull(allStores);
        assertEquals(stores.size(), allStores.size());
        UserInfo ui = users_dict.get(users[0][0]);
        StoreInfo newStore0 = new StoreInfo(ui.getUserId(), "iphone", "Phone Accessory Store", "img");
        status = login(ui.getEmail(), ui.getPassword());
        assertTrue(status > 0);
        ui.setUserId(status);
        newStore0.setStoreId(createStore(newStore0));
        assertTrue(newStore0.getStoreId() >= 0);
        allStores = this.getAllStores();
        assertNotNull(allStores);
        assertEquals(stores.size() + 1, allStores.size());
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }

    @Test
    public void testGetAllStoresInSystemAfterRemoveStore() {
        int adminId = this.mainAdmin.getAdminId();
        List<StoreInfo> allStores = this.getAllStores();
        assertNotNull(allStores);
        assertEquals(stores.size(), allStores.size());
        UserInfo ui = users_dict.get(users[0][0]);
        int status = login(ui.getEmail(), ui.getPassword());
        assertTrue(status > 0);
        assertTrue(closeStore(ui.getUserId(), stores.get(0).getStoreId()) > 0);
        allStores = this.getAllStores();
        assertNotNull(allStores);
        assertEquals(stores.size() -1, allStores.size());
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }


//    Get all admins in system:

    @Test
    public void testGetAllAdminsInSystem() {
        int adminId = this.mainAdmin.getAdminId();
        List<AdminInfo> allAdmins = this.getAllAdmins(adminId);
        assertNotNull(allAdmins);
        assertEquals(admins_dict.size(), allAdmins.size());
        int status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }

    @Test
    public void testGetAllAdminsInSystemAfterAddAdmin() {
        int adminId = this.mainAdmin.getAdminId();
        List<AdminInfo> allAdmins = this.getAllAdmins(adminId);
        assertNotNull(allAdmins);
        assertEquals(admins_dict.size(), allAdmins.size());
        int status = this.addAdmin(adminId, "admin2", "admin2");
        assertTrue(status > 0);
        allAdmins = this.getAllAdmins(adminId);
        assertNotNull(allAdmins);
        assertEquals(admins_dict.size() + 1, allAdmins.size());
        status = adminLogout(mainAdmin.getAdminId());
        assertTrue(status > 0);
    }

    @Test
    public void testGetAllAdminsInSystemAfterRemoveAdmin() {
        int adminId = this.mainAdmin.getAdminId();
        List<AdminInfo> allAdmins = this.getAllAdmins(adminId);
        assertNotNull(allAdmins);
        assertEquals(admins_dict.size(), allAdmins.size());
        AdminInfo ai = admins_dict.get("admin1@gmail.com");
        ai.setAdminId(adminLogin(ai.getEmail(), ai.getPassword()));
        assertTrue(ai.getAdminId() < 0);
        int status = this.removeAdmin(ai.getAdminId());
        assertTrue(status > 0);
        allAdmins = this.getAllAdmins(adminId);
        assertNotNull(allAdmins);
        assertNotEquals(admins_dict.size(), allAdmins.size());
    }

    @Test
    public void testNotAdminTryGetAllAdminsInSystem() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int status = login(ui.getEmail(), ui.getPassword());
        assertTrue(status > 0);
        List<AdminInfo> allAdmins = this.getAllAdmins(ui.getUserId());
        assertNull(allAdmins);
        status = logout(ui.getUserId());
        assertTrue(status > 0);
    }
}
