package store;

import domain.store.discount.AbstractDiscount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.order.OrderController;
import domain.store.product.Inventory;
import static org.junit.jupiter.api.Assertions.*;

import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import org.junit.jupiter.api.*;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class DiscountTest {
    private DiscountFactory factory;
    private StoreController storeCtrl;
    Store s1;
    Store s2;
    Inventory inv1;
    Inventory inv2;

    void setUp() throws Exception{
        AtomicInteger inventoryIds1 = new AtomicInteger();
        AtomicInteger inventoryIds2 = new AtomicInteger();
        storeCtrl = new StoreController();
        storeCtrl.createNewStore(0,"Shoes and stuff");
        s1 = storeCtrl.getStore(0);
        inv1 = s1.getInventory();
        inv1.addProduct("Banana","",inventoryIds1,10,5);
        inv1.addProduct("Apple","",inventoryIds1,20,3);
        inv1.addToCategory("Bananas",0);
        inv1.addToCategory("Fruits",0);
        inv1.addToCategory("Yellow",0);
        inv1.addToCategory("Bananas",1);
        inv1.addToCategory("Fruits",1);
        storeCtrl.createNewStore(1,"Slippers and stuff");
        s2 = storeCtrl.getStore(1);
        inv2 = s2.getInventory();
        inv2.addProduct("Banana","",inventoryIds2,10,5);
        inv2.addProduct("Apple","",inventoryIds2,20,3);
        inv2.addToCategory("Bananas",0);
        inv2.addToCategory("Fruits",0);
        inv2.addToCategory("Yellow",0);
        inv2.addToCategory("Bananas",1);
        inv2.addToCategory("Fruits",1);
    }
    //Discount On Store
    void storeDiscountSetUp(){
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
//        new PredicateFactory().createPredicate(DiscountPredicate.PredicateTypes.MinPrice,"20",0,inv1 :: getProductCategories);
        PredicateDataObject p1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,"20",null);
        predicates.add(p1);
        //Discounts
        DiscountDataObject d1 = new DiscountDataObject(15, AbstractDiscount.discountTypes.Store,0,"",predicates);
        s1.AddDiscount(d1);
    }


    //TODO: fix test to put shopping cart in order

//    @Test
//    public void storeDiscountTest() throws Exception {
//        setUp();
//        storeDiscountSetUp();
//        HashMap<Integer,HashMap<Integer,Integer>> products = new HashMap<>();
//        products.put(0,new HashMap<>());
//        products.get(0).put(0,1); //price is 10
//        Order or1 = new OrderController().createNewOrder(0,products,storeCtrl::calculatePrice,storeCtrl::setPrices);
//        double res1 = s1.handleDiscount(or1);
//        assertEquals(res1,10);
//        products.get(0).put(1,1); //price is 30
//        Order or2 = new OrderController().createNewOrder(0,products,storeCtrl::calculatePrice,storeCtrl::setPrices);
////        storeCtrl.purchaseProducts()
//        double res2 = s1.handleDiscount(or2);
//        assertEquals(res2,25.5);
//    }
}
