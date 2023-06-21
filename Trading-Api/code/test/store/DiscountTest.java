package store;

import database.daos.Dao;
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
import org.json.JSONObject;
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
    Member creator;
    Member worker;
    JSONObject content = new JSONObject();

    @BeforeEach
    void setUp() throws Exception{
        Dao.setForTests(true);
        content.put("description","");
        creator = new Member(2, "eli@gmail.com", "123Aaa", "24/02/2002");
        worker = new Member(3, "eli1@gmail.com", "123Aaa", "24/02/2002");
        AtomicInteger inventoryIds1 = new AtomicInteger();
        AtomicInteger inventoryIds2 = new AtomicInteger();
        storeCtrl = new StoreController();

        //inventory 1 items
        s1 = storeCtrl.createNewStore(creator,"Shoes and stuff", null);
        inv1 = s1.getInventory();
        //Products
        List<String> categories = new ArrayList<>();
        inv1.addProduct("Banana","",inventoryIds1,10,5, categories, null);
        s1Product1ID = inventoryIds1.get()-1;
        inv1.addProduct("Apple","",inventoryIds1,20,3, categories, null);
        s1Product2ID = inventoryIds1.get()-1;
        inv1.addProduct("Laptop","Computers and stuff",inventoryIds1,100,10, categories, null);
        s1Product3ID = inventoryIds1.get()-1;
        inv1.addToCategory(Bananas,s1Product1ID, null);
        inv1.addToCategory(Fruits,s1Product1ID, null);
        inv1.addToCategory(Yellow,s1Product1ID, null);
        inv1.addToCategory(Bananas,s1Product2ID, null);
        inv1.addToCategory(Fruits,s1Product2ID, null);
        inv1.addToCategory(Electronics,s1Product3ID, null);
        inv1.addToCategory(Laptops,s1Product3ID, null);

        //inventory 2 items
        s2 = storeCtrl.createNewStore(worker,"Slippers and stuff", null);
        inv2 = s2.getInventory();
        inv2.addProduct("Banana","",inventoryIds2,10,5, categories, null);
        s2Product1ID = inventoryIds2.get()-1;
        inv2.addProduct("Apple","",inventoryIds2,20,3, categories, null);
        s2Product2ID = inventoryIds2.get()-1;
        inv2.addToCategory(Bananas,s2Product1ID, null);
        inv2.addToCategory(Fruits,s2Product1ID, null);
        inv2.addToCategory(Yellow,s2Product1ID, null);
        inv2.addToCategory(Bananas,s2Product2ID, null);
        inv2.addToCategory(Fruits,s2Product2ID, null);
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
        try {
            s1.addDiscount(d1, content.toString(), null);
        }catch (Exception e){
            assert false;
        }
    }

    void categoryDiscountSetup(){
        DiscountDataObject d1 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,Yellow,new ArrayList<>());
        try {
            s1.addDiscount(d1, content.toString(), null);
        }catch (Exception e){
            assert false;
        }
        DiscountDataObject d2 = new DiscountDataObject(50,AbstractDiscount.discountTypes.Category,0,Bananas,new ArrayList<>());
        try {
            s2.addDiscount(d2, content.toString(), null);
        }catch (Exception e){
            assert false;
        }
    }

    void itemDiscountSetup(){
        String minNumOfItemPredParams = "1 1";
        String minNumFromCategoryParams = "Yellow 5";
        double percentage = 50;
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
        PredicateDataObject p2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemPredParams, null);
        predicates.add(p2);

        DiscountDataObject d1 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s1Product1ID,"",new ArrayList<>());
        try {
            s1.addDiscount(d1, content.toString(), null);
        }catch (Exception e){
            assert false;
        }
        DiscountDataObject d2 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s2Product2ID,"",predicates);
        try {
            s2.addDiscount(d2, content.toString(), null);
        }catch (Exception e){
            assert false;
        }

        PredicateDataObject p1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumFromCategory,minNumFromCategoryParams,And);
        predicates.add(p1);
        DiscountDataObject d3 = new DiscountDataObject(percentage,AbstractDiscount.discountTypes.Product,s2Product1ID,"",predicates);
        try {
            s2.addDiscount(d3, content.toString(), null);
        }catch (Exception e){
            assert false;
        }
    }

    void composeDiscountsSetup() throws Exception {
        double percent25 = 25;
        double percent50 = 50;
        DiscountDataObject d1 = new DiscountDataObject(percent25,AbstractDiscount.discountTypes.Product,s1Product2ID,"",new ArrayList<>());
        DiscountDataObject d2 = new DiscountDataObject(percent50,AbstractDiscount.discountTypes.Category,0,Yellow,new ArrayList<>());
        ArrayList<DiscountDataObject> discounts1 = new ArrayList<>();
        discounts1.add(d1);
        discounts1.add(d2);
        CompositeDataObject logical1 = new CompositeDataObject(percent50,null, LogicalDiscountComposite.logical.Or,null,discounts1,null);
        s1.addDiscount(logical1,"", null);
    }

    @Test
    public void storeDiscountTest() throws Exception {
        storeDiscountSetUp();
        int quantity = 1;
        double actualWithoutDiscount = 10;
        double actualAfterDiscount = 25.5;
        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),quantity),quantity);
        int totalPrice = storeCtrl.calculatePrice(cart);
        Order or1 = new OrderController().createNewOrder(worker,cart,totalPrice);
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,actualWithoutDiscount);
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),quantity),quantity);
        Order or2 = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        double res2 = s1.handleDiscount(or2);
        assertEquals(res2,actualAfterDiscount);
    }

    @Test
    public void categoryDiscountTest() throws Exception {
        categoryDiscountSetup();
        int quantity = 1;
        double afterFirstDiscount = 25;
        double afterSecondDiscount = 40;
        ShoppingCart cart1 = new ShoppingCart();
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),quantity),quantity);
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),quantity),quantity);
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,afterFirstDiscount);
        cart1.changeQuantityInCart(s2.getStoreId(),new ProductInfo(s2.getStoreId(),inv2.getProduct(s2Product1ID),quantity),quantity);
        cart1.changeQuantityInCart(s2.getStoreId(),new ProductInfo(s2.getStoreId(),inv2.getProduct(s2Product2ID),quantity),quantity);
        //total price is 60, after discount should be 40
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        s1.handleDiscount(or2);
        s2.handleDiscount(or2);
        assertEquals(or2.getTotalPrice(),afterSecondDiscount);
    }

    @Test
    public void itemDiscountTestWithPredCompose() throws Exception{
        itemDiscountSetup();
//        categoryDiscountSetup();
        int quantity =1;
        double afterFirstDiscount = 25;
        double afterSecondDiscount = 45;
        ShoppingCart cart1 = new ShoppingCart();
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),quantity),quantity);
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),quantity),quantity);
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,afterFirstDiscount);
        cart1.changeQuantityInCart(s2.getStoreId(),new ProductInfo(s2.getStoreId(),inv2.getProduct(s2Product1ID),quantity),quantity);
        cart1.changeQuantityInCart(s2.getStoreId(),new ProductInfo(s2.getStoreId(),inv2.getProduct(s2Product2ID),quantity),quantity);
        //total price is 60, after discount should be 45
        Order or2 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        s1.handleDiscount(or2);
        s2.handleDiscount(or2);
        assertEquals(or2.getTotalPrice(),afterSecondDiscount);
    }

    @Test
    public void testSimpleCompositeDiscount() throws Exception {
        composeDiscountsSetup();
        int quantity = 1;
        double afterDiscount = 20;
        ShoppingCart cart1 = new ShoppingCart();
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),quantity),quantity); //price = 10 5 after discount
        cart1.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),quantity),quantity); //price = 20 15 after discount
        //total price is 30, after discount should be 25.
        Order or1 = new OrderController().createNewOrder(worker,cart1,storeCtrl.calculatePrice(cart1));
        double res1 = s1.handleDiscount(or1);
        assertEquals(res1,afterDiscount);

    }


    @Test
    public void tenPercentDiscountOver200() throws Exception {
        String minPriceParams = "200";
        int percentage = 10;
        double expectedAfterDiscount = 218;
        double expectedBeforeDiscount = 120;
        int prod1InitialQuantity = 2;
        int prod3Quantity = 1;
        PredicateDataObject predicate = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,minPriceParams,null);
        DiscountDataObject discount = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Product,s1Product1ID,"",new ArrayList<>(Arrays.asList(predicate)));

        s1.addDiscount(discount,content.toString(), null);
        ShoppingCart cart = new ShoppingCart();
        //added 2 bananas, 20 before discount
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(), inv1.getProduct(s1Product1ID),prod1InitialQuantity),prod1InitialQuantity);
        //price should be 120 now and there shouldn't be any discount;
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity);
        Order or1 = new OrderController().createNewOrder(worker,cart, storeCtrl.calculatePrice(cart));
        assertEquals(s1.handleDiscount(or1),expectedBeforeDiscount);
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity);
        or1 = new OrderController().createNewOrder(worker,cart, storeCtrl.calculatePrice(cart));
        assertEquals(expectedAfterDiscount,s1.handleDiscount(or1));
    }
    //• xor:" הנחה על מוצרי חלב או על מאפים, אבל לא שתיהן . בתוספת כלל להכרעה" .
    @Test
    public void xorLogicalCompositeTest() throws Exception {
        int percentage1 = 50;
        int percentage2 = 30;
        int prod1Quantity = 2;
        int prod3Quantity = 1;
        double expectedPrice = 90;
        DiscountDataObject dis1 = new DiscountDataObject(percentage1, AbstractDiscount.discountTypes.Category,0,Bananas,null);
        DiscountDataObject dis2 = new DiscountDataObject(percentage2, AbstractDiscount.discountTypes.Category,0,Electronics,null);
        CompositeDataObject composite = new CompositeDataObject(percentage1,null, LogicalDiscountComposite.logical.Xor, LogicalDiscountComposite.xorDecidingRules.MaxDiscountValue,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite,"", null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1Quantity),prod1Quantity); //price = 20 -> 10 after
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity); //price = 100 -> 70 after so this has max value

        Order order = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));

        assertEquals(expectedPrice,s1.handleDiscount(order));
    }


    // יש הנחת מאפים של 10% על לחמניות ולחם רק אם הסל מכיל לפחות 5
    //לחמניות וגם לפחות 2 כיכרות לחם "
    @Test
    public void andLogicalCompositeTest() throws Exception{
        String minNumOfItemParams1 = "0 5";
        String minNumOfItemParams2 = "2 2";
        int percentage = 10;
        int prod1QuantityInitial = 4;
        int prod1AddedQuantity = 1;
        int prod3Quantity = 2;
        double expectedPriceBefore = 240;
        double expectedPriceAfter = 225;

        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemParams1,null);
        DiscountDataObject dis1 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Product,s1Product1ID,"",new ArrayList<>(Arrays.asList(pred1)));
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemParams2,null);
        DiscountDataObject dis2 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Product,s1Product3ID,"",new ArrayList<>(Arrays.asList(pred2)));
        CompositeDataObject composite = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.And, null,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite,"", null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1QuantityInitial),prod1QuantityInitial);
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity);

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedPriceBefore,s1.handleDiscount(or)); //no discount because it doesnt uphold all predicates

        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1AddedQuantity),prod1AddedQuantity);
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        double price = s1.handleDiscount(or);
        assertEquals(expectedPriceAfter,price);
    }

    // יש 5% הנחה על מוצרי חלב אם הסל מכיל לפחות 3 גביעי קוטג' או לפחות 2
    //יוגורטים ".
    @Test
    public void orLogicalCompositeTest() throws Exception{
        String minNumOfItemParams1 = "0 3";
        String minNumOfItemParams2 = "1 2";
        int percentage = 10;
        int quantity = 2;
        int addedQuantity = 1;
        double expectedPrice1 = 54;
        double expectedPrice2 = 56;
        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemParams1,null);
        DiscountDataObject dis1 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Category,s1Product1ID,Bananas,new ArrayList<>(Arrays.asList(pred1)));
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemParams2,null);
        DiscountDataObject dis2 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Category,s1Product2ID,Bananas,new ArrayList<>(Arrays.asList(pred2)));
        CompositeDataObject composite = new CompositeDataObject(50,null, LogicalDiscountComposite.logical.Or, null,new ArrayList<>(Arrays.asList(dis1,dis2)),null);
        s1.addDiscount(composite,"", null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),quantity),quantity);
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),quantity),quantity);

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedPrice1,s1.handleDiscount(or));

        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),addedQuantity),addedQuantity);
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedPrice2,s1.handleDiscount(or));
    }
    //"אם שוו
    //הסל גבוה מ 100₪- וגם הסל מכיל לפחות 3 חבילות פסטה, אז יש הנחה של 5%
    //על מוצרי חלב.
    @Test
    public void predComposeTest() throws Exception {
        String minPriceParams = "100";
        String minNumOfItemParams = "0 3";
        int percentage = 10;
        int prod1InitialQuantity = 2;
        int prod1AdditionalQuantity = 1;
        int prod3Quantity = 1;
        double expectedBeforeDiscount = 120;
        double expectedAfterDiscount = 127; //added item
        PredicateDataObject pred1 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinPrice,minPriceParams, null);
        PredicateDataObject pred2 = new PredicateDataObject(DiscountPredicate.PredicateTypes.MinNumOfItem,minNumOfItemParams,And);
        DiscountDataObject dis = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Category,0,Bananas,new ArrayList<>(Arrays.asList(pred1,pred2)));
        s1.addDiscount(dis,content.toString(), null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1InitialQuantity),prod1InitialQuantity); //20
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity); //100

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedBeforeDiscount,s1.handleDiscount(or));

        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1AdditionalQuantity),prod1AdditionalQuantity); //10
        or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedAfterDiscount,s1.handleDiscount(or));

    }
    //ההנחה הניתנת היא המקס' מבין הערך בש"ח של 5% מעלות כל חבילות
    //הפסטה בסל לבין הערך בש"ח של 17% מעלות קרטוני החלב בסל."
    @Test
    public void maxNumericCompositeTest() throws Exception {
        int percentage = 10;
        int prod1Quantity = 3;
        int prod2Quantity = 1;
        double expectedPriceAfterDiscount = 47;

        DiscountDataObject d1 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Product,s1Product1ID,"",null);
        DiscountDataObject d2 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Product,s1Product2ID,"",null);
        CompositeDataObject comp = new CompositeDataObject(percentage, NumericDiscountComposite.numeric.Max,null,null,new ArrayList<>(Arrays.asList(d1,d2)),null);
        s1.addDiscount(comp,"", null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1Quantity),prod1Quantity); //30
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product2ID),prod2Quantity),prod2Quantity); //20

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedPriceAfterDiscount,s1.handleDiscount(or));
    }

    // יש 5% הנחה על מוצרי חלב, ובנוסף, יש 20% הנחה על כל חנות לכן על
    //מוצרי חלב יש בסה"כ 25% הנחה".
    @Test
    public void additionNumericCompositeTest() throws Exception {
        int percentage = 10;
        int prod1Quantity = 3;
        int prod3Quantity = 1;
        double expectedPriceAfterDiscount = 114;

        DiscountDataObject d1 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Category,s1Product1ID,"Bananas",null);
        DiscountDataObject d2 = new DiscountDataObject(percentage, AbstractDiscount.discountTypes.Store,s1Product2ID,"",null);
        CompositeDataObject comp = new CompositeDataObject(percentage, NumericDiscountComposite.numeric.Addition,null,null,new ArrayList<>(Arrays.asList(d1,d2)),null);
        s1.addDiscount(comp,"", null);

        ShoppingCart cart = new ShoppingCart();
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product1ID),prod1Quantity),prod1Quantity); //30 ->27 ->
        cart.changeQuantityInCart(s1.getStoreId(),new ProductInfo(s1.getStoreId(),inv1.getProduct(s1Product3ID),prod3Quantity),prod3Quantity); //100 ->100 ->90

        Order or = new OrderController().createNewOrder(worker,cart,storeCtrl.calculatePrice(cart));
        assertEquals(expectedPriceAfterDiscount,s1.handleDiscount(or));
    }
}
