package database.daos;

import database.DbEntity;
import database.dtos.*;
import domain.store.product.Product;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Bid;
import domain.store.storeManagement.Store;
import domain.user.Basket;
import domain.user.Member;
import domain.user.ShoppingCart;
import domain.user.User;
import org.hibernate.Session;
import org.mockito.internal.matchers.Or;
import utils.Pair;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.orderRelated.Order;

import java.util.*;

public class StoreDao {
    private static HashMap<Integer, Store> storesMap = new HashMap<>();
    private static boolean stores = false;
    private static HashMap<Integer, HashMap<Integer, Product>> productsMap = new HashMap<>();
    private static Set<Integer> products = new HashSet<>();
    private static HashMap<Integer, HashMap<String, Set<Integer>>> categoryMap = new HashMap<>();
    private static HashMap<Integer, Set<String>> categories = new HashMap<>();
    private static Set<Integer> allCategories = new HashSet<>();
    private static HashMap<Integer, HashMap<String, Appointment>> appointmentMap = new HashMap<>();
    private static Set<Integer> appointments = new HashSet<>();
    private static HashMap<Integer, AppHistory> appMap = new HashMap<>();
    private static Set<Integer> app = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, Bid>> bidMap = new HashMap<>();
    private static Set<Integer> bids = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, ConstraintDto>> constraintMap = new HashMap<>();
    private static Set<Integer> constraints = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, DiscountDto>> discountsMap = new HashMap<>();
    private static Set<Integer> discounts = new HashSet<>();
    private static HashMap<Integer, HashMap<Integer, Order>> storeOrderMap = new HashMap<>();
    private static Set<Integer> storeOrders = new HashSet<>();



    //stores
    public static void saveStore(Store s, Session session) throws Exception{
        Dao.save(s, session);
    }

    public static int getMaxStoreId(){
        return Dao.getMaxId("Store", "storeId");
    }

    public static Store getStore(int storeId) throws Exception{
        if(storesMap.containsKey(storeId))
            return storesMap.get(storeId);
        Store s = (Store) Dao.getById(Store.class, storeId);
        if(s != null){
            storesMap.put(s.getStoreId(), s);
            s.initialParams();
        }
        return s;
    }

    public static Store getStore(String  storeName) throws Exception{
        for(Store s : storesMap.values())
            if(s.getName().equals(storeName))
                return s;
        Store s = (Store) Dao.getByParam(Store.class, "Store", "storeName = '%s' ");
        if(s != null){
            storesMap.put(s.getStoreId(), s);
            s.initialParams();
        }
        return s;
    }

    public static List<Store> getAllStores() throws Exception{
        if (!stores) {
            List<? extends DbEntity> storesDto = Dao.getAllInTable("Store");
            for(Store s : (List<Store>) storesDto)
                if(!storesMap.containsKey(s.getStoreId())) {
                    storesMap.put(s.getStoreId(), s);
                    s.initialParams();
                }
            stores = true;
        }
        return new ArrayList<>(storesMap.values());
    }

    public static void removeStore(int storeId, Session session) throws Exception{
        Dao.removeIf("Store", String.format("storeId = %d", storeId), session);
        removeProducts(storeId, session);
        removeAppointments(storeId, session);
        MessageDao.removeStoreMessages(storeId, session);
        removeBids(storeId, session);
        removeConstraints(storeId, session);
        removeDiscounts(storeId, session);
        storesMap.remove(storeId);
    }

    public static void saveProduct(Product p, Session session) throws Exception{
        Dao.save(p, session);
    }

    public static int getMaxProductId(){
        return Dao.getMaxId("Product", "productId");
    }

