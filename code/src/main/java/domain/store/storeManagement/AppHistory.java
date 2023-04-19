package domain.store.storeManagement;

import utils.Pair;
import utils.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class AppHistory {
    public static class Node{
        Pair<Integer, Role> data; //userid and role
        ArrayList<Node> children; //list of all the users this user appoint in this store

        Set<Integer> dismissed;
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

    public AppHistory(Pair<Integer, Role> creatorNode){

        root = new Node(creatorNode);
    }
    public boolean addNode(Integer father, Pair<Integer, Role> child) throws Exception {
        Node childNode = root.findNode(child.getFirst());
        Node fatherNode = root.findNode(father);
        if (childNode != null)
        {
            throw new Exception("user already have a role in the store");
        }
        if (fatherNode == null)
        {
            throw new Exception("User cant appoint other users in the store");
        }
        fatherNode.addChild(child);
        return true;
    }

    public Set<Integer> removeChild(Integer userId) throws Exception {
       Node childNode = root.findNode(userId);
       if (childNode == null)
       {
           throw new Exception("user isn't part of the store");
       }
       this.root.dismissed.clear();
       return root.deleteNode(childNode.data);
    }

    public boolean isChild(Integer father, Integer child)
    {
        return getNode(father).children.contains(getNode(child));
    }

    public Node getNode(Integer userId)
    {
        return root.findNode(userId);
    }
}
