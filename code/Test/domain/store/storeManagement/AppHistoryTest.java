package domain.store.storeManagement;

import datastructres.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Role;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppHistoryTest {
    static Pair<Integer, Role> node0;
    static Pair<Integer, Role> node1;
    static Pair<Integer, Role> node2;
    static Pair<Integer, Role> node3;
    static Pair<Integer, Role> node4;
    static Pair<Integer, Role> node5;
    static AppHistory root;
    @BeforeAll
    public static void setup()
    {
        node0 = new Pair<>(0, Role.Creator);
        node1 = new Pair<>(1, Role.Owner);
        node2 = new Pair<>(2, Role.Manager);
        node3 = new Pair<>(3, Role.Manager);
        node4 = new Pair<>(4, Role.Owner);
        node5 = new Pair<>(5, Role.Owner);
        root = new AppHistory(node0);
    }

    @Test
    void getNodeExists()
    {
        Assertions.assertNotNull(root.getNode(0));

    }

    @Test
    void getNodeNotExists()
    {
        Assertions.assertNull(root.getNode(2));

    }
    @Test
    void addDuplicateNode() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            root.addNode(node0.getFirst(), node1);
            root.addNode(node0.getFirst(), node1);
        });
        String expectedMessage = "user already have a role in the store";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void addChildToNonExistentNode() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> {
            root.addNode(node5.getFirst(), node2);
        });
        String expectedMessage = "User cant appoint other users in the store";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void removeChildNotInTree() {
        Exception exception = assertThrows(Exception.class, () -> {
            root.removeChild(6);
        });
        String expectedMessage = "user isn't part of the store";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void removeNodeWithDescendants() throws Exception {
        root.addNode(node0.getFirst(), node1);
        root.addNode(node1.getFirst(), node2);
        root.addNode(node2.getFirst(), node3);
        ArrayList<Integer> dismissed = root.removeChild(node1.getFirst());
        assertTrue(dismissed.contains(node2.getFirst()));
        assertTrue(dismissed.contains(node3.getFirst()));
        dismissed.clear();
    }
    @Test
    void removeLeafNode() throws Exception {
        root.addNode(node0.getFirst(), node3);
        ArrayList<Integer> dismissed = root.removeChild(node3.getFirst());
        assertTrue(dismissed.contains(3));
    }
    @AfterAll
    static void tearDown() {

        root = null;
    }

}