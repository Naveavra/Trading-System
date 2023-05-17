package domain.store.storeManagement;
import domain.user.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;
import utils.orderRelated.Order;
import utils.stateRelated.Role;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreTest {
    Store store;
    Member member;

    Order orderA, orderB;


    @BeforeEach
    void setUp() throws Exception {
        store = new Store(1, "candy shop", 0);
        store.addNewProduct("gum", "gumigun", new AtomicInteger(0), 10, 3);
        store.getInventory().getProduct(0).replaceQuantity(10);
        store.addNewProduct("coke", "diet", new AtomicInteger(1), 10, 3);
        store.getInventory().getProduct(1).replaceQuantity(10);
        store.appointUser(0, 1, Role.Manager);
        store.appointUser(1, 2, Role.Manager);
        member = new Member(2, "lala@gmail.com", "aA12345", "31/08/2022");
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 5);
        map.put(1, 10);
        HashMap<Integer, HashMap<Integer, Integer>> map2 = new HashMap<>();
        map2.put(1, map);
        orderA = new Order(0, 2, map2);
        orderB = new Order(1,2,map2);
        store.addOrder(orderA);
        store.addOrder(orderB);
//        store.appointUser(0, 2, Role.Manager);
//        store.appointUser(2, 4, Role.Manager);

    }
    @Test
    void getStoreRating() throws Exception {
        Message review = new Message(0, "great store", member, 0, 0, MessageState.reviewStore);
        Message reviewB = new Message(1, "shitty store", member, 1, 0, MessageState.reviewStore);
        review.addRating(5);
        reviewB.addRating(1);
        store.addReview(0, review);
        store.addReview(1, reviewB);
        double rating = store.getStoreRating();
        assertEquals(3, rating, "failed to get store rating");
    }

    @Test
    void invalidAddReview() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            Message review = new Message(0, "great store", member, 3, 0, MessageState.reviewStore);
            review.addRating(5);
            store.addReview(3, review);
        });
        String expectedMessage = "order doesnt exist";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "order doesnt exist so the test should fail");
    }

    @Test
    void getAppHistorySuccess() {
        Set<Integer> set = new HashSet<>();
        set.add(0);
        set.add(1);
        set.add(2);
        assertEquals(set, store.getUsersInStore(), "3 users in the store");
    }

    @Test
    void fireUser() throws Exception {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        Set<Integer> firedUsers = store.fireUser(1);
        assertEquals(set,firedUsers, "we fired user 1 and he appointed user 2 so he to should be fired");
    }

    @Test
    void closeStoreByNonCreatorFail() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            store.closeStoreTemporary(2);
        });
        String expectedMessage = "user isn't authorized to close this store";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "should fail because the user isn't the store creator");
    }

    @Test
    void closeStoreByCreatorSuccess() throws Exception {
        store.closeStoreTemporary(0);
        assertFalse(store.isActive(), "store has been closed by the creator");
    }

    @Test
    void createOrderSuccess() throws Exception {
        HashMap<Integer, Integer> basket = new HashMap<>();
        basket.put(0, 5);
        basket.put(1, 5);
        int expectedPrice = 100;
//        int actualPrice = store.createOrder(basket);
//        assertEquals(expectedPrice, actualPrice);
        store.makeOrder(basket);
        assertEquals(5, store.getInventory().getProduct(0).quantity, "total was 10 user bought 5");
        assertEquals(5, store.getInventory().getProduct(1).quantity, "total was 10 user bought 5");
    }

    @Test
    void createOrderFailNotEnoughUnits() throws Exception {
        HashMap<Integer, Integer> basket = new HashMap<>();
        basket.put(0, 5);
        basket.put(1, 11);
        assertFalse(store.makeOrder(basket), "store quantity is 10, user wanted 11");
    }

    @Test
    void createOrderFailNotEnoughUnitsAfterPurchasing() throws Exception {
        HashMap<Integer, Integer> basket = new HashMap<>();
        basket.put(0, 5);
        basket.put(1, 9);
        //store.createOrder(basket);
        store.makeOrder(basket);
        HashMap<Integer, Integer> basket2 = new HashMap<>();
        basket2.put(1, 3);
        assertFalse(store.makeOrder(basket2), "store quantity is 1, user wanted 3");
    }

    @Test
    void createOrderFailProductNotExist() throws Exception {
        HashMap<Integer, Integer> basket = new HashMap<>();
        basket.put(0, 5);
        basket.put(2, 11);
        Exception exception = assertThrows(Exception.class, () -> {
            store.makeOrder(basket);
        });
        String expectedMessage = "Product not found, ID: 2";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getMessages() throws Exception {
        Message review = new Message(0, "great store", member, 0, 0, MessageState.reviewStore);
        Message reviewB = new Message(1, "shitty store", member, 1, 0, MessageState.reviewStore);
        review.addRating(5);
        reviewB.addRating(1);
        store.addReview(0, review);
        store.addReview(1, reviewB);
        ArrayList<String> actualMessages = store.checkMessages();
        ArrayList<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("great store");
        expectedMessages.add("shitty store");
        assertEquals(expectedMessages, actualMessages);
        assertEquals(0, store.checkMessages().size(), "the messages already been seen");
    }









}