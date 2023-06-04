package store;

import domain.store.discount.AbstractDiscount;
import domain.store.discount.DiscountFactory;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.order.OrderController;
import domain.store.product.Inventory;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
public class DiscountTest {
    private StoreController storeCtrl;
    int s1Product1ID;
    int s1Product2ID;
    int s1Product3ID;
    int s2Product1ID;
    int s2Product2ID;
    String Bananas = "Bananas";
    String Fruits = "Fruits";
    String Yellow = "Yellow";
    String Electronics = "Electronics";
    String Laptops = "Laptops";
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

        //inventory 1 items
        storeCtrl.createNewStore(creator,"Shoes and stuff");
        s1 = storeCtrl.getStore(0);
        inv1 = s1.getInventory();
        //Products
        inv1.addProduct("Banana","",inventoryIds1,10,5);
        s1Product1ID = inventoryIds1.get()-1;
        inv1.addProduct("Apple","",inventoryIds1,20,3);
        s1Product2ID = inventoryIds1.get()-1;
        inv1.addProduct("Laptop","Computers and stuff",inventoryIds1,100,10);
        s1Product3ID = inventoryIds1.get()-1;
        inv1.addToCategory(Bananas,s1Product1ID);
        inv1.addToCategory(Fruits,s1Product1ID);
        inv1.addToCategory(Yellow,s1Product1ID);
        inv1.addToCategory(Bananas,s1Product2ID);
        inv1.addToCategory(Fruits,s1Product2ID);
        inv1.addToCategory(Electronics,s1Product3ID);
        inv1.addToCategory(Laptops,s1Product3ID);

