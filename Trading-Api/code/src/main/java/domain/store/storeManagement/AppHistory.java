package domain.store.storeManagement;

import utils.Pair;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class AppHistory {
    public static class Node{
        private Pair<Integer, Role> data; //userid and role
        private ArrayList<Node> children; //list of all the users this user appoint in this store

        private Set<Integer> dismissed;
        public Node(Pair<Integer, Role> appointment){
            //Assign data to the new node, set left and right children to null
            this.data = appointment;
            this.children = new ArrayList<Node>();
            this.dismissed = new HashSet<>();
        }
        private Node findNode(Integer userId)
        {
            if (Objects.equals(data.getFirst(), userId))
            {
                return this;   //meaning this user is already part of the store
            }
            for (Node child : children)
            {
                Node node = child.findNode(userId);
                if (node != null)
                {
                    return node;
                }
            }
            return null;
        }

        //added for tests
        public Pair<Integer, Role> getData(){
            return data;
        }

        private void addChild(Pair<Integer, Role> child)
        {
            children.add(new Node((child)));
        }

        //remove the node and all of his descendants
        private Set<Integer>  deleteNode(Pair<Integer, Role> nodeData) {
            Node node = findNode(nodeData.getFirst());
            if (node == null) {
                return null; // node not found
            }
            dismissed.add(node.data.getFirst());
            for (Node child : node.children) {
                dismissed.addAll(Objects.requireNonNull(deleteNode(child.data)));
            }
            children.remove(node);
            return dismissed;
        }
    }
    //Represent the root of binary tree
    public Node root;

    public Set<Integer> usersInStore;

    public AppHistory(Pair<Integer, Role> creatorNode){

        root = new Node(creatorNode);
        usersInStore = new HashSet<>();
        usersInStore.add(creatorNode.getFirst());
    }
    public boolean addNode(Integer father, Pair<Integer, Role> child) throws Exception {
        Node childNode = root.findNode(child.getFirst());
        Node fatherNode = root.findNode(father);
        if (childNode != null)
        {
            appointToNewRole(father, child);
        }
        if (fatherNode == null)
        {
            throw new Exception("User cant appoint other users in the store");
        }
        fatherNode.addChild(child);
        usersInStore.add(child.getFirst());
        return true;
    }

    public Set<Integer> removeChild(Integer userId) throws Exception {
       Node childNode = root.findNode(userId);
       if (childNode == null)
       {
           throw new Exception("user isn't part of the store");
       }
       if (Objects.equals(childNode.data.getFirst(), root.data.getFirst()))
       {
           throw new Exception("Cannot remove store creator");
       }
       this.root.dismissed.clear();
        Set<Integer> dismissedes = new HashSet<>();
        dismissedes.add(userId);
        dismissedes.addAll(Objects.requireNonNull(root.deleteNode(childNode.data)));
        usersInStore.removeAll(dismissedes);
        return dismissedes;
    }

    public Set<Integer> getUsers()
    {
        return usersInStore;
    }

    public boolean isChild(Integer father, Integer child)
    {
        Node node = getNode(father);
        if (node != null) {
            return getNode(father).children.contains(getNode(child));
        }
        return false;
    }

    public Node getNode(Integer userId)
    {
        return root.findNode(userId);
    }

    public void appointToNewRole(Integer fatherid, Pair<Integer, Role> child) throws Exception {
        Node childNode = root.findNode(child.getFirst());
        if (childNode == null) {
            throw new Exception("child node not found");
        }
        Node fatherNode = root.findNode(fatherid);
        if (fatherNode == null) {
            throw new Exception("father node not found");
        }
        if (childNode == root) {
            throw new Exception("Cannot move the root node");
        }
        if (isChild(child.getFirst(), fatherid)) {
            throw new Exception("Cannot move a node to its own child");
        }
        childNode.data.setSecond(child.getSecond());
        Set<Integer> dismissedes = new HashSet<>(Objects.requireNonNull(root.deleteNode(childNode.data)));
        fatherNode.addChild(childNode.data);
        dismissedes.remove(child.getFirst());
        usersInStore.removeAll(dismissedes);
    }
}