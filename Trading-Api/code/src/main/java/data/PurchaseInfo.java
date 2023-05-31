package data;

import java.util.ArrayList;
import java.util.List;

public class PurchaseInfo {
    List<ProductInfo> purchases;

    public PurchaseInfo(List<utils.infoRelated.ProductInfo> purchases) {
        this.purchases = new ArrayList<>();
        for(utils.infoRelated.ProductInfo product : purchases)
            this.purchases.add(new ProductInfo(product));
    }
}