        //inventory 2 items
        storeCtrl.createNewStore(worker,"Slippers and stuff");
        s2 = storeCtrl.getStore(1);
        inv2 = s2.getInventory();
        inv2.addProduct("Banana","",inventoryIds2,10,5);
        s2Product1ID = inventoryIds2.get()-1;
        inv2.addProduct("Apple","",inventoryIds2,20,3);
        s2Product2ID = inventoryIds2.get()-1;
        inv2.addToCategory(Bananas,s2Product1ID);
        inv2.addToCategory(Fruits,s2Product1ID);
        inv2.addToCategory(Yellow,s2Product1ID);
        inv2.addToCategory(Bananas,s2Product2ID);
        inv2.addToCategory(Fruits,s2Product2ID);
    }
    //Discount On Store
    void storeDiscountSetUp(){
        String minPrice = "20";
        double percentage = 15;
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
//        new PredicateFactory().createPredicate(DiscountPredicate.PredicateTypes.MinPrice,"20",0,inv1 :: getProductCategories);
        PredicateDataObject p1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,minPrice,null);
        predicates.add(p1);
        //Discounts
        DiscountDataObject d1 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Store,0,"",predicates);
        s1.addDiscount(d1);
    }

    void categoryDiscountSetup(){
        DiscountDataObject d1 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,Yellow,new ArrayList<>());
        s1.addDiscount(d1);
        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,Bananas,new ArrayList<>());
        s2.addDiscount(d2);
    }

    void itemDiscountSetup(){
        String minNumOfItemPredParams = "1 1";
        String minNumFromCategoryParams = "Yellow 5";
        double percentage = 50;
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
        PredicateDataObject p2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemPredParams, null);
        predicates.add(p2);

        DiscountDataObject d1 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s1Product1ID,"",new ArrayList<>());
        s1.addDiscount(d1);

        DiscountDataObject d2 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s2Product2ID,"",predicates);
        s2.addDiscount(d2);

        PredicateDataObject p1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumFromCategory,minNumFromCategoryParams,And);
        predicates.add(p1);
        DiscountDataObject d3 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s2Product1ID,"",predicates);
        s2.addDiscount(d3);
    }

    void composeDiscountsSetup() throws Exception {
        DiscountDataObject d1 = new DiscountDataObject(25,AbstractDiscount.discountTypes.Product,1,"",new ArrayList<>());
        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,Yellow,new ArrayList<>());
        ArrayList<DiscountDataObject> discounts1 = new ArrayList<>();
        discounts1.add(d1);
        discounts1.add(d2);
        CompositeDataObject logical1 = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.Or,null,discounts1,null);
        s1.addDiscount(logical1);
    }

    @Test
    public void storeDiscountTest() throws Exception {
        storeDiscountSetUp();
        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1);
        int totalPrice = storeCtrl.calculatePrice(cart);
        Order or1 = new OrderController().createNewOrder(worker,cart,totalPrice);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,10);
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1);
        Order or2 = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
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
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,25);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(0),1),1);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(1),1),1);
        //total price is 60, after discount should be 40
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
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
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,25);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(0),1),1);
        cart1.addProductToCart(1,new ProductInfo(1,inv2.getProduct(1),1),1);
        //total price is 60, after discount should be 45
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        s1.handleDiscount(or2);
        s2.handleDiscount(or2);
        assertEquals(or2.getTotalPrice(),45);
    }

    @Test
    public void testSimpleCompositeDiscount() throws Exception {
        composeDiscountsSetup();
        ShoppingCart cart1 = new ShoppingCart();
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1); //price = 10 5 after discount
        cart1.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1); //price = 20 15 after discount
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,20);

    }


    @Test
    public void tenPercentDiscountOver200() throws Exception {
        PredicateDataObject predicate = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,"200",null);
        DiscountDataObject discount = new DiscountDataObject(10, AbstractDiscount.discountTypes.Product,0,"",new ArrayList<>(List.of(predicate)));
        s1.addDiscount(discount);
        ShoppingCart cart = new ShoppingCart();
        //added 2 bananas, 20 before discount
        cart.addProductToCart(0,new ProductInfo(0, inv1.getProduct(0),2),2);
        //price should be 120 now and there shouldn't be any discount;
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),1),1);
        Order or1 = new OrderController().createNewOrder(worker,cart, storeCtrl.calculatePrice(cart));
        assertEquals(s1.handleDiscount(or1),120);
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),1),1);
        or1 = new OrderController().createNewOrder(worker,cart, storeCtrl.calculatePrice(cart));
        assertEquals(218,s1.handleDiscount(or1));
    }
    //• xor:" הנחה על מוצרי חלב או על מאפים, אבל לא שתיהן . בתוספת כלל להכרעה" .
    @Test
    public void xorLogicalCompositeTest() throws Exception {
        DiscountDataObject dis1 = new DiscountDataObject(50, AbstractDiscount.discountTypes.Category,0,"Bananas",null);
        DiscountDataObject dis2 = new DiscountDataObject(30, AbstractDiscount.discountTypes.Category,0,"Electronics",null);
        CompositeDataObject composite = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.Xor, LogicalDiscountComposite.xorDecidingRules.MaxDiscountValue,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),2),2); //price = 20 -> 10 after
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),1),1); //price = 100 -> 70 after so this has max value

        Order order = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));

        assertEquals(90,s1.handleDiscount(order));
    }


    // יש הנחת מאפים של 10% על לחמניות ולחם רק אם הסל מכיל לפחות 5
    //לחמניות וגם לפחות 2 כיכרות לחם "
    @Test
    public void andLogicalCompositeTest() throws Exception{
        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"0 5",null);
        DiscountDataObject dis1 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Product,0,"",new ArrayList<>(List.of(pred1)));
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"2 2",null);
        DiscountDataObject dis2 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Product,2,"",new ArrayList<>(List.of(pred2)));
        CompositeDataObject composite = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.And, null,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),4),4);
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),2),2);

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(240,s1.handleDiscount(or)); //no discount because it doesnt uphold all predicates

        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1);
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(225,s1.handleDiscount(or));
    }

    // יש 5% הנחה על מוצרי חלב אם הסל מכיל לפחות 3 גביעי קוטג' או לפחות 2
    //יוגורטים ".
    @Test
    public void orLogicalCompositeTest() throws Exception{
        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"0 3",null);
        DiscountDataObject dis1 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Category,0,"Bananas",new ArrayList<>(List.of(pred1)));
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"1 2",null);
        DiscountDataObject dis2 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Category,1,"Bananas",new ArrayList<>(List.of(pred2)));
        CompositeDataObject composite = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.Or, null,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),2),2);
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),2),2);

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(54,s1.handleDiscount(or));

        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1);
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(56,s1.handleDiscount(or));

    }
    //"אם שווי
    //הסל גבוה מ 100₪- וגם הסל מכיל לפחות 3 חבילות פסטה, אז יש הנחה של 5%
    //על מוצרי חלב.
    @Test
    public void predComposeTest() throws Exception {
        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,"100", null);
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,"0 3",And);
        DiscountDataObject dis = new DiscountDataObject(10, AbstractDiscount.discountTypes.Category,0,"Bananas",new ArrayList<>(Arrays.asList(pred1,pred2)));
        s1.addDiscount(dis);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),2),2); //20
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),2),1); //100

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(120,s1.handleDiscount(or));

        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),1),1); //10
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(127,s1.handleDiscount(or));

    }
    //ההנחה הניתנת היא המקס' מבין הערך בש"ח של 5% מעלות כל חבילות
    //הפסטה בסל לבין הערך בש"ח של 17% מעלות קרטוני החלב בסל."
    @Test
    public void maxNumericCompositeTest() throws Exception {
        DiscountDataObject d1 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Product,0,"",null);
        DiscountDataObject d2 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Product,1,"",null);
        CompositeDataObject comp = new CompositeDataObject(10, NumericDiscountComposite.numeric.Max,null,null,new ArrayList<>(Arrays.asList(d1,d2)),null);
        s1.addDiscount(comp);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),3),3); //30
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(1),1),1); //20

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(47,s1.handleDiscount(or));
    }

    // יש 5% הנחה על מוצרי חלב, ובנוסף, יש 20% הנחה על כל חנות לכן על
    //מוצרי חלב יש בסה"כ 25% הנחה".
    @Test
    public void additionNumericCompositeTest() throws Exception {
        DiscountDataObject d1 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Category,0,"Bananas",null);
        DiscountDataObject d2 = new DiscountDataObject(10, AbstractDiscount.discountTypes.Store,1,"",null);
        CompositeDataObject comp = new CompositeDataObject(10, NumericDiscountComposite.numeric.Addition,null,null,new ArrayList<>(Arrays.asList(d1,d2)),null);
        s1.addDiscount(comp);

        ShoppingCart cart = new ShoppingCart();
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(0),3),3); //30 ->27 ->
        cart.addProductToCart(0,new ProductInfo(0,inv1.getProduct(2),1),1); //100 ->100 ->90

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(114,s1.handleDiscount(or));
    }
}
