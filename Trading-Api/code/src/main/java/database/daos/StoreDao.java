package database.daos;

import database.DbEntity;
import database.dtos.Appointment;
import database.dtos.CategoryDto;
import database.dtos.ConstraintDto;
import database.dtos.DiscountDto;
import domain.store.product.Product;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Bid;
import domain.store.storeManagement.Store;
import utils.Pair;

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
    public static void saveStore(Store s){
        Dao.save(s);
    }

    public static Store getStore(int storeId){
        if(storesMap.containsKey(storeId))
            return storesMap.get(storeId);
        Store s = (Store) Dao.getById(Store.class, storeId);
        if(s != null){
            storesMap.put(s.getStoreId(), s);
            s.initialParams();
        }
        return s;
    }

    public static Store getStore(String  storeName){
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

    public static List<Store> getAllStores() {
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

    public static void removeStore(int storeId){
        Dao.removeIf("Store", String.format("storeId = %d", storeId));
        removeProducts(storeId);
        removeAppointments(storeId);
        MessageDao.removeStoreMessages(storeId);
        removeBids(storeId);
        removeConstraints(storeId);
        removeDiscounts(storeId);
        storesMap.remove(storeId);
    }

    public static void saveProduct(Product p){
        Dao.save(p);
    }

    public static Product getProduct(int storeId, int productId){
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

    public static Product getProduct(int storeId, String productName){
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

    public static List<Product> getProducts(int storeId){
        if(!products.contains(storeId)){
            if(!productsMap.containsKey(storeId))
                productsMap.put(storeId, new HashMap<>());
            List<? extends DbEntity> productsDto = Dao.getListById(Product.class, storeId, "Product", "storeId");
            for(Product p : (List<Product>) productsDto)
                if(!productsMap.get(storeId).containsKey(p.productId)) {
                    productsMap.get(storeId).put(p.productId, p);
                    p.initialParams();
                }
            products.add(storeId);
        }
        return new ArrayList<>(productsMap.get(storeId).values());
    }

    public static void removeProduct(int storeId, int productId){
        Dao.removeIf("Product", String.format("storeId = %d AND productId = %d", storeId, productId));
        if(productsMap.containsKey(storeId))
            productsMap.get(storeId).remove(productId);
        Dao.removeIf("CategoryDto", String.format("storeId = %d AND productId = %d", storeId, productId));
        if(categoryMap.containsKey(storeId)) {
            HashMap<String, Set<Integer>> set = categoryMap.get(storeId);
            for(Set<Integer> prodcts : set.values())
                prodcts.remove(productId);
        }
    }

    public static void removeProducts(int storeId) {
        Dao.removeIf("Product", String.format("storeId = %d", storeId));
        products.remove(storeId);
        productsMap.remove(storeId);
        Dao.removeIf("CategoryDto", String.format("storeId = %d", storeId));
        categories.remove(storeId);
        categoryMap.remove(storeId);
    }

    //categories

    public static void saveCategory(CategoryDto categoryDto){
        Dao.save(categoryDto);
    }

    public static List<Integer> getCategoryProducts(int storeId, String categoryName){
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

    public static HashMap<String, Set<Integer>> getAllCategories(int storeId){
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
    public static void saveAppointment(Appointment appointment){
        Dao.save(appointment);
    }

    public static AppHistory.Node getNode(int storeId, int userId, int creatorId){
        if(appMap.containsKey(storeId))
            if (appMap.get(storeId).getNode(userId) != null)
                return appMap.get(storeId).getNode(userId);
        List<? extends DbEntity> appointmentsDto = Dao.getListByCompositeKey(Appointment.class, storeId, userId,
                "AppointmentDto", "storeId", "fatherId");
        if(!appMap.containsKey(storeId))
            appMap.put(storeId, new AppHistory(storeId, new Pair<>(SubscriberDao.getMember(creatorId), SubscriberDao.getRole(creatorId, storeId))));
        try {
            appMap.get(storeId).addNode(creatorId, new Pair<>(SubscriberDao.getMember(userId), SubscriberDao.getRole(userId, storeId)));
            for (Appointment appointment : (List<Appointment>) appointmentsDto)
                if(!appMap.get(storeId).isChild(appointment.getFatherId(), appointment.getChildId())) {
                    appointment.initialParams();
                    appMap.get(storeId).addNode(userId, new Pair<>(SubscriberDao.getMember(appointment.getChildId()),
                            SubscriberDao.getRole(appointment.getChildId(), storeId)));
                }
        }catch (Exception ignored){}
        return appMap.get(storeId).getNode(userId);
    }

    public static AppHistory getAppHistory(int storeId, int creatorId){
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
                        try {
                            appointment.initialParams();
                            appMap.get(storeId).addNode(id, new Pair<>(SubscriberDao.getMember(appointment.getChildId()),
                                    SubscriberDao.getRole(appointment.getChildId(), storeId)));
                        }catch (Exception ignored){}
                    }
            }
            app.add(storeId);
        }
        return appMap.get(storeId);
    }

    public static Appointment getAppointment(int storeId, String childName){
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
    public static List<Appointment> getAppointments(int storeId) {
        return new ArrayList<>();
    }

    public static void removeAppointment(int storeId, int childId, String childName){
        Dao.removeIf("Appointment", String.format("storeId = %d AND childName = '%s' ",
                storeId, childName));
        Dao.removeIf("Appointment", String.format("storeId = %d AND fatherName = '%s' ",
                storeId, childName));
        Dao.removeIf("AppApproved", String.format("storeId = %d AND childId = %d", storeId, childId));
        Dao.removeIf("AppApproved", String.format("storeId = %d AND fatherId = %d", storeId, childId));
        if(appMap.containsKey(storeId)) {
            try {
                appMap.get(storeId).removeChild(childId);
            }catch (Exception ignored){}
        }
    }

    public static void removeAppointments(int storeId) {
        Dao.removeIf("Appointment", String.format("storeId = %d", storeId));
        Dao.removeIf("AppApproved", String.format("storeId = %d", storeId));
        app.remove(storeId);
        appMap.remove(storeId);
    }


    //bids
    public static void saveBid(Bid bid){
        Dao.save(bid);
    }

    public static Bid getBid(int storeId, int bidId){
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

    public static List<Bid> getBids(int storeId){
        if(!bids.contains(storeId)){

            if(!bidMap.containsKey(storeId))
                bidMap.put(storeId, new HashMap<>());

            List<? extends DbEntity> bidsDto = Dao.getByParamList(Bid.class, "Bid", String.format("storeId = %d", storeId));
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

    public static void removeBid(int storeId, int bidId){
        Dao.removeIf("Bid", String.format("bidId = %d AND storeId = %d", bidId, storeId));
        Dao.removeIf("ApproverDto", String.format("storeId = %d AND bidId = %d", storeId, bidId));
        if(bidMap.containsKey(storeId))
            bidMap.get(storeId).remove(bidId);
    }

    public static void removeBids(int storeId){
        Dao.removeIf("Bid", String.format("storeId = %d", storeId));
        Dao.removeIf("ApproverDto", String.format("storeId = %d", storeId));
        bids.remove(storeId);
        bidMap.remove(storeId);
    }


    //purchaseConstraints
    public static void saveConstraint(ConstraintDto constraintDto){
        Dao.save(constraintDto);
    }

    public static ConstraintDto getConstraint(int storeId, int constraintId){
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
    public static List<ConstraintDto> getConstraints(int storeId){
        if(!constraintMap.containsKey(storeId))
            constraintMap.put(storeId, new HashMap<>());
        if(!constraints.contains(storeId)){
            List<? extends DbEntity> constraintDtos = Dao.getListById(ConstraintDto.class, storeId, "ConstraintDto", "storeId");
            for(ConstraintDto constraintDto : (List<ConstraintDto>) constraintDtos)
                if(!constraintMap.get(storeId).containsKey(constraintDto.getConstraintId()))
                    constraintMap.get(storeId).put(constraintDto.getConstraintId(), constraintDto);
            constraints.add(storeId);
        }
        return new ArrayList<>(constraintMap.get(storeId).values());
    }

    public static void removeConstraint(int storeId, int constraintId){
        Dao.removeIf("ConstraintDto", String.format("storeId = %d AND constraintId = %d", storeId, constraintId));
        if(constraintMap.containsKey(storeId))
            constraintMap.get(storeId).remove(constraintId);
    }

    public static void removeConstraints(int storeId){
        Dao.removeIf("ConstraintDto", String.format("storeId = %d", storeId));
        constraintMap.remove(storeId);
    }

    //discounts
    public static void saveDiscount(DiscountDto discountDto){
        Dao.save(discountDto);
    }

    public static DiscountDto getDiscount(int storeId, int discountId){
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

    public static List<DiscountDto> getDiscounts(int storeId){
        if(!discountsMap.containsKey(storeId))
            discountsMap.put(storeId, new HashMap<>());
        if(discounts.contains(storeId)){
            List<? extends DbEntity> discountDtos = Dao.getListById(DiscountDto.class, storeId, "DiscountDto", "storeId");
            for(DiscountDto dto : (List<DiscountDto>) discountDtos)
                if(!discountsMap.get(storeId).containsKey(dto.getDiscountId()))
                    discountsMap.get(storeId).put(dto.getDiscountId(), dto);
            discounts.add(storeId);
        }
        return new ArrayList<>(discountsMap.get(storeId).values());
    }

    public static void removeDiscount(int storeId, int discountId){
        Dao.removeIf("DiscountDto", String.format("discountId = %d AND storeId = %d", discountId, storeId));
        if(discountsMap.containsKey(storeId))
            discountsMap.get(storeId).remove(discountId);
    }

    public static void removeDiscounts(int storeId){
        Dao.removeIf("DiscountDto", String.format("storeId = %d", storeId));
        discountsMap.remove(storeId);
    }
}
