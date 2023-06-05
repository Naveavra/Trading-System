package domain.store.storeManagement;
import domain.states.StoreManager;
import domain.user.Basket;
import domain.user.Member;
import domain.user.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Message;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.messageRelated.MessageState;
import utils.orderRelated.Order;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreTest {
    Store store;
    Member member;

    int orderA_Id=0, orderB_Id=1;

    Order orderA, orderB;
    ProductInfo p, p2;
    Member creator = new Member(0, "eli@gmail.com", "123Aaa", "24/02/2002");
    Member worker = new Member(1, "eli1@gmail.com", "123Aaa", "24/02/2002");
    Member worker2 = new Member(2, "eli2@gmail.com", "123Aaa", "24/02/2002");

    @BeforeEach
    void setUp() throws Exception {
        store = new Store(1, "candy shop", creator);
        store.addNewProduct("gum", "gumigun", new AtomicInteger(0), 10, 3);
        store.getInventory().getProduct(0).replaceQuantity(10);
        store.addNewProduct("coke", "diet", new AtomicInteger(1), 10, 3);
        store.getInventory().getProduct(1).replaceQuantity(10);
        store.appointUser(0, worker, new StoreManager(worker.getId(), worker.getName(), null));
        store.appointUser(1, worker2, new StoreManager(worker2.getId(), worker2.getName(), null));
        member = new Member(2, "lala@gmail.com", "aA12345", "31/08/2022");
        ShoppingCart cart = new ShoppingCart();
        p = new ProductInfo(store.getStoreId(), store.getInventory().getProduct(0), 10);
        p2 = new ProductInfo(store.getStoreId(), store.getInventory().getProduct(1), 10);
        cart.addProductToCart(1, p, 5);
        cart.addProductToCart(1, p2, 10);
        orderA = new Order(0, worker2, cart);
        orderB = new Order(1,worker2,cart);
        store.addOrder(orderA);
        store.addOrder(orderB);
//        ShoppingCart mockCart = mock(ShoppingCart.class);
//        Basket mockBasket = mock(Basket.class);


    }
    @Test
    void getStoreRating() throws Exception {
        Message review = new Message(0, NotificationOpcode.STORE_REVIEW, "great store", member, 0, MessageState.reviewStore);
        review.addStore(0);
        Message reviewB = new Message(1, NotificationOpcode.STORE_REVIEW, "shitty store", member, 1, MessageState.reviewStore);
        review.addStore(0);
        review.addRating(5);
        reviewB.addRating(1);
        store.addReview(orderA_Id, review);
        store.addReview(orderB_Id, reviewB);
        double rating = store.getStoreRating();
        int expectedRating = 3; //the rating supposed to be the average number of all ratings
        assertEquals(expectedRating, rating, "failed to get store rating");
    }

    @Test
    void invalidAddReview() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            Message review = new Message(0, NotificationOpcode.STORE_REVIEW, "great store", member, 3, MessageState.reviewStore);
            review.addStore(store.getStoreId());
            review.addRating(5);
            int wrongOrderId = 3;
            store.addReview(wrongOrderId, review);
        });
        String expectedMessage = "order doesnt exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "order doesnt exist so the test should fail");
    }

    @Test
    void getAppHistorySuccess() {
        Set<Integer> set = new HashSet<>();
        set.add(creator.getId());
        set.add(worker.getId());
        set.add(worker2.getId());
        assertEquals(set, store.getUsersInStore(), "3 users in the store");
    }

    @Test
    void fireUser() throws Exception {
        Set<Integer> set = new HashSet<>();
        set.add(worker.getId());
        set.add(worker2.getId());
        Set<Integer> firedUsers = store.fireUser(worker.getId());
        assertEquals(set,firedUsers, "we fired user 1 and he appointed user 2 so he to should be fired");
    }

    @Test
    void closeStoreByNonCreatorFail() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            store.closeStoreTemporary(worker2.getId());
        });
        String expectedMessage = "user isn't authorized to close this store";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "should fail because the user isn't the store creator");
    }

    @Test
    void closeStoreByCreatorSuccess() throws Exception {
        store.closeStoreTemporary(creator.getId());
        assertFalse(store.isActive(), "store has been closed by the creator");
    }

    @Test
    void createOrderSuccess() throws Exception {
        Basket basket = new Basket(store.getStoreId());
        basket.addProductToCart(p, 5);
        basket.addProductToCart(p2, 5);
        store.makeOrder(basket);
        int expectedQuantity = 5; //original quantity before purchasing was 10 we bought 5 10-5=5
        assertEquals(expectedQuantity, store.getInventory().getProduct(p.id).quantity, "total was 10 user bought 5");
        assertEquals(expectedQuantity, store.getInventory().getProduct(p2.id).quantity, "total was 10 user bought 5");
    }

    @Test
    void createOrderFailNotEnoughUnits() throws Exception {
        Basket basket = new Basket(store.getStoreId());
        int originalQuantity = 10;
        basket.addProductToCart(p, 5);
        basket.addProductToCart(p2, 11);
        assertFalse(store.makeOrder(basket), "store quantity is 10, user wanted 11");
        assertEquals(originalQuantity, store.getInventory().getQuantity(p2.getId()), "the order failed so the quantity" +
                "should remain the same");
    }

    @Test
    void createOrderFailNotEnoughUnitsAfterPurchasing() throws Exception {
        Basket basket = new Basket(store.getStoreId());
        basket.addProductToCart(p, 5);
        basket.addProductToCart(p2, 9);
        //store.createOrder(basket);
        store.makeOrder(basket);
        Basket basket2 = new Basket(store.getStoreId());
        basket2.addProductToCart(p2, 2);
        assertFalse(store.makeOrder(basket2), "store quantity is 1, user wanted 3");
    }

    @Test
    void createOrderFailProductNotExist() throws Exception {
        Basket basket = new Basket(store.getStoreId());
        basket.addProductToCart(p, 5);
        ProductInfo p3 =  new ProductInfo(store.getStoreId(), store.getInventory().getProduct(1), 11);
        p3.id = 2;
        basket.addProductToCart(p3, 11);
        Exception exception = assertThrows(Exception.class, () -> {
            store.makeOrder(basket);
        });
        String expectedMessage = "Product not found, ID: 2";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getMessages() throws Exception {
        Message review = new Message(0, NotificationOpcode.STORE_REVIEW, "great store", member, 0, MessageState.reviewStore);
        review.addStore(0);
        Message reviewB = new Message(1, NotificationOpcode.STORE_REVIEW, "shitty store", member, 1, MessageState.reviewStore);
        review.addStore(0);
        review.addRating(5);
        reviewB.addRating(1);
        store.addReview(orderA_Id, review);
        store.addReview(orderB_Id, reviewB);
        ArrayList<String> actualMessages = store.checkMessages();
        ArrayList<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("great store");
        expectedMessages.add("shitty store");
        assertEquals(expectedMessages, actualMessages);
        assertEquals(0, store.checkMessages().size(), "the messages already been seen");
    }


}