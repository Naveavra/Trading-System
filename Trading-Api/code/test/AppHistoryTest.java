
import domain.store.storeManagement.AppHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.stateRelated.Role;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


    class AppHistoryTest {
        static Pair<Integer, Role> node0;
        static Pair<Integer, Role> node1;
        static Pair<Integer, Role> node2;
        static Pair<Integer, Role> node3;
        static Pair<Integer, Role> node4;
        static Pair<Integer, Role> node5;
        static AppHistory root;

        @BeforeEach
        public void setup() {
            node0 = new Pair<>(0, Role.Creator);
            node1 = new Pair<>(1, Role.Owner);
            node2 = new Pair<>(2, Role.Manager);
            node3 = new Pair<>(3, Role.Manager);
            node4 = new Pair<>(4, Role.Owner);
            node5 = new Pair<>(5, Role.Owner);
            root = new AppHistory(node0);
        }

        @Test
        void getNodeExists() {
            Assertions.assertNotNull(root.getNode(0));

        }

        @Test
        void circularAppointment() throws Exception {
            AppHistory appHistory = new AppHistory(new Pair<>(1, Role.Owner));

            Exception exception = assertThrows(Exception.class, () -> {
                appHistory.addNode(1, new Pair<>(2, Role.Manager));
                appHistory.addNode(2, new Pair<>(3, Role.Manager));
                appHistory.addNode(3, new Pair<>(4, Role.Manager));
                appHistory.addNode(4, new Pair<>(2, Role.Owner));
            });
            String expectedMessage = "circular appointment";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testGetNode() throws Exception {
            AppHistory appHistory = new AppHistory(new Pair<>(1, Role.Owner));
            appHistory.addNode(1, new Pair<>(2, Role.Manager));
            AppHistory.Node node = appHistory.getNode(2);
            assertNotNull(node);
            assertEquals(node.getData().getFirst(), 2);
            assertEquals(node.getData().getSecond(), Role.Manager);
        }


        @Test
        void getNodeNotExists() {
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
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst());
            assertTrue(dismissed.contains(node2.getFirst()));
            assertTrue(dismissed.contains(node3.getFirst()));
            dismissed.clear();
        }

        @Test
        void testRemoveChild() throws Exception {
            AppHistory appHistory = new AppHistory(new Pair<>(1, Role.Owner));
            appHistory.addNode(1, new Pair<>(2, Role.Manager));
            appHistory.removeChild(2);
            assertFalse(appHistory.isChild(1, 2));
        }

        @Test
        void testIsChild() throws Exception {
            AppHistory appHistory = new AppHistory(new Pair<>(1, Role.Owner));
            appHistory.addNode(1, new Pair<>(2, Role.Manager));
            assertTrue(appHistory.isChild(1, 2));
            assertFalse(appHistory.isChild(2, 1));
        }

        @Test
        void removeLeafNode() throws Exception {
            root.addNode(node0.getFirst(), node3);
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node3.getFirst());
            assertTrue(dismissed.contains(3));
        }

        @Test
        void testRemoveNonLeafNode() throws Exception {
            root.addNode(node0.getFirst(), node1);
            root.addNode(node1.getFirst(), node2);
            root.addNode(node2.getFirst(), node3);
            assertTrue(root.isChild(node0.getFirst(), node1.getFirst()));
            assertTrue(root.isChild(node1.getFirst(), node2.getFirst()));
            assertTrue(root.isChild(node2.getFirst(), node3.getFirst()));
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst());
            assertTrue(dismissed.contains(node1.getFirst()));
            assertTrue(dismissed.contains(node2.getFirst()));
            assertTrue(dismissed.contains(node3.getFirst()));
            assertFalse(root.isChild(node0.getFirst(), node1.getFirst()));
            assertFalse(root.isChild(node1.getFirst(), node2.getFirst()));
            assertFalse(root.isChild(node2.getFirst(), node3.getFirst()));
        }

        @Test
        void testRemoveRootNode() {
            Exception exception = assertThrows(Exception.class, () -> {
                root.removeChild(node0.getFirst());
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
            root.addNode(node0.getFirst(), node1);
            assertTrue(root.isChild(node0.getFirst(), node1.getFirst()));
            HashSet<Integer> dismissed = (HashSet<Integer>) root.removeChild(node1.getFirst());
            assertTrue(dismissed.contains(node1.getFirst()));
            assertFalse(root.isChild(node0.getFirst(), node1.getFirst()));
        }

        @Test
        void testAddMultipleNodesToParent() throws Exception {
            root.addNode(node0.getFirst(), node1);
            root.addNode(node0.getFirst(), node2);
            assertTrue(root.isChild(node0.getFirst(), node1.getFirst()));
            assertTrue(root.isChild(node0.getFirst(), node2.getFirst()));
        }



}