    public static Product getProduct(int storeId, int productId) throws Exception{
        if(productsMap.containsKey(storeId))
            if(productsMap.get(storeId).containsKey(productId))
                return productsMap.get(storeId).get(productId);
        Product p = (Product) Dao.getByParam(Product.class, "Product",
                String.format("storeId = %d AND productId = %d", storeId, productId));
        if(p != null){
            if(!productsMap.containsKey(storeId))
                productsMap.put(storeId, new HashMap<>());
            productsMap.get(storeId).put(productId, p);
            p.initialParams();
        }
        return p;
    }

    public static Product getProduct(int storeId, String productName) throws Exception{
        if(productsMap.containsKey(storeId))
            for(Product p : productsMap.get(storeId).values())
                if(p.getName().equals(productName))
                    return p;
        Product p = (Product) Dao.getByParam(Product.class, "Product",
                String.format("storeId = %d AND name = '%s' ", storeId, productName));
        if(p != null){
            if(!productsMap.containsKey(storeId))
                productsMap.put(storeId, new HashMap<>());
            productsMap.get(storeId).put(p.productId, p);
            p.initialParams();
        }
        return p;
    }

    public static List<Product> getProducts(int storeId) throws Exception{
        if(!products.contains(storeId)){
            if(!productsMap.containsKey(storeId))
                productsMap.put(storeId, new HashMap<>());
            List<? extends DbEntity> productsDto = Dao.getListById(Product.class, storeId, "Product",
                    "storeId");
            for(Product p : (List<Product>) productsDto)
                if(!productsMap.get(storeId).containsKey(p.productId)) {
                    productsMap.get(storeId).put(p.productId, p);
                    p.initialParams();
                }
            products.add(storeId);
        }
        return new ArrayList<>(productsMap.get(storeId).values());
    }

    public static void removeProduct(int storeId, int productId, Session session) throws Exception{
        Dao.removeIf("Product", String.format("storeId = %d AND productId = %d", storeId, productId), session);
        if(productsMap.containsKey(storeId))
            productsMap.get(storeId).remove(productId);
        Dao.removeIf("CategoryDto", String.format("storeId = %d AND productId = %d", storeId, productId), session);
        if(categoryMap.containsKey(storeId)) {
            HashMap<String, Set<Integer>> set = categoryMap.get(storeId);
            for(Set<Integer> prodcts : set.values())
                prodcts.remove(productId);
        }
    }

    public static void removeProducts(int storeId, Session session) throws Exception{
        Dao.removeIf("Product", String.format("storeId = %d", storeId), session);
        products.remove(storeId);
        productsMap.remove(storeId);
        Dao.removeIf("CategoryDto", String.format("storeId = %d", storeId), session);
        categories.remove(storeId);
        categoryMap.remove(storeId);
    }

    //categories

    public static void saveCategory(CategoryDto categoryDto, Session session) throws Exception{
        Dao.save(categoryDto, session);
    }

    public static List<String> getProductCategories(int storeId, int productId) throws Exception{
        List<String> ans = new ArrayList<>();
        if(!categoryMap.containsKey(storeId))
            categoryMap.put(storeId, new HashMap<>());
        List<? extends DbEntity> categoryDtos = Dao.getByParamList(CategoryDto.class, "CategoryDto",
                String.format("storeId = %d AND productId = %d ", storeId, productId));
        for(CategoryDto categoryDto : (List<CategoryDto>) categoryDtos) {
            if (!categoryMap.get(storeId).containsKey(categoryDto.getCategoryName()))
                categoryMap.get(storeId).put(categoryDto.getCategoryName(), new HashSet<>());
            categoryMap.get(storeId).get(categoryDto.getCategoryName()).add(productId);
            ans.add(categoryDto.getCategoryName());
        }
        return ans;
    }

