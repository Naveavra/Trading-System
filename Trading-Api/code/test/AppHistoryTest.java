
import database.Dao;
import domain.states.StoreCreator;
import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.states.UserState;
import domain.store.storeManagement.AppHistory;
import domain.user.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.stateRelated.Role;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


    class AppHistoryTest {
        static Pair<Member, UserState> node0;
        static Pair<Member, UserState> node1;
        static Pair<Member, UserState> node2;
        static Pair<Member, UserState> node3;
        static Pair<Member, UserState> node4;
        static Pair<Member, UserState> node5;
        static AppHistory root;
        Member m0 = new Member("eli@gmail.com", "123Aaa", "24/02/2002");
        Member m1 = new Member("eli2@gmail.com", "123Aaa", "24/02/2002");
        Member m2 = new Member("eli3@gmail.com", "123Aaa", "24/02/2002");
        Member m3 = new Member("eli4@gmail.com", "123Aaa", "24/02/2002");
        Member m4 = new Member("eli5@gmail.com", "123Aaa", "24/02/2002");
        Member m5 = new Member("eli6@gmail.com", "123Aaa", "24/02/2002");
        @BeforeEach
        public void setup() {
            Dao.setForTests(true);
            node0 = new Pair<>(m0, new StoreCreator(m0.getId(), m0.getName(), null));
            node1 = new Pair<>(m1, new StoreOwner(m1.getId(), m1.getName(), null));
            node2 = new Pair<>(m2, new StoreManager(m2.getId(), m2.getName(), null));
            node3 = new Pair<>(m3, new StoreManager(m3.getId(), m3.getName(), null));
            node4 = new Pair<>(m4, new StoreOwner(m4.getId(), m4.getName(), null));
            node5 = new Pair<>(m5, new StoreOwner(m5.getId(), m5.getName(), null));
            root = new AppHistory(0, node0);
        }

        @Test
        void getNodeExists() {
            Assertions.assertNotNull(root.getNode(m0.getId()));

        }

        @Test
        void circularAppointment() throws Exception {
            AppHistory appHistory = new AppHistory(0, new Pair<>(m1, new StoreOwner(m1.getId(), m1.getName(), null)));

            Exception exception = assertThrows(Exception.class, () -> {
                appHistory.addNode(m1.getId(), new Pair<>(m2, new StoreManager(m2.getId(), m2.getName(), null)));
                appHistory.addNode(m2.getId(), new Pair<>(m3, new StoreManager(m3.getId(), m3.getName(), null)));
                appHistory.addNode(m3.getId(), new Pair<>(m4, new StoreManager(m4.getId(), m4.getName(), null)));
                appHistory.addNode(m4.getId(), new Pair<>(m5, new StoreOwner(m5.getId(), m5.getName(), null)));
                appHistory.addNode(m5.getId(), new Pair<>(m1, new StoreManager(m1.getId(), m1.getName(), null)));
            });
            String expectedMessage = "circular appointment";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testGetNode() throws Exception {
            AppHistory appHistory = new AppHistory(0, new Pair<>(m1, new StoreOwner(m1.getId(), m1.getName(), null)));
            appHistory.addNode(m1.getId(), new Pair<>(m2, new StoreManager(m2.getId(), m2.getName(), null)));
            AppHistory.Node node = appHistory.getNode(m2.getId());
            assertNotNull(node);
            assertEquals(node.getData().getFirst().getId(), m2.getId());
            assertEquals(node.getData().getSecond().getRole(), Role.Manager);
        }


        @Test
        void getNodeNotExists() {
            Assertions.assertNull(root.getNode(2));

        }

        @Test
        void addDuplicateNode() throws Exception {
            Exception exception = assertThrows(Exception.class, () -> {
                root.addNode(node0.getFirst().getId(), node1);
                root.addNode(node0.getFirst().getId(), node1);
            });
            String expectedMessage = "user already have a role in the store";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }


        @Test
        void addChildToNonExistentNode() throws Exception {
            Exception exception = assertThrows(Exception.class, () -> {
                root.addNode(node5.getFirst().getId(), node2);
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
            root.addNode(node0.getFirst().getId(), node1);
            root.addNode(node1.getFirst().getId(), node2);
            root.addNode(node2.getFirst().getId(), node3);
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst().getId());
            assertTrue(dismissed.contains(node2.getFirst().getId()));
            assertTrue(dismissed.contains(node3.getFirst().getId()));
            dismissed.clear();
        }

        @Test
        void testRemoveChild() throws Exception {
            AppHistory appHistory = new AppHistory(0, new Pair<>(m1, new StoreOwner(m1.getId(), m1.getName(), null)));
            appHistory.addNode(m1.getId(), new Pair<>(m2, new StoreManager(m2.getId(), m2.getName(), null)));
            appHistory.removeChild(m2.getId());
            assertFalse(appHistory.isChild(m1.getId(), m2.getId()));
        }

        @Test
        void testIsChild() throws Exception {
            AppHistory appHistory = new AppHistory(0, new Pair<>(m1, new StoreOwner(m1.getId(), m1.getName(), null)));
            appHistory.addNode(m1.getId(), new Pair<>(m2, new StoreManager(m2.getId(), m2.getName(), null)));
            assertTrue(appHistory.isChild(m1.getId(), m2.getId()));
            assertFalse(appHistory.isChild(m2.getId(), m1.getId()));
        }

        @Test
        void removeLeafNode() throws Exception {
            root.addNode(node0.getFirst().getId(), node3);
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node3.getFirst().getId());
            assertTrue(dismissed.contains(m3.getId()));
        }

        @Test
        void testRemoveNonLeafNode() throws Exception {
            root.addNode(node0.getFirst().getId(), node1);
            root.addNode(node1.getFirst().getId(), node2);
            root.addNode(node2.getFirst().getId(), node3);
            assertTrue(root.isChild(node0.getFirst().getId(), node1.getFirst().getId()));
            assertTrue(root.isChild(node1.getFirst().getId(), node2.getFirst().getId()));
            assertTrue(root.isChild(node2.getFirst().getId(), node3.getFirst().getId()));
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst().getId());
            assertTrue(dismissed.contains(node1.getFirst().getId()));
            assertTrue(dismissed.contains(node2.getFirst().getId()));
            assertTrue(dismissed.contains(node3.getFirst().getId()));
            assertFalse(root.isChild(node0.getFirst().getId(), node1.getFirst().getId()));
            assertFalse(root.isChild(node1.getFirst().getId(), node2.getFirst().getId()));
            assertFalse(root.isChild(node2.getFirst().getId(), node3.getFirst().getId()));
        }

        @Test
        void testRemoveRootNode() {
            Exception exception = assertThrows(Exception.class, () -> {
                root.removeChild(node0.getFirst().getId());
            });
            String expectedMessage = "Cannot remove store creator";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }

//    @Test
//    void testAddNodeToLeafNode() throws Exception {
//        root.addNode(node0.getFirst(), node1);
//        root.addNode(node1.getFirst(), node2);
//        Exception exception = assertThrows(Exception.class, () -> {
//            root.addNode(node2.getFirst(), node3);
//        });
//        String expectedMessage = "Cannot add child to a leaf node";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }

        @Test
        void testRemoveNodeWithNoChildren() throws Exception {
            root.addNode(node0.getFirst().getId(), node1);
            assertTrue(root.isChild(node0.getFirst().getId(), node1.getFirst().getId()));
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst().getId());
            assertTrue(dismissed.contains(node1.getFirst().getId()));
            assertFalse(root.isChild(node0.getFirst().getId(), node1.getFirst().getId()));
        }

        @Test
        void testAddMultipleNodesToParent() throws Exception {
            root.addNode(node0.getFirst().getId(), node1);
            root.addNode(node0.getFirst().getId(), node2);
            assertTrue(root.isChild(node0.getFirst().getId(), node1.getFirst().getId()));
            assertTrue(root.isChild(node0.getFirst().getId(), node2.getFirst().getId()));
        }



}
