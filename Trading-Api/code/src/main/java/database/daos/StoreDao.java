package database.daos;

import database.DbEntity;
import database.dtos.AppointmentDto;
import database.dtos.CategoryDto;
import domain.store.product.Product;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Store;
import utils.Pair;

import java.awt.event.PaintEvent;
import java.util.*;
import java.util.jar.JarEntry;

public class StoreDao {
    private static HashMap<Integer, Store> storesMap = new HashMap<>();
    private static boolean stores = false;
    private static HashMap<Integer, HashMap<Integer, Product>> productsMap = new HashMap<>();
    private static HashMap<Integer, Boolean> products = new HashMap<>();
    private static HashMap<Integer, HashMap<String, Set<Integer>>> categoryMap = new HashMap<>();
    private static HashMap<Integer, HashMap<String, Boolean>> categories = new HashMap<>();
    private static HashMap<Integer, Boolean> allCategories = new HashMap<>();
    private static HashMap<Integer, AppHistory> appointmentMap = new HashMap<>();
    private static HashMap<Integer, Boolean> appointments = new HashMap<>();
    public static void saveStore(Store s){
        Dao.save(s);
    }

    public static Store getStore(int storeId){
        if(storesMap.containsKey(storeId))
            return storesMap.get(storeId);
        Store s = (Store) Dao.getById(Store.class, storeId);
        if(s != null){
            storesMap.put(s.getStoreId(), s);
            products.put(s.getStoreId(), false);
            appointments.put(s.getStoreId(), false);
            categories.put(s.getStoreId(), new HashMap<>());
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
            products.put(s.getStoreId(), false);
            appointments.put(s.getStoreId(), false);
            categories.put(s.getStoreId(), new HashMap<>());
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
        if(!products.containsKey(storeId) || !products.get(storeId)){
            if(!productsMap.containsKey(storeId))
                productsMap.put(storeId, new HashMap<>());
            List<? extends DbEntity> productsDto = Dao.getListById(Product.class, storeId, "Product", "storeId");
            for(Product p : (List<Product>) productsDto)
                if(!productsMap.get(storeId).containsKey(p.productId)) {
                    productsMap.get(storeId).put(p.productId, p);
                    p.initialParams();
                }
            products.put(storeId, true);
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

    //categories

    public static void saveCategory(CategoryDto categoryDto){
        Dao.save(categoryDto);
    }

    public static List<Integer> getCategoryProducts(int storeId, String categoryName){
        if(categories.containsKey(storeId))
            if(categories.get(storeId).get(categoryName))
                return new ArrayList<>(categoryMap.get(storeId).get(categoryName));

        if(!categories.containsKey(storeId))
            categories.put(storeId, new HashMap<>());

        if(!categoryMap.containsKey(storeId))
            categoryMap.put(storeId, new HashMap<>());
        if(!categoryMap.get(storeId).containsKey(categoryName))
            categoryMap.get(storeId).put(categoryName, new HashSet<>());

        List<? extends DbEntity> categoryDtos = Dao.getByParamList(CategoryDto.class, "CategoryDto",
                String.format("storeId = %d AND categoryName = '%s' ", storeId, categoryName));
        for(CategoryDto categoryDto : (List<CategoryDto>) categoryDtos)
            categoryMap.get(storeId).get(categoryName).add(categoryDto.getProductId());

        categories.get(storeId).put(categoryName, true);
        return new ArrayList<>(categoryMap.get(storeId).get(categoryName));
    }

    public static HashMap<String, Set<Integer>> getAllCategories(int storeId){
        if(!allCategories.containsKey(storeId) || !allCategories.get(storeId)){
            if(!categoryMap.containsKey(storeId))
                categoryMap.put(storeId, new HashMap<>());

            List<? extends DbEntity> categoryDtos = Dao.getByParamList(CategoryDto.class,
                    "CategoryDto", String.format("storeId = %d ", storeId));
            for(CategoryDto categoryDto : (List<CategoryDto>) categoryDtos) {
                if(!categoryMap.get(storeId).containsKey(categoryDto.getCategoryName()))
                    categoryMap.get(storeId).put(categoryDto.getCategoryName(), new HashSet<>());
                categoryMap.get(storeId).get(categoryDto.getCategoryName()).add(categoryDto.getProductId());
            }
            allCategories.put(storeId,true);
        }
        return categoryMap.get(storeId);
    }

    //appointments
    public static void saveAppointment(AppointmentDto appointmentDto){
        Dao.save(appointmentDto);
    }

    public static AppHistory.Node getNode(int storeId, int userId, int creatorId){
        if(appointmentMap.containsKey(storeId))
            if (appointmentMap.get(storeId).getNode(userId) != null)
                return appointmentMap.get(storeId).getNode(userId);
        List<? extends DbEntity> appointmentsDto = Dao.getListByCompositeKey(AppointmentDto.class, storeId, userId,
                "AppointmentDto", "storeId", "fatherId");
        if(!appointmentMap.containsKey(storeId))
            appointmentMap.put(storeId, new AppHistory(storeId, new Pair<>(SubscriberDao.getMember(creatorId), SubscriberDao.getRole(creatorId, storeId))));
        try {
            appointmentMap.get(storeId).addNode(creatorId, new Pair<>(SubscriberDao.getMember(userId), SubscriberDao.getRole(userId, storeId)));
            for (AppointmentDto appointmentDto : (List<AppointmentDto>) appointmentsDto)
                if(!appointmentMap.get(storeId).isChild(appointmentDto.getFatherId(), appointmentDto.getChildId()))
                    appointmentMap.get(storeId).addNode(userId, new Pair<>(SubscriberDao.getMember(appointmentDto.getChildId()),
                            SubscriberDao.getRole(appointmentDto.getChildId(), storeId)));
        }catch (Exception ignored){}
        return appointmentMap.get(storeId).getNode(userId);
    }

    public static AppHistory getAppHistory(int storeId, int creatorId){
        if(!appointments.containsKey(storeId) || !appointments.get(storeId)){
            if(!appointmentMap.containsKey(storeId))
                appointmentMap.put(storeId, new AppHistory(storeId, new Pair<>(SubscriberDao.getMember(creatorId),
                        SubscriberDao.getRole(creatorId, storeId))));
            List<? extends DbEntity> appointmentsDto = Dao.getListById(AppointmentDto.class, storeId,
                    "AppointmentDto", "storeId");
            List<Integer> fathers = new ArrayList<>();
            fathers.add(creatorId);
            while(fathers.size() > 0){
                int id = fathers.remove(0);
                for(AppointmentDto appointmentDto : (List<AppointmentDto>) appointmentsDto)
                    if(appointmentDto.getFatherId() == id) {
                        try {
                            appointmentMap.get(storeId).addNode(id, new Pair<>(SubscriberDao.getMember(appointmentDto.getChildId()),
                                    SubscriberDao.getRole(appointmentDto.getChildId(), storeId)));
                        }catch (Exception ignored){}
                    }
            }
            appointments.put(storeId, true);
        }
        return appointmentMap.get(storeId);
    }

    public static void removeAppointment(int storeId, int childId){
        Dao.removeIf("AppointmentDto", String.format("storeId = %d AND childId = %d",
                storeId, childId));
        Dao.removeIf("AppointmentDto", String.format("storeId = %d AND fatherId = %d",
                storeId, childId));
        if(appointmentMap.containsKey(storeId)) {
            try {
                appointmentMap.get(storeId).removeChild(childId);
            }catch (Exception ignored){}
        }
    }
}