    public static List<Integer> getCategoryProducts(int storeId, String categoryName) throws Exception{
        if(categories.containsKey(storeId))
            if(categories.get(storeId).contains(categoryName))
                return new ArrayList<>(categoryMap.get(storeId).get(categoryName));

        if(!categories.containsKey(storeId))
            categories.put(storeId, new HashSet<>());

        if(!categoryMap.containsKey(storeId))
            categoryMap.put(storeId, new HashMap<>());
        if(!categoryMap.get(storeId).containsKey(categoryName))
            categoryMap.get(storeId).put(categoryName, new HashSet<>());

        List<? extends DbEntity> categoryDtos = Dao.getByParamList(CategoryDto.class, "CategoryDto",
                String.format("storeId = %d AND categoryName = '%s' ", storeId, categoryName));
        for(CategoryDto categoryDto : (List<CategoryDto>) categoryDtos)
            categoryMap.get(storeId).get(categoryName).add(categoryDto.getProductId());

        categories.get(storeId).add(categoryName);
        return new ArrayList<>(categoryMap.get(storeId).get(categoryName));
    }

    public static HashMap<String, Set<Integer>> getAllCategories(int storeId) throws Exception{
        if(!allCategories.contains(storeId)){

            if(!categoryMap.containsKey(storeId))
                categoryMap.put(storeId, new HashMap<>());

            List<? extends DbEntity> categoryDtos = Dao.getByParamList(CategoryDto.class,
                    "CategoryDto", String.format("storeId = %d ", storeId));
            for(CategoryDto categoryDto : (List<CategoryDto>) categoryDtos) {
                if(!categoryMap.get(storeId).containsKey(categoryDto.getCategoryName()))
                    categoryMap.get(storeId).put(categoryDto.getCategoryName(), new HashSet<>());
                categoryMap.get(storeId).get(categoryDto.getCategoryName()).add(categoryDto.getProductId());
            }
            allCategories.add(storeId);
        }
        return categoryMap.get(storeId);
    }

    //appointments
    public static void saveAppointment(Appointment appointment, Session session) throws Exception{
        Dao.save(appointment, session);
    }

    public static AppHistory.Node getNode(int storeId, int userId, int creatorId) throws Exception{
        if(appMap.containsKey(storeId))
            if (appMap.get(storeId).getNode(userId) != null)
                return appMap.get(storeId).getNode(userId);
        List<? extends DbEntity> appointmentsDto = Dao.getListByCompositeKey(Appointment.class, storeId, userId,
                "AppointmentDto", "storeId", "fatherId");
        if(!appMap.containsKey(storeId))
            appMap.put(storeId, new AppHistory(storeId, new Pair<>(SubscriberDao.getMember(creatorId), SubscriberDao.getRole(creatorId, storeId))));
        appMap.get(storeId).addNode(creatorId, new Pair<>(SubscriberDao.getMember(userId), SubscriberDao.getRole(userId, storeId)));
        for (Appointment appointment : (List<Appointment>) appointmentsDto)
            if(!appMap.get(storeId).isChild(appointment.getFatherId(), appointment.getChildId())) {
                appointment.initialParams();
                appMap.get(storeId).addNode(userId, new Pair<>(SubscriberDao.getMember(appointment.getChildId()),
                        SubscriberDao.getRole(appointment.getChildId(), storeId)));
            }
        return appMap.get(storeId).getNode(userId);
    }

    public static AppHistory getAppHistory(int storeId, int creatorId) throws Exception{
        if(!app.contains(storeId)){
            if(!appMap.containsKey(storeId))
                appMap.put(storeId, new AppHistory(storeId, new Pair<>(SubscriberDao.getMember(creatorId),
                        SubscriberDao.getRole(creatorId, storeId))));
            List<? extends DbEntity> appointmentsDto = Dao.getListById(Appointment.class, storeId,
                    "Appointment", "storeId");
            List<Integer> fathers = new ArrayList<>();
            fathers.add(creatorId);
            while(fathers.size() > 0){
                int id = fathers.remove(0);
                for(Appointment appointment : (List<Appointment>) appointmentsDto)
                    if(appointment.getFatherId() == id) {
                        appointment.initialParams();
                        appMap.get(storeId).addNode(id, new Pair<>(SubscriberDao.getMember(appointment.getChildId()),
                                SubscriberDao.getRole(appointment.getChildId(), storeId)));
                    }
            }
            app.add(storeId);
        }
        return appMap.get(storeId);
    }

