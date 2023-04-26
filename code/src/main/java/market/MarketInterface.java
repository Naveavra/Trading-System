package market;

import utils.marketRelated.Response;

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
    public Response addProductToCart(String name, int storeId, int productId, int quantity);

    public Response removeProductFromCart(int userId,  int storeId, int productId);
    public Response removeProductFromCart(String userName,  int storeId, int productId);
    public Response changeQuantityInCart(int userId, int storeId, int productId, int change);
    public Response changeQuantityInCart(String userName, int storeId, int productId, int change);

    public Response getCart(int id);
    public Response getCart(String userName);
    public Response makePurchase(int userId , String accountNumber);
    public Response getStoreDescription(int storeId);

    //member methods
    public Response login(String email , String pass, List<String> answers);
    public Response addSecurityQuestion(int userId, String question, String answer);
    public Response changeAnswerForLoginQuestion(int userId, String question, String answer);
    public Response removeSecurityQuestion(int userId, String question);
    public Response displayNotifications(int userId);
    public Response logout(int userId);
    public Response changePassword(int userId,String oldPass ,String newPass);

    public Response changeName(int userId, String newUserName);
    public Response changeEmail(int userId, String newEmail);

    public Response openStore(int userId,String storeDescription);
    public Response getMemberInformation(int userId);
    public Response getUserPurchaseHistory(int userId);
    public Response writeReviewToStore(int orderId, int storeId, String content, int grading, int userId);
    public Response writeReviewToProduct(int orderId, int storeId,int productId, String content, int grading, int userId);
    public Response checkReviews(int userId, int storeId);
    public Response getProductInformation(int storeId , int productId);
    public Response getStoreInformation( int storeId);
    public Response getStoreProducts(int storeId);

    public Response sendQuestion(int userId,int storeId,String msg);
    public Response sendComplaint(int userId,int orderId,int storeId,String msg);

    // manager methods
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
    public Response addManagerPermission (int ownerId, int userId,int storeId, int permissionsId);
    public Response removeManagerPermission (int ownerId, int userId,int storeId, int permissionsId);
    public Response getAppointments(int userId, int storeId);

    //store creator methods
    public Response closeStore(int userId,int storeId);
    public Response reopenStore(int userId,int storeId);

    public Response closeStorePermanently(int adminId, int storeId);

    //store methods
    //todo: decide if getStore will bring every thing togheter , products , orders , ..statistics
    public Response getStore(int storeId);
    public Response getProducts(int storeId);

    // admin methods
    public Response adminLogin(String email ,String pass);
    public Response adminLogout(int adminId);
    public Response getAdmins(int adminId);
    public Response getStores();
    public Response addAdmin(int userId, String email , String pass);
    public Response removeAdmin(int adminId);
    public Response getUsersPurchaseHistory(int adminId);
    public Response answerComplaint(int adminId,int complaintId,String ans);
    public Response cancelMembership(int adminId,int userToRemove);
    public Response watchLog(int adminId);

    public Response watchMarketStatus(int adminId);

}
