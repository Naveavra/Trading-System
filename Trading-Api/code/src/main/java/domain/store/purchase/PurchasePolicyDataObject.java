package domain.store.purchase;

import java.util.ArrayList;
import domain.store.purchase.PurchasePolicy.*;

public class PurchasePolicyDataObject {

    //For all Policies
    public int policyID;
    public int storeID;
    public String content;
    public limiters limiter; //User,Item,Category,DateTime
    public int productID; //for user only if category is null, Item, Basket

    public int ageLimit; //User
    public String category; // for user only if productID is null, Category,DateTime
    public int amount; //Item,Category,Basket
    //DateTime
    public int[] dateLimit;
    public int[] timeLimit;
    public PurchasePolicyDataObject next;
    public policyComposeTypes composure;
    public policyTypes type;

    public PurchasePolicyDataObject(int policyID, int storeID, String content, limiters limiter, int productID, int ageLimit, String category, int amount, int[] dateLimit, int[] timeLimit, PurchasePolicyDataObject next, policyComposeTypes composure, policyTypes type) {
        this.policyID = policyID;
        this.storeID = storeID;
        this.content = content;
        this.limiter = limiter;
        this.productID = productID;
        this.ageLimit = ageLimit;
        this.category = category;
        this.amount = amount;
        this.dateLimit = dateLimit;
        this.timeLimit = timeLimit;
        this.next = next;
        this.composure = composure;
        this.type = type;
    }
}
