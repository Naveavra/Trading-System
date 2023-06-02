package store;

import domain.store.discount.AbstractDiscount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.order.OrderController;
import domain.store.product.Inventory;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite.logical.*;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite.numeric.*;
import static domain.store.discount.predicates.DiscountPredicate.composore.*;
import static org.junit.jupiter.api.Assertions.*;

import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;
import domain.user.ShoppingCart;
import org.junit.jupiter.api.*;
import utils.infoRelated.ProductInfo;
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
    Member creator = new Member(0, "eli@gmail.com", "123Aaa", "24/02/2002");
    Member worker = new Member(1, "eli1@gmail.com", "123Aaa", "24/02/2002");

    @BeforeEach
    void setUp() throws Exception{
        AtomicInteger inventoryIds1 = new AtomicInteger();
        AtomicInteger inventoryIds2 = new AtomicInteger();
        storeCtrl = new StoreController();
        storeCtrl.createNewStore(creator,"Shoes and stuff");
        s1 = storeCtrl.getStore(0);
        inv1 = s1.getInventory();
        inv1.addProduct("Banana","",inventoryIds1,10,5);
        inv1.addProduct("Apple","",inventoryIds1,20,3);
        inv1.addToCategory("Bananas",0);
        inv1.addToCategory("Fruits",0);
        inv1.addToCategory("Yellow",0);
        inv1.addToCategory("Bananas",1);
        inv1.addToCategory("Fruits",1);
        storeCtrl.createNewStore(worker,"Slippers and stuff");
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

    void categoryDiscountSetup(){
        DiscountDataObject d1 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,"Yellow",new ArrayList<>());
        s1.AddDiscount(d1);
        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,"Bananas",new ArrayList<>());
        s2.AddDiscount(d2);
    }

    void itemDiscountSetup(){
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
        PredicateDataObject p2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"1 1", null);
        predicates.add(p2);

        DiscountDataObject d1 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Product,0,"",new ArrayList<>());
        s1.AddDiscount(d1);

        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Product,1,"",predicates);
        s2.AddDiscount(d2);

        PredicateDataObject p1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumFromCategory,"Yellow 5",And);
        predicates.add(p1);
        DiscountDataObject d3 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Product,0,"",predicates);
        s2.AddDiscount(d3);
    }

    void composeDiscountsSetup() throws Exception {
        DiscountDataObject d1 = new DiscountDataObject(25,AbstractDiscount.discountTypes.Product,1,"",new ArrayList<>());
        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,"Yellow",new ArrayList<>());
        ArrayList<DiscountDataObject> discounts1 = new ArrayList<>();
        discounts1.add(d1);
        discounts1.add(d2);
        CompositeDataObject logical1 = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.Or,null,discounts1,null);
        s1.AddDiscount(logical1);
    }

    @Test
    public void storeDiscountTest() throws Exception {
        storeDiscountSetUp();
        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1);
        int totalPrice = storeCtrl.calculatePrice(cart);
        Order or1 = new OrderController().createNewOrder(worker,cart,totalPrice,storeCtrl::setPrices);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,10);
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1);
        Order or2 = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart),storeCtrl::setPrices);
        double res2 = s1.handleDiscount(or2);
        assertEquals(res2,25.5);
    }

    @Test
    public void categoryDiscountTest() throws Exception {
        categoryDiscountSetup();
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1);
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1);
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1),storeCtrl :: setPrices);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,25);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(0),1),1);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(1),1),1);
        //total price is 60, after discount should be 40
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1),storeCtrl :: setPrices);
        s1.handleDiscount(or2);
        s2.handleDiscount(or2);
        assertEquals(or2.getTotalPrice(),40);
    }

    @Test
    public void itemDiscountTestWithPredCompose() throws Exception{
        itemDiscountSetup();
//        categoryDiscountSetup();
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1); //price = 10
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1); //price = 20
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1),storeCtrl :: setPrices);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,25);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(0),1),1);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(1),1),1);
        //total price is 60, after discount should be 45
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1),storeCtrl :: setPrices);
        s1.handleDiscount(or2);
        s2.handleDiscount(or2);
        assertEquals(or2.getTotalPrice(),45);
    }

    @Test
    public void TestSimpleCompositeDiscount() throws Exception {
        composeDiscountsSetup();
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1); //price = 10 5 after discount
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1); //price = 20 15 after discount
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1),storeCtrl :: setPrices);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,20);

    }


}
