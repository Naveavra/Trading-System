package store;

import database.Dao;
import domain.store.order.OrderController;
import domain.store.product.Product;
import domain.store.purchase.PurchasePolicy;
import domain.store.purchase.PurchasePolicyDataObject;
import domain.store.purchase.PurchasePolicyFactory;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;
import domain.user.ShoppingCart;

import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;
import org.junit.jupiter.api.*;

import static domain.user.StringChecks.curDay;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchasePolicyTests {
    private StoreController storeCtrl = new StoreController();
    private AtomicInteger pids = new AtomicInteger();
    private AtomicInteger policyIds = new AtomicInteger();
    Member creator = new Member(0, "eli@gmail.com", "123Aaa", "24/02/2002");
    Member worker = new Member(1, "eli1@gmail.com", "123Aaa", "24/02/2015");
    Store store = storeCtrl.createNewStore(creator,"Some Random Description");
    int storeId = store.getStoreId();
    Product tomato;
    Product vodka;
    Product icecream;
    PurchasePolicyFactory factory = new PurchasePolicyFactory(policyIds,storeId);
    boolean flag = true;

    public PurchasePolicyTests() throws Exception {
        Dao.setForTests(true);
    }

    void setUp() throws Exception {
        // Adding Products
        store.addNewProduct("Tomatoes","Red Juicy vegetable-like fruits",pids,50,50,new ArrayList<String>(List.of("fruits"))); //pid = 0
        store.addNewProduct("Vodka","Russian's nectar of the gods",pids,30,50,new ArrayList<>(List.of("alcohol"))); //pid = 1
        store.addNewProduct("Ice-Cream","My Own Weakness",pids,6,50,new ArrayList<>(List.of("icecream"))); //pid = 2

        tomato = store.getInventory().getProduct(0);
        vodka = store.getInventory().getProduct(1);
        icecream = store.getInventory().getProduct(2);
    }

    public void firstTest() throws Exception {
        if(flag){
            setUp();
            flag = false;
        }
    }
    @Test
    public void testBasketDoesntContainMoreThanXProducts() throws Exception {
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Max;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.Item;
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, tomato, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with a policy
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId, storeId, "", limiter, tomato.getID(), -1,
                    "", 5, nullVal, nullVal, null, null, type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
//            or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertNull(creatorIds);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }

    }
    @Test
    public void testUserUnderAgeCantPurchase(){
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Min;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.User;
            int ageLimit = 18;
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, vodka, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter,-1,ageLimit,
                    "alcohol",-1,nullVal,nullVal,null,null,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertNull(creatorIds);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test
    public void testTimeLimitPurchase(){
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Max;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.DateTime;
            int[] timeLimit = {12,0,0};
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, vodka, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter,-1,-1,
                    "alcohol",-1,nullVal,timeLimit,null,null,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertNull(creatorIds);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test
    public void testDateLimitPurchase(){
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Max;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.DateTime;
            int[] dateLimit = {17,6,2023};
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, vodka, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with policy fail
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter,-1,-1,
                    "alcohol",-1,dateLimit,nullVal,null,null,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertNull(creatorIds);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test
    public void testGivenDateCannotPurchase(){
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter1 = PurchasePolicy.limiters.Exact;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.DateTime;
            int[] curDay = curDay();
            int[] dateLimitToday = {curDay[0],curDay[1],curDay[2]};
            int[] dateLimitTommorow = {curDay[0] == 31?1:curDay[0]+1,curDay[1],curDay[2]};
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, vodka, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with policy fail
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter1,-1,-1,
                    "alcohol",-1,dateLimitToday,nullVal,null,null,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertNull(creatorIds);
            store.removePolicy(0);
//            store.removePolicy(1);
            //Creating an order with policy success
            dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter1,-1,-1,
                    "alcohol",-1,dateLimitTommorow,nullVal,null,null,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertTrue(creatorIds.size()>0);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testAndComposePolicies(){
        try {
            firstTest();
            int policyId1 = 0;
            int policyId2 = 1;
            PurchasePolicy.limiters limiter1 = PurchasePolicy.limiters.Max;
            PurchasePolicy.limiters limiter2 = PurchasePolicy.limiters.Min;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.Item;
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, tomato, 5), 5);
            // Creating an order without a policy
            Order or1 = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or1);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with a policy fail
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId1, storeId, "", limiter1, tomato.getID(), -1,
                    "", 5, nullVal, nullVal, null, null, type);
            PurchasePolicyDataObject dataObj2 = new PurchasePolicyDataObject(policyId2, storeId, "", limiter2, vodka.getID(), -1,
                    "", 2, nullVal, nullVal, dataObj, PurchasePolicy.policyComposeTypes.PolicyAnd, type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj2));
//            or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            creatorIds = storeCtrl.purchaseProducts(cart, or1);
            assertNull(creatorIds);
            // Creating an order with a policy success
            cart.changeQuantityInCart(storeId,new ProductInfo(storeId,vodka,2),2);
            Order or2 = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
            creatorIds = storeCtrl.purchaseProducts(cart,or2);
            assertTrue(creatorIds.size()>0);
            store.removePolicy(policyId1);
            store.removePolicy(policyId2);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    //cannot buy alcohol after 23:00 or its an holiday.
    @Test
    public void testOrComposePolicies(){
        try {
            firstTest();
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Max;
            PurchasePolicy.limiters exactLimiter = PurchasePolicy.limiters.Exact;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.DateTime;
            int[] timeLimit = {23,0,0};
            int[] holidayDate = {curDay()[0],curDay()[1],curDay()[2]}; //every day is a holiday with sadna
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, vodka, 6), 6);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with a policy
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId,storeId,"",limiter,-1,-1,
                    "alcohol",-1,nullVal,timeLimit,null,null,type);
            PurchasePolicyDataObject dataObj2 = new PurchasePolicyDataObject(policyId,storeId,"",exactLimiter,-1,-1,
                    "alcohol",-1,holidayDate,nullVal,dataObj, PurchasePolicy.policyComposeTypes.PolicyOr,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj2));
            creatorIds =storeCtrl.purchaseProducts(cart,or);
            assertNull(creatorIds);
            store.removePolicy(policyId);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testConditionalComposePolicies(){
        try {
            firstTest();
            int minAmount = 1;
            int policyId = 0;
            PurchasePolicy.limiters limiter = PurchasePolicy.limiters.Exact;
            PurchasePolicy.limiters minLimit = PurchasePolicy.limiters.Min;
            PurchasePolicy.policyTypes type = PurchasePolicy.policyTypes.Item;
            // Adding Products to cart
            ShoppingCart cart = new ShoppingCart();
            cart.changeQuantityInCart(storeId, new ProductInfo(storeId, tomato, 5), 5);
            // Creating an order without a policy
            Order or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            Set<Integer> creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertTrue(creatorIds.size() > 0);
            // Creating an order with a policy fail
            int[] nullVal = null;
            PurchasePolicyDataObject dataObj2 = new PurchasePolicyDataObject(policyId,storeId,"",minLimit,vodka.getID(),-1,
                    "alcohol",minAmount,nullVal,nullVal,null, null,type);
            PurchasePolicyDataObject dataObj = new PurchasePolicyDataObject(policyId, storeId, "", limiter, tomato.getID(), -1,
                    "", 5, nullVal, nullVal, dataObj2, PurchasePolicy.policyComposeTypes.PolicyConditioning,type);
            store.getPurchasePolicies().add(factory.createPolicy(dataObj));
//            or = new OrderController().createNewOrder(worker, cart, storeCtrl.calculatePrice(cart));
            creatorIds = storeCtrl.purchaseProducts(cart, or);
            assertNull(creatorIds);
            // Creating an order with policy success
            cart.changeQuantityInCart(storeId,new ProductInfo(storeId,vodka,1),1);
            or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
            creatorIds = storeCtrl.purchaseProducts(cart,or);
            assertTrue(creatorIds.size()>0);
        }catch (Exception e){
            fail(e.getMessage());
        }

    }
}