    public static Appointment getAppointment(int storeId, String childName) throws Exception{
        if(!appointmentMap.containsKey(storeId))
            appointmentMap.put(storeId, new HashMap<>());

        if(appointmentMap.get(storeId).containsKey(childName))
            return appointmentMap.get(storeId).get(childName);

        Appointment appointment = (Appointment) Dao.getByParam(Appointment.class, "Appointment",
                String.format("storeId = %d AND childName= '%s' ", storeId, childName));
        if(appointment != null) {
            appointmentMap.get(storeId).put(childName, appointment);
            appointment.initialParams();
        }
        return appointment;
    }
    public static List<Appointment> getAppointments(int storeId) throws Exception{
        if(!appointmentMap.containsKey(storeId))
            appointmentMap.put(storeId, new HashMap<>());
        if(!appointments.contains(storeId)){
            List<? extends DbEntity> appointmentsDto = Dao.getListById(Appointment.class, storeId,
                    "Appointment", "storeId");
            for(Appointment appointment : (List<Appointment>) appointmentsDto)
                if(!appointmentMap.get(storeId).containsKey(appointment.getChildName()))
                    appointmentMap.get(storeId).put(appointment.getChildName(), appointment);
            appointments.add(storeId);
        }
        return new ArrayList<>(appointmentMap.get(storeId).values());
    }

    public static void removeAppointment(int storeId, int childId, String childName, Session session) throws Exception{
        Dao.removeIf("Appointment", String.format("storeId = %d AND childName = '%s' ",
                storeId, childName), session);
        Dao.removeIf("Appointment", String.format("storeId = %d AND fatherName = '%s' ",
                storeId, childName), session);
        Dao.removeIf("AppApproved", String.format("storeId = %d AND childId = %d", storeId, childId), session);
        Dao.removeIf("AppApproved", String.format("storeId = %d AND fatherId = %d", storeId, childId), session);
        if(appMap.containsKey(storeId)) {
            if(appMap.get(storeId).getNode(childId) != null)
                appMap.get(storeId).removeChild(childId);
        }
    }

    public static void removeAppointments(int storeId, Session session) throws Exception{
        Dao.removeIf("Appointment", String.format("storeId = %d", storeId), session);
        Dao.removeIf("AppApproved", String.format("storeId = %d", storeId), session);
        app.remove(storeId);
        appMap.remove(storeId);
    }


    //bids
    public static void saveBid(Bid bid, Session session) throws Exception{
        Dao.save(bid, session);
    }

    public static int getMaxBidId(){
        return Dao.getMaxId("Bid", "bidId");
    }

    public static Bid getBid(int storeId, int bidId) throws Exception{
        if(!bidMap.containsKey(storeId))
            bidMap.put(storeId, new HashMap<>());

        if(bidMap.get(storeId).containsKey(bidId))
            return bidMap.get(storeId).get(bidId);

        Bid bid = (Bid) Dao.getByParam(Bid.class, "Bid",
                String.format("bidId = %d AND storeId = %d", bidId, storeId));
        if(bid != null){
            bidMap.get(storeId).put(bid.getBidId(), bid);
            bid.initialParams();
        }
        return bid;
    }

    public static List<Bid> getBids(int storeId) throws Exception{
        if(!bids.contains(storeId)){

            if(!bidMap.containsKey(storeId))
                bidMap.put(storeId, new HashMap<>());

            List<? extends DbEntity> bidsDto = Dao.getByParamList(Bid.class, "Bid",
                    String.format("storeId = %d", storeId));
            for(Bid b : (List<Bid>) bidsDto) {

                if (bidMap.get(storeId).containsKey(b.getBidId())) {
                    bidMap.get(storeId).put(b.getBidId(), b);
                    b.initialParams();
                }
            }
            bids.add(storeId);
        }
        return new ArrayList<>(bidMap.get(storeId).values());
    }

