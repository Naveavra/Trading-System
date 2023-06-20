package domain.store.storeManagement;

import database.DbEntity;
import domain.states.UserState;
import domain.user.Member;
import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.Info;
import utils.infoRelated.Information;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.*;


public class AppHistory{
    private int storeId;



    public static class Node{
        private Pair<Member, UserState> data; //userid and role
        private ArrayList<Node> children; //list of all the users this user appoint in this store
        private Set<Integer> dismissed;
        private Node father;
        public Node(Pair<Member, UserState> appointment){
            //Assign data to the new node, set left and right children to null
            this.data = appointment;
            this.children = new ArrayList<Node>();
            this.dismissed = new HashSet<>();
        }
        private Node findNode(Integer userId)
        {
            if (Objects.equals(data.getFirst().getId(), userId))
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

        public void setFather(Node n){father=n;}

        //added for tests
        public Pair<Member, UserState> getData(){
            return data;
        }

        private void addChild(Pair<Member, UserState> child)
        {
            Node childNode = new Node(child);
            childNode.setFather(this);
            children.add(childNode);
        }

        //remove the node and all of his descendants
        private Set<Integer>  deleteNode(Pair<Member, UserState> nodeData) {
            Node node = findNode(nodeData.getFirst().getId());
            if (node == null) {
                return null; // node not found
            }
            dismissed.add(node.data.getFirst().getId());
            for (Node child : node.children) {
                dismissed.addAll(Objects.requireNonNull(deleteNode(child.data)));
            }
            children.remove(node);
            return dismissed;
        }

        public boolean isAncestor(Node child, Node father)
        {
            if (Objects.equals(child.data.getFirst(), father.data.getFirst())){return true;}
            if (child.father == null){return false;}
            return isAncestor(child.father, father);
        }
    }
    //Represent the root of binary tree
    public Node root;

    public Set<Integer> usersInStore;

    public AppHistory(int storeId, Pair<Member, UserState> creatorNode){

        this.storeId = storeId;
        root = new Node(creatorNode);
        root.father = null;
        usersInStore = new HashSet<>();
        usersInStore.add(creatorNode.getFirst().getId());
    }
    public void canAddNode(int father, Member child, Role role) throws Exception{
        Node childNode = root.findNode(child.getId());
        Node fatherNode = root.findNode(father);
        if (fatherNode != null) {
            if (childNode != null) {
                if (childNode.data.getSecond().getRole() == role) {
                    throw new Exception("user already have a role in the store");
                }
                if (childNode.isAncestor(fatherNode, childNode)) {
                    throw new Exception("circular appointment");
                }
            }
            return;
        }
        throw new Exception("User cant appoint other users in the store");

    }

    public boolean addNode(Integer father, Pair<Member, UserState> child) throws Exception {
        Node childNode = root.findNode(child.getFirst().getId());
        Node fatherNode = root.findNode(father);
        if (fatherNode != null) {
            if (childNode != null) {
                if (childNode.data.getSecond().getRole() == child.getSecond().getRole()) {
                    throw new Exception("user already have a role in the store");
                }
                if (childNode.isAncestor(fatherNode, childNode)) {
                    throw new Exception("circular appointment");
                }
                appointToNewRole(father, child);
                return true;
            }
            fatherNode.addChild(child);
            usersInStore.add(child.getFirst().getId());
            return true;
        }
        throw new Exception("User cant appoint other users in the store");
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

    public void appointToNewRole(Integer fatherid, Pair<Member, UserState> child) throws Exception {
        Node childNode = root.findNode(child.getFirst().getId());
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
        if (isChild(child.getFirst().getId(), fatherid)) {
            throw new Exception("Cannot move a node to its own child");
        }
        childNode.data.setSecond(child.getSecond());
        childNode.father = fatherNode;
        Set<Integer> dismissedes = new HashSet<>(Objects.requireNonNull(root.deleteNode(childNode.data)));
        fatherNode.addChild(childNode.data);
        dismissedes.remove(child.getFirst().getId());
        usersInStore.removeAll(dismissedes);
    }

    public List<Pair<Info, List<Info>>> getAppHistory(){
        List<Pair<Info, List<Info>>> ans = new ArrayList<>();
        getChildren(root, ans);
        return ans;
    }

    public void getChildren(Node n, List<Pair<Info, List<Info>>> add){
        List<Info> ans = new LinkedList<>();
        for(Node child: n.children) {
            getChildren(child, add);
            ans.add(child.data.getFirst().getInformation(storeId));
        }
        add.add(new Pair<>(n.data.getFirst().getInformation(storeId), ans));
    }

    public List<UserState> getRoles(){
        List<UserState> ans = new ArrayList<>();
        getChildrenRoles(root, ans);
        return ans;
    }

    public void getChildrenRoles(Node n, List<UserState> add){
        for(Node child: n.children) {
            getChildrenRoles(child, add);
        }
        add.add(n.data.getSecond());
    }

    public List<String> getStoreWorkersWithPermission(Action permission)
    {
        List<String> lst = new ArrayList<>();
        for (UserState state : getRoles())
        {
            if (state.checkPermission(permission)){lst.add(state.getUserName());}
        }
        return lst;
    }

    public List<JSONObject> toJson(){
        List<JSONObject> ans = new ArrayList<>();
        List<Pair<Info, List<Info>>> history = getAppHistory();
        for(Pair<Info, List<Info>> branch : history) {
            JSONObject jsonBranch = new JSONObject();
            jsonBranch.put("memane", branch.getFirst().toJson());
            jsonBranch.put("memoonim", Information.infosToJson(branch.getSecond()));
            ans.add(jsonBranch);
        }
        return ans;
    }
    public Set<Integer> getStoreCreatorsOwners() {
        Set<Integer> roles = new HashSet<>();
        for (UserState state : getRoles())
        {
            if (state.getRole() == Role.Owner || state.getRole() == Role.Manager){roles.add(state.getUserId());}
        }
        return roles;
    }
}