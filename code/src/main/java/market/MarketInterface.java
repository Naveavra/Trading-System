package market;

import org.json.simple.JSONArray;
import utils.Message;
import utils.Response;

import java.util.List;

//TODO: can remove admins but at least one has to be in the system.
//TODO: need to add those different functions: gettingInformationOnStore(int storeId), searchProduct(String name, ...),
//TODO: getProduct(int storeId, int productId), checkProductAvailability(int storeId, int productId, int quantity),
//TODO: addProduct(int userId, int storeId, int productId, int quantity), changeQuantity(int userId, int storeId, int productId, int quantity),
//TODO: gettingInformationOnProduct(int storeId, int productId), removeFromCart(int userId, int storeId, int productId),
//TODO: getCartContent(int userId), purchaseCart(int userId), openStore(int userId),
public interface MarketInterface {
    //assumption :
        // guest will be recognized by id
        // member will be recognized by email & id
        //for each function of guest there will be 2 function , owns get an id and the other gets userName
    //guest methods
    public Response register(String email ,String pass ,String birthday );
    public Response addProductToCart(int userId,int storeId ,int productId, int quantity);
    public Response addProductToCart(String name, int storeId, int productId, int quantity);

    public Response removeProductFromCart(int userId,  int storeId, int productId);
    public Response removeProductFromCart(String userName,  int storeId, int productId);
    public Response changeQuantityInCart(int userId, int storeId, int productId, int change);
    public Response changeQuantityInCart(String userName, int storeId, int productId, int change);

//    public Response removeProductFromCart(int storeId, int productId);
//    public Response changeQuantityInCart(int storeId, int productId, int change);
    public Response getCart(int id);
    public Response getCart(String userName);
    public Response makePurchase(int userId , String accountNumber);
    public Response makePurchase(String userName,String accountNumber);
    public Response getStoreDescription(int storeId);

    //member methods
    public Response login(String email , String pass, List<String> answers);
    public Response logout(int userId);
    public Response changePassword(int userId,String oldPass ,String newPass);

    public Response changeName(int userId, String newUserName);
    public Response changeEmail(int userId, String newEmail);

    public Response openStore(int userId,String storeDescription);
    public Response getMemberInformation(int userId);
    public Response getUserPurchaseHistory(int userId);
    public Response writeReviewToStore(int orderId, int storeId, String content, int grading, int userId);
    public Response writeReviewToProduct(int orderId, int storeId,int productId, String content, int grading, int userId);
    public Response getProductInformation(int storeId , int productId);
    public Response getStoreInformation( int storeId);
    public Response getStoreProducts(int storeId);

    public Response sendQuestion(int userId,int storeId,String msg);
    public Response sendComplaint(int userId,int orderId,int storeId,String msg);

    // manager methods
    //todo: miki what purchase and discount policy and constraint should get
    //todo : check if need to make "add constraint" method and "add policy" ...
    public Response appointManager(int userId, int storeId, int managerIdToAppoint);
    public Response changeStoreDescription(int userId,int storeId,String description);
    public Response changePurchasePolicy(int userId,int storeId,String policy);
    public Response changeDiscountPolicy(int userId,int storeId,String policy);
    public Response addPurchaseConstraint(int userId,int storeId,String constraint);
    public Response fireManager(int userId,int storeId,int managerToFire);
    public Response checkWorkersStatus(int userId,int storeId);
    public Response viewQuestions(int userId,int storeId);
    public Response answerQuestion(int userId,int storeId ,int questionId,String answer);
    public Response seeStoreHistory(int userId,int storeId);
    public Response addProduct(int useIid, int storeId,List<String> categories, String name , String description , int price , int quantity);
    public Response deleteProduct(int userId,int storeId,int productId);
    public Response updateProduct(int userId, int storeId,int productId, List<String> categories, String name , String description , int price , int quantity);
    public Response getStoreOrders(int userId , int storeId);

    //store owner methods
    public Response appointOwner(int userId , int storeId,int ownerId);
    public Response fireOwner(int userId , int storeId, int ownerId);
    public Response changeManagerPermission (int userId,int storeId, List<Integer> permissionsIds);
    public Response getAppointments(int userId, int storeId);

    //store creator methods
    public Response closeStore(int userId,int storeId);
    public Response reopenStore(int userId,int storeId);

    public Response closeStorePermanently(int adminId, int storeId);

    //store methods
    //todo: decide if getStore will bring every thing togheter , prosucts , orders , ..statistics
    public Response getStore(int storeId);
    public Response getProducts(int storeId);

    // admin methods
    public Response getAdmins();
    public Response getStores();
    public Response addAdmin(int userId, String email , String pass);
    public Response removeAdmin(int userId , int adminId);
    public Response getUsers(int adminId);
    public Response answerComplaint(int adminId,int complaintId,String ans);
    public Response cancelMembership(int adminId,int userToRemove);
    public Response sendNotification(int adminId,int userToSendTo,String msg);

}
