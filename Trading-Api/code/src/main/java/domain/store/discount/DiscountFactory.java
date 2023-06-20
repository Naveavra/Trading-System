package domain.store.discount;

import com.google.protobuf.Internal;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.storeManagement.Store;
import domain.user.Basket;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscountFactory {
    public int storeID;
    private AtomicInteger discountIds;
    private GetProductOperation getProdOp;
    private GetCategoriesOperation getCategoryOp;
    public DiscountFactory(int storeID, GetProductOperation getProdOp, GetCategoriesOperation getCategoryOp){
        this.storeID = storeID;
        discountIds = new AtomicInteger();
        this.getProdOp = getProdOp;
        this.getCategoryOp = getCategoryOp;
    }
    public synchronized Discount createDiscount(CompositeDataObject discountData) throws Exception {
        Discount concreteDiscount = null;
        if(discountData.numericType != null){
            concreteDiscount = new NumericDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.numericType,getProdOp);
        }
        else {
            switch (discountData.logicalType){
                case Xor -> {
                    LogicalDiscountComposite temp = new LogicalDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.logicalType,getProdOp);
                    temp.setDecidingRule(discountData.xorDecidingRule);
                    concreteDiscount = temp;
                }
                default -> {
                    concreteDiscount = new LogicalDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.logicalType,getProdOp);
                }
            }
        }
        if(!validSize(discountData.discounts,discountData.composites)){
            throw new Exception("Not enough information about the discounts you're trying to compose");
        }
        for(DiscountDataObject dis : discountData.discounts){
            concreteDiscount.addDiscount(createDiscount(dis));
        }
        for(CompositeDataObject comp : discountData.composites){
            concreteDiscount.addDiscount(createDiscount(comp));
        }
        return concreteDiscount;
    }

    public boolean validSize(ArrayList<DiscountDataObject> discounts, ArrayList<CompositeDataObject> composites) {
        if(discounts!=null && composites!=null && discounts.size()+composites.size() >= 2)
            return true;
        if(discounts!=null && discounts.size() >= 2)
            return true;
        if(composites!=null && composites.size() >= 2)
            return true;
        return false;
    }
    public synchronized Discount createDiscount(DiscountDataObject discountData){
        Discount concreteDiscount = new AbstractDiscount() {


            @Override
            public double handleDiscount(Basket basket, Order order) {
                return 0;
            }
        };
        switch (discountData.discountType){
            case Store -> {
                concreteDiscount = new DiscountOnStore(discountIds.getAndIncrement(),storeID,discountData.percentage,"");
            }
            case Product -> {
                DiscountOnItem temp = new DiscountOnItem(discountIds.getAndIncrement(),storeID,discountData.percentage,"");
                temp.setDiscountedProductID(discountData.prodId);
                concreteDiscount = temp;
            }
            case Category -> {
                DiscountOnCategory temp = new DiscountOnCategory(discountIds.getAndIncrement(),storeID,discountData.percentage, discountData.discountedCategory);
                concreteDiscount = temp;
            }
        }
        concreteDiscount.setOperations(getProdOp,getCategoryOp);

        return (discountData.predicates == null || discountData.predicates.isEmpty())? concreteDiscount : addPredicates(concreteDiscount,discountData);
    }

    private Discount addPredicates(Discount concreteDiscount, DiscountDataObject discountData) {
        ArrayList<PredicateDataObject> predicates = discountData.predicates;
        if(!predicates.isEmpty()){
            for(PredicateDataObject pred : predicates){
                concreteDiscount.addPredicate(pred.predType, pred.params, pred.composore);
            }
        }
        return concreteDiscount;
    }

    public CompositeDataObject parseCompositeDiscount(JSONObject req) throws Exception {
        CompositeDataObject root = null;
        HashMap<Integer,CompositeDataObject> composites = new HashMap<>();
        HashMap<Integer,DiscountDataObject> regular = new HashMap<>();
        JSONArray compJsons = req.getJSONArray("discountNodes");
        for(int i = 0; i<compJsons.length();i++){
            JSONObject compJsonData = compJsons.getJSONObject(i);
            int id = Integer.parseInt(compJsonData.get("id").toString());
            JSONObject data = compJsonData.getJSONObject("data");
            String type = data.get("type").toString();
            if(Objects.equals(type, "regular")){
                regular.put(id,parseRegularDiscount(data));
                continue;
            }
            double percentage = Double.parseDouble(data.get("percentage").toString());
            NumericDiscountComposite.numeric numericType = getNumericType(data.get("numericType").toString());
            LogicalDiscountComposite.logical logicalType = getLogicalType(data.get("logicalType").toString());
            LogicalDiscountComposite.xorDecidingRules xorDecidingRule = getXorDecidingRule(data.get("xorDecidingRule").toString());
            CompositeDataObject dataObj = new CompositeDataObject(percentage,numericType,logicalType,xorDecidingRule,null,null);
            composites.put(id,dataObj);
        }
        JSONArray nodeLoc = req.getJSONArray("discountEdges");
        for(int i = 0 ; i<nodeLoc.length() ; i++){
            JSONObject edge = nodeLoc.getJSONObject(i);
            int source = Integer.parseInt(edge.get("source").toString());
            int target = Integer.parseInt(edge.get("target").toString());
            CompositeDataObject src = composites.getOrDefault(source,null);
            if(src != null){
                if(source == 0)
                    root = src;

                CompositeDataObject compTarget = composites.getOrDefault(target,null);
                if(compTarget != null){
                    src.addToComposites(compTarget);
                    continue;
                }
                DiscountDataObject regTarget = regular.getOrDefault(target,null);
                if(regTarget != null){
                    src.addToDiscounts(regTarget);
                }
            }
        }
        return root;
    }

    public void parse(String data,ArrayList<Discount> discounts){
        JSONObject dataObj = new JSONObject(data);
        try {
            JSONArray arr = dataObj.getJSONArray("discountNodes");
            discounts.add(createDiscount(parseCompositeDiscount(dataObj)));
        } catch (Exception e) {
            discounts.add(createDiscount(parseRegularDiscount(dataObj)));
        }
    }
    private LogicalDiscountComposite.xorDecidingRules getXorDecidingRule(String s){
        return switch (s){
            case "MinDiscountValue" -> LogicalDiscountComposite.xorDecidingRules.MinDiscountValue;
            case "MaxDiscountValue" -> LogicalDiscountComposite.xorDecidingRules.MaxDiscountValue;
            default -> null ;
        };
    }
    private LogicalDiscountComposite.logical getLogicalType(String s){
        return switch (s){
            case "XOR" -> LogicalDiscountComposite.logical.Xor;
            case "AND" -> LogicalDiscountComposite.logical.And;
            case "OR" -> LogicalDiscountComposite.logical.Or;
            default -> null;
        };
    }
    private NumericDiscountComposite.numeric getNumericType(String s){
        return switch (s){
            case "ADDITTION" -> NumericDiscountComposite.numeric.Addition;
            case "MAX" -> NumericDiscountComposite.numeric.Max;
            default -> null;
        };
    }
    public DiscountDataObject parseRegularDiscount(JSONObject request){
        // int storeId = Integer.parseInt(request.get("storeId").toString());
     //   int userId = Integer.parseInt(request.get("userId").toString());
        int percentage =  Integer.parseInt(request.get("percentage").toString());
        String discountType = request.get("discountType").toString();
        int prodId = Integer.parseInt(request.get("prodId").toString());
        String discountedCategory = request.get("discountedCategory").toString();
        String predicates = request.get("predicates").toString();
        JSONArray predicatesJson = request.getJSONArray("predicates");
        List<String[]> predicatesLst =new ArrayList<>();
        if (!predicates.equals("null")) {
            if(!predicatesJson.equals("[]")){
            for (int i = 0; i < predicatesJson.length(); i++) {
                JSONObject predicateObject = predicatesJson.getJSONObject(i);
                String predType = predicateObject.getString("predType");
                String params = predicateObject.getString("params");
                String composore = predicateObject.getString("composore");
                String[] arr = {predType, params, composore};
                predicatesLst.add(arr);
            }                //predicates.substring(1, predicates.length() - 1).split(",");
            }
        }
//        if (!predicates.equals("null")) {
//            String[] arr = predicates.substring(1, predicates.length() - 1).split(",");
//            predicatesLst =new ArrayList<>(Arrays.asList(arr));
//        }
//        Store s = getActiveStore(storeId);
        AbstractDiscount.discountTypes discountTypeEnum = AbstractDiscount.discountTypes.Store;
        if (Objects.equals(discountType.toLowerCase(), "product")){discountTypeEnum = AbstractDiscount.discountTypes.Product;}
        if (Objects.equals(discountType.toLowerCase(), "category")){discountTypeEnum = AbstractDiscount.discountTypes.Category;}
        DiscountDataObject obj = new DiscountDataObject(percentage,discountTypeEnum,prodId, discountedCategory, parsePredicateData(new ArrayList<>(predicatesLst)));
        return obj;
    }

    public ArrayList<PredicateDataObject> parsePredicateData(ArrayList<String[]> predData){
        ArrayList<PredicateDataObject> predicates = new ArrayList<>();
        for(String[] data : predData){
//            JSONObject dataJson = new JSONObject(data);
//            String predTypeStr = dataJson.getString("predType");
//            DiscountPredicate.PredicateTypes predType = DiscountPredicate.PredicateTypes.valueOf(predTypeStr);
//            String params = dataJson.getString("params");
//            String composureStr = dataJson.getString("composore");
//            DiscountPredicate.composore composore = DiscountPredicate.composore.valueOf(composureStr);
//
            String predTypeStr = data[0];
            DiscountPredicate.PredicateTypes predType = DiscountPredicate.PredicateTypes.valueOf(predTypeStr);
            String params = data[1];
            String composoreStr = data[2];
            DiscountPredicate.composore composore = DiscountPredicate.composore.valueOf(composoreStr);
            predicates.add(new PredicateDataObject(predType,params,composore));
        }
        return predicates;
    }
}