    public static void removeBid(int storeId, int bidId, Session session) throws Exception{
        Dao.removeIf("Bid", String.format("bidId = %d AND storeId = %d", bidId, storeId), session);
        Dao.removeIf("ApproverDto", String.format("storeId = %d AND bidId = %d", storeId, bidId), session);
        if(bidMap.containsKey(storeId))
            bidMap.get(storeId).remove(bidId);
    }

    public static void removeBids(int storeId, Session session) throws Exception{
        Dao.removeIf("Bid", String.format("storeId = %d", storeId), session);
        Dao.removeIf("ApproverDto", String.format("storeId = %d", storeId), session);
        bids.remove(storeId);
        bidMap.remove(storeId);
    }


    //purchaseConstraints
    public static void saveConstraint(ConstraintDto constraintDto, Session session) throws Exception{
        Dao.save(constraintDto, session);
    }

    public static int getMaxConstraintId(){
        return Dao.getMaxId("ConstraintDto", "constraintId");
    }

    public static ConstraintDto getConstraint(int storeId, int constraintId) throws Exception{
        if(!constraintMap.containsKey(storeId))
            constraintMap.put(storeId, new HashMap<>());
        if(constraintMap.containsKey(storeId))
            if(constraintMap.get(storeId).containsKey(constraintId))
                return constraintMap.get(storeId).get(constraintId);
        ConstraintDto constraintDto = (ConstraintDto) Dao.getByParam(ConstraintDto.class, "ConstraintDto",
                String.format("constraintId = %d AND storeId = %d", constraintId, storeId));
        if(constraintDto != null)
            constraintMap.get(storeId).put(constraintId, constraintDto);
        return constraintDto;
    }
    public static List<ConstraintDto> getConstraints(int storeId) throws Exception{
        if(!constraintMap.containsKey(storeId))
            constraintMap.put(storeId, new HashMap<>());
        if(!constraints.contains(storeId)){
            List<? extends DbEntity> constraintDtos = Dao.getListById(ConstraintDto.class, storeId, "ConstraintDto",
                    "storeId");
            for(ConstraintDto constraintDto : (List<ConstraintDto>) constraintDtos)
                if(!constraintMap.get(storeId).containsKey(constraintDto.getConstraintId()))
                    constraintMap.get(storeId).put(constraintDto.getConstraintId(), constraintDto);
            constraints.add(storeId);
        }
        return new ArrayList<>(constraintMap.get(storeId).values());
    }

    public static void removeConstraint(int storeId, int constraintId, Session session) throws Exception{
        Dao.removeIf("ConstraintDto", String.format("storeId = %d AND constraintId = %d", storeId, constraintId), session);
        if(constraintMap.containsKey(storeId))
            constraintMap.get(storeId).remove(constraintId);
    }

    public static void removeConstraints(int storeId, Session session) throws Exception{
        Dao.removeIf("ConstraintDto", String.format("storeId = %d", storeId), session);
        constraintMap.remove(storeId);
    }

    //discounts
    public static void saveDiscount(DiscountDto discountDto, Session session) throws Exception{
        Dao.save(discountDto, session);
    }

    public static int getMaxDiscountId(){
        return Dao.getMaxId("DiscountDto", "discountId");
    }

    public static DiscountDto getDiscount(int storeId, int discountId) throws Exception{
        if(!discountsMap.containsKey(storeId))
            discountsMap.put(storeId, new HashMap<>());
        if(discountsMap.get(storeId).containsKey(discountId))
            return discountsMap.get(storeId).get(discountId);

        DiscountDto discountDto = (DiscountDto) Dao.getByParam(DiscountDto.class, "DiscountDto",
                String.format("discountId = %d AND  storeId = %d", discountId, storeId));
        if(discountDto != null)
            discountsMap.get(storeId).put(discountId, discountDto);
        return discountDto;
    }

