package utils.Filter;

import domain.store.product.Product;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterProductByKeywords extends FilterStrategy{
    private ArrayList<String> keywords;

    public FilterProductByKeywords() {
        this.name = "Keywords";
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public ArrayList<Product> filter(ArrayList<Product> products) {
        Set<Product> temp = new HashSet<>();
        Map<Product, Integer> productToNumMatches = new HashMap<>();
        for(String word : keywords) {
            String regex = "\\b" + Pattern.quote(word) + "\\b";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            for (Product p : products) {
                Matcher matcher = pattern.matcher(p.getDescription());
                if(matcher.find()){
                    if(temp.add(p)){
                        productToNumMatches.put(p,1);
                    }
                    else{
                        productToNumMatches.put(p,productToNumMatches.get(p)+1);
                    }
                }
            }
        }
        ArrayList<Product> matchingProducts = new ArrayList<>(temp);

        matchingProducts.sort((p1, p2) -> {
            int numMatches1 = productToNumMatches.get(p1);
            int numMatches2 = productToNumMatches.get(p2);
            return Integer.compare(numMatches2, numMatches1);
        });

        FilterStrategy nextOne = getNext();
        if(nextOne!=null){
            matchingProducts = nextOne.filter(products);
        }
        return matchingProducts;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
}
