package market;

import domain.store.storeManagement.Store;
import utils.LoginInformation;
import utils.marketRelated.Response;

import java.util.HashMap;
import java.util.List;

public interface MarketInterface {
    //assumption :
        // guest will be recognized by id
        // member will be recognized by email & id
        //for each function of guest there will be 2 function , owns get an id and the other gets userName
    //guest methods
    public Response enterGuest();
    public Response exitGuest(int guestId);
    public Response register(String email ,String pass ,String birthday );
    public Response addProductToCart(int userId,int storeId ,int productId, int quantity);
    public Response removeProductFromCart(int userId,  int storeId, int productId);
    public Response changeQuantityInCart(int userId, int storeId, int productId, int change);
    public Response getCart(int id);
    Response<String> removeCart(int userId);
    public Response makePurchase(int userId , String accountNumber);
    public Response getStoreDescription(int storeId);


    //TODO: will change when miki adds all changes
    public Response showFilterOptions();
    public Response filterBy(HashMap<String,String> filterOptions);


    //member methods
    public Response login(String email , String pass);
    Response<LoginInformation> getMember(int userId, String token);
    public Response checkSecurityQuestions(int userId, String token, List<String> answers);
    public Response addSecurityQuestion(int userId, String token, String question, String answer);
    public Response changeAnswerForLoginQuestion(int userId, String token, String question, String answer);
    public Response removeSecurityQuestion(int userId, String token, String question);
    public Response displayNotifications(int userId, String token);
    public Response logout(int userId);
    public Response changePassword(int userId, String token, String oldPass, String newPass);

    public Response changeName(int userId, String token, String newUserName);
    public Response changeEmail(int userId, String token, String newEmail);

    public Response openStore(int userId,String token, String storeDescription);
    public Response openStore(int userId, String token, String storeName, String des, String img);
    public Response getMemberInformation(int userId, String token);
    public Response getUserPurchaseHistory(int userId, String token, int buyerId);
    public Response writeReviewToStore(int userId, String token, int orderId, int storeId, String content, int grading);
    public Response writeReviewToProduct(int userId, String token, int orderId, int storeId,int productId, String content, int grading);
    public Response checkReviews(int userId, String token, int storeId);
    public Response getProductInformation(int storeId , int productId);
    public Response getStoreInformation(int storeId);
    Response<Store> getStore(int userId, String token, int storeId);
    public Response getStoreProducts(int storeId);

    public Response sendQuestion(int userId, String token, int storeId, String msg);
    public Response sendComplaint(int userId, String token, int orderId, int storeId, String msg);

    // manager methods
    public Response appointManager(int userId, String token, int managerIdToAppoint, int storeId);
    public Response appointManager(int userId, String token, String managerToAppoint, int storeId);
    public Response changeStoreDescription(int userId, String token, int storeId, String description);
    Response<String> changeStoreImg(int userId, String token, int storeId, String img);
    Response<String> changeStoreName(int userId, String token, int storeId, String name);
    public Response changePurchasePolicy(int userId, String token, int storeId, String policy);
    public Response addDiscountPolicy(int userId, String token, int storeId, String policy);
    public Response addPurchaseConstraint(int userId, String token, int storeId, String constraint);
    public Response fireManager(int userId, String token, int managerToFire, int storeId);
    public Response checkWorkerStatus(int userId, String token, int workerId, int storeId);
    public Response checkWorkersStatus(int userId, String token, int storeId);
    public Response viewQuestions(int userId, String token, int storeId);
    public Response answerQuestion(int userId, String token, int storeId, int questionId, String answer);
    public Response seeStoreHistory(int userId, String token, int storeId);
    public Response addProduct(int useIid, String token, int storeId,List<String> categories, String name , String description , int price , int quantity);

    public Response<Integer> addProduct(int userId, String token, int storeId, List<String> categories, String name, String description,
                                        int price, int quantity, String img);
    public Response deleteProduct(int userId, String token, int storeId, int productId);
    //store owner methods
    public Response appointOwner(int userId , String token,int ownerId, int storeId);

    //TODO: need to use supplierProxy for changing the quantity
    Response<String> updateProduct(int userId, String token, int storeId, int productId, List<String> categories, String name, String description,
                                   int price, int quantity, String img);

    public Response<String> appointOwner(int userId, String token, String owner, int storeId);
    public Response fireOwner(int userId , String token, int ownerId, int storeId);
    public Response addManagerPermission (int ownerId, String token, int userId,int storeId, int permissionsId);

    Response<String> addManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds);

    public Response removeManagerPermission (int ownerId, String token, int userId, int storeId, int permissionsId);

    Response<String> removeManagerPermissions(int ownerId, String token, int userId, int storeId, List<Integer> permissionsIds);

    public Response getAppointments(int userId, String token, int storeId);

    //store creator methods
    public Response closeStore(int userId, String token, int storeId);
    public Response reopenStore(int userId, String token, int storeId);

    public Response closeStorePermanently(int adminId, String token, int storeId);

    //store methods
    //todo: decide if getStore will bring every thing together , products , orders , ..statistics
    //public Response getStore(int storeId);
    public Response getProducts(int storeId);
    public Response getProducts();

    // admin methods
    public Response adminLogin(String email, String pass);
    public Response adminLogout(int adminId);
    public Response getAdmins(int adminId, String token);
    public Response addAdmin(int userId, String token, String email , String pass);
    public Response removeAdmin(int adminId, String token);
    public Response getUsersPurchaseHistory(int buyerId, String token);
    public Response answerComplaint(int adminId, String token, int complaintId, String ans);
    public Response cancelMembership(int adminId, String token, int userToRemove);
    public Response watchEventLog(int adminId, String token);
    public Response watchFailLog(int adminId, String token);

    public Response watchMarketStatus(int adminId, String token);

    public Response setPaymentService(int adminId, String token, String paymentService);
    public Response getPaymentServicePossible(int adminId, String token);
    public Response getPaymentServiceAvailable(int userId);
    public Response addPaymentService(int adminId, String token, String paymentService);
    public Response removePaymentService(int adminId, String token, String paymentService);

    public Response setSupplierService(int adminId, String token, String supplierService);
    public Response getSupplierServicePossible(int adminId, String token);
    public Response getSupplierServiceAvailable(int userId);
    public Response addSupplierService(int adminId, String token, String supplierService);
    public Response removeSupplierService(int adminId, String token, String supplierService);

}