    public static List<DiscountDto> getDiscounts(int storeId) throws Exception{
        if(!discountsMap.containsKey(storeId))
            discountsMap.put(storeId, new HashMap<>());
        if(!discounts.contains(storeId)){
            List<? extends DbEntity> discountDtos = Dao.getListById(DiscountDto.class, storeId, "DiscountDto",
                    "storeId");
            for(DiscountDto dto : (List<DiscountDto>) discountDtos)
                if(!discountsMap.get(storeId).containsKey(dto.getDiscountId()))
                    discountsMap.get(storeId).put(dto.getDiscountId(), dto);
            discounts.add(storeId);
        }
        return new ArrayList<>(discountsMap.get(storeId).values());
    }

    public static void removeDiscount(int storeId, int discountId, Session session) throws Exception{
        Dao.removeIf("DiscountDto", String.format("discountId = %d AND storeId = %d", discountId, storeId), session);
        if(discountsMap.containsKey(storeId))
            discountsMap.get(storeId).remove(discountId);
    }

    public static void removeDiscounts(int storeId, Session session) throws Exception{
        Dao.removeIf("DiscountDto", String.format("storeId = %d", storeId), session);
        discountsMap.remove(storeId);
    }


    //storeOrders
    public static int getMaxOrderId(){
        return Dao.getMaxId("Receipt", "orderId");
    }

    public static Order getStoreOrder(int storeId, int orderId) throws Exception{
        if(!storeOrderMap.containsKey(storeId))
            storeOrderMap.put(storeId, new HashMap<>());

        if(storeOrderMap.get(storeId).containsKey(orderId))
            storeOrderMap.get(storeId).get(orderId);
        Order order = null;
        Receipt receipt = SubscriberDao.getReceipt(orderId);
        if(receipt != null){
            receipt.initialParams();
            ShoppingCart cart = new ShoppingCart();
            if(receipt.getCart().hasStore(storeId)) {
                for (ProductInfo p : receipt.getCart().getBasket(storeId).getContent()) {
                    cart.addProductToCart(storeId, p, p.getQuantity());
                }
                order = new Order(receipt.getOrderId(), receipt.getMember(), cart);
                storeOrderMap.get(storeId).put(orderId, order);
            }
        }
        return order;
    }


    public static List<Order> getOrders(int storeId) throws Exception{
        if(!storeOrderMap.containsKey(storeId))
            storeOrderMap.put(storeId, new HashMap<>());
        if(!storeOrders.contains(storeId)) {
            List<? extends DbEntity> receipts = Dao.getListById(ReceiptDto.class, storeId, "ReceiptDto",
                    "storeId");
            HashMap<Integer, ShoppingCart> newOrders = new HashMap<>();
            for(ReceiptDto receiptDto : (List<ReceiptDto>) receipts){
                if(!storeOrderMap.get(storeId).containsKey(receiptDto.getOrderId())){
                    if(!newOrders.containsKey(receiptDto.getOrderId())) {
                        newOrders.put(receiptDto.getOrderId(), new ShoppingCart());
                    }
                    newOrders.get(receiptDto.getOrderId()).addProductToCart(receiptDto.getStoreId(),
                            getProduct(receiptDto.getStoreId(), receiptDto.getProductId()).getProductInfo(),
                            receiptDto.getQuantity());
                }
            }

            for(int orderId : newOrders.keySet()){
                Receipt receipt = SubscriberDao.getReceipt(orderId);
                Member member = SubscriberDao.getMember(receipt.getMemberId());
                Order order = new Order(orderId,member, newOrders.get(orderId));
                order.setTotalPrice(receipt.getTotalPrice());
                storeOrderMap.get(storeId).put(orderId, order);
            }
            storeOrders.add(storeId);
        }
        return new ArrayList<>(storeOrderMap.get(storeId).values());
    }
}
