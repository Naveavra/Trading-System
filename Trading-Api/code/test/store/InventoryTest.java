package store;

import database.daos.Dao;
import domain.store.product.Inventory;
import static org.junit.jupiter.api.Assertions.*;
import domain.store.product.Product;
import org.junit.jupiter.api.*;
import utils.Filter.ProductFilter;
import utils.infoRelated.ProductInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryTest {
    private Inventory inventory;
    private AtomicInteger ids;
    @BeforeEach
    void setUp(){
        inventory = new Inventory(0);
        ids = new AtomicInteger();
        Dao.setForTests(true);
    }
    @Test
    void testAddProduct_ValidInput() throws Exception{
        List<String> categories = new ArrayList<>();
        inventory.addProduct("OG Kush","A familiar brand with a fruity scent.",ids,10, 5, categories);
        Product p = inventory.getProduct(ids.get()-1);
        assertNotNull(p);
        assertEquals(p.getName(),"OG Kush");
        assertEquals(0,p.getID());
        inventory.addProduct("Lemon-Haze","Lemony flavors laced in the sweat of your enemies.",ids,10, 5, categories);
        Product p2 = inventory.getProduct(ids.get()-1);
        assertEquals(p2.getName(),"Lemon-Haze");
        assertEquals(1,p2.getID());
    }

    @Test
    void testAddProduct_InvalidInput() throws Exception{
        List<String> categories = new ArrayList<>();
        inventory.addProduct("Waffles","Fluffy, tasty and makes you fat AF.",ids,10, 5, categories);
        assertThrows(Exception.class, ()->inventory.addProduct("Waffles","Fluffy, tasty and makes you fat AF.",ids,10, 5, categories));
        assertEquals(ids.get()-1,0);

    }

    @Test
    void testGetProductByName_CaseInsensitive(){
        try{
            List<String> categories = new ArrayList<>();
            Product expected = inventory.addProduct("Milk","Something to calm your baby face down.",ids,10, 5, categories);
            assertEquals(expected,inventory.getProductByName("Milk")); //should pass no issues
            assertEquals(expected,inventory.getProductByName("milk"));
            assertEquals(expected,inventory.getProductByName("MiLk"));
            assertEquals(expected,inventory.getProductByName("mILK"));
            assertEquals(expected,inventory.getProductByName("MILK"));
        }catch (Exception e){
            fail("Unexpected Exception\n" + e.getMessage());
        }
    }

    @Test
    void testAddToCategory()throws Exception{
        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Dairy","Momma's"));
        Product p = inventory.addProduct("Milk","Something to calm your baby face down.",ids,10, 5, categories);
        for(String real:inventory.getProductCategories(p.getID())){
            assertTrue(categories.contains(real));
        }
    }

    @Test
    void testRemoveProduct_Valid() throws Exception{
        ArrayList<String> categories = new ArrayList<>(Arrays.asList("Dairy","Momma's"));
        Product p = inventory.addProduct("Cocaine","Snowy white powder that gives you magical powers.",ids,10, 5, categories);
        assertNotNull(inventory.getProduct(p.getID())); //item added
        assertTrue(inventory.getProductCategories(0).size()>=2); //categories aware of item
        assertDoesNotThrow(()->inventory.removeProduct(0)); //remove product here
        assertThrows(Exception.class,()->inventory.getProduct(p.getID()));
        assertEquals(0, inventory.getProductCategories(0).size());
    }

    @Test
    public void testAddSameProductConcurrently() throws Exception {
        String name = "Product1";
        String description = "This is a product";
        AtomicInteger prod_id = new AtomicInteger();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> categories = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            Callable<Product> task = () -> {
                latch.countDown(); // decrement the count on each thread start
                latch.await(); // wait until all threads start
                return inventory.addProduct(name, description, prod_id,10, 5, categories);
            };
            executorService.submit(task);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(1, prod_id.get());
    }

    @Test
    public void testAddDifferentProductConcurrently() throws Exception {
        String description = "This is a product";
        List<String> categories = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            String name = "Product"+i;
            Callable<Product> task = () -> {
                latch.countDown(); // decrement the count on each thread start
                latch.await(); // wait until all threads start
                return inventory.addProduct(name, description, ids,10, 5, categories);
            };
            executorService.submit(task);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(inventory.getProducts().size(), 10);
    }

    @Test
    void testGetPricesConcurrently() throws InterruptedException {
        String description = "This is a product";
        List<String> categories = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        HashMap<Integer,Integer> expected = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String name = "Product"+i;
            int price = (i+1)*10;
//            expected.put(i,price);
            Callable<Product> task = () -> {
                latch.countDown(); // decrement the count on each thread start
                latch.await(); // wait until all threads start
                Product p = inventory.addProduct(name,description,ids,price, 5, categories);
                expected.put(p.getID(),price);
//                inventory.setPrice(p.getID(),price);
                return p;
            };
            executorService.submit(task);
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        HashMap<Integer,Integer> results = inventory.getPrices();
        assertEquals(10,results.size());
        for(Integer key : results.keySet()){
            assertTrue(expected.containsKey(key));
            assertEquals(results.get(key),expected.get(key));
        }
    }

    @Test
    void testProductRatings_Solo_ValidInput() throws Exception{
        List<String> categories = new ArrayList<>();
        Product p = inventory.addProduct("Demon Blood","Used in satanic rituals, bitter taste.",ids,10, 5, categories);
        ArrayList<Integer> ratings = new ArrayList<>(Arrays.asList(3,4,2,5,4,3,2,2,3,4,4));
        double avg = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedAvg = df.format(avg);
        avg = Double.parseDouble(formattedAvg);
        for(Integer rating:ratings){
            inventory.rateProduct(p.getID(),rating);
        }
        assertEquals(avg,p.getRating());
    }
    @Test
    void testProductRatings_Solo_InvalidInput(){
        try {
            List<String> categories = new ArrayList<>();
            Product p = inventory.addProduct("Katana", "Used to slash innocent elderly and children.", ids, 10, 5, categories);
            assertThrows(Exception.class,()->{inventory.rateProduct(p.getID(),6);});
            assertEquals(p.getRating(),5);
        }
        catch (Exception e){
            fail("Something went seriously wrong, as usual.");
        }
    }

    @Test
    void testProductRatings_Concurrent_ValidInput() throws Exception {
        List<String> categories = new ArrayList<>();
        Product p = inventory.addProduct("Banana Slicer 9000","Are you tired of having to manually slice your bananas with a knife like a Neanderthal? Well, say hello to the Banana Slicer 9000! With this cutting-edge technology, you can slice your bananas with the precision of a surgeon and the speed of a ninja. Don't let your bananas bully you anymore, show them who's boss with the Banana Slicer 9000!", ids, 10, 5, categories);
        assertNotNull(p);
        ArrayList<Integer> ratings = new ArrayList<>(Arrays.asList(3,4,2,5,4,3,2,2,3,4,4));
        double expectedAvg = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        CountDownLatch latch = new CountDownLatch(ratings.size());
        ExecutorService executor = Executors.newFixedThreadPool(ratings.size());

        for (Integer rating : ratings) {
            executor.submit(() -> {
                try {
                    inventory.rateProduct(p.getID(), rating);
                } catch (Exception e) {
                    fail("Something went wrong: "+e.getMessage());
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();

        double avg = p.getRating();
        DecimalFormat df = new DecimalFormat("#.#");
        expectedAvg = Double.parseDouble(df.format(expectedAvg));

        assertEquals(expectedAvg, avg);
    }

    @Test
    void testProductRatings_Concurrent_InvalidInput() throws Exception {
        List<String> categories = new ArrayList<>();
        Product p = inventory.addProduct("Selfie Toaster","Burn your selfie onto your toast with the Selfie Toaster. Start your day with a smile and show off your best selfie to everyone... even your breakfast!", ids, 10, 5, categories);
        assertNotNull(p);
        ArrayList<Integer> ratings = new ArrayList<>(Arrays.asList(3,4,2,5,4,3,2,2,3,4,4));
        double expectedAvg = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        ratings.addAll(Arrays.asList(6,7,8));
        CountDownLatch latch = new CountDownLatch(ratings.size());
        ExecutorService executor = Executors.newFixedThreadPool(ratings.size());

        for (Integer rating : ratings) {
            executor.submit(() -> {
                try {
                    if(rating<=5){
                        inventory.rateProduct(p.getID(), rating);
                        latch.countDown();

                    }
                    else {
                        assertThrows(Exception.class,()->{inventory.rateProduct(p.getID(),rating);});
                        latch.countDown();
                    }
                } catch (Exception e) {
                    fail("Something went wrong: "+e.getMessage());
                }
            });
        }
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        double avg = p.getRating();
        DecimalFormat df = new DecimalFormat("#.#");
        expectedAvg = Double.parseDouble(df.format(expectedAvg));

        assertEquals(expectedAvg, avg);
    }

//    @Test
//    void multipleCalls() throws Exception{
//        for(int i =0;i<1000;i++){
//            setUp();
//            testProductRatings_Concurrent_InvalidInput();
//        }
//    }

    @Test
    void testFilterByProductRating() throws Exception{
        //Setup
        String ratingHigh = "4";
        String ratingLow = "2";
        List<String> categories = new ArrayList<>();
        Product p1 = inventory.addProduct("prod1", "Description.", ids, 10, 5, categories);
        Product p2 = inventory.addProduct("prod2", "Nice Description.", ids, 10, 5, categories);
        Product p3 = inventory.addProduct("prod3", "Very Nice Description.", ids, 10, 5, categories);
        Product p4 = inventory.addProduct("prod4", "Not Very Nice Description.", ids, 10, 5, categories);
        int[][] productRatings = {
                {p1.getID(), 3, 4, 5, 5, 5}, //avg 4.4
                {p2.getID(), 4, 5, 5, 5, 5, 5}, //avg 4.67
                {p3.getID(), 0, 1, 2, 4, 5, 5, 5}, //avg 3.29
                {p4.getID(), 0, 0, 1, 1} //avg 0.5
        };
        for (int[] productRating : productRatings) {
            int[] ratings = Arrays.copyOfRange(productRating, 1, productRating.length);
            for (int rating : ratings) {
                inventory.rateProduct(productRating[0], rating);
            }
        }

        //High Rating
        ProductFilter filter = new ProductFilter();
        ProductFilter.strategies strategy = ProductFilter.strategies.ProductRating;
        filter.addStrategy(filter.createStrategy(strategy,ratingHigh)); //assign rating > 4

        ArrayList<ProductInfo> filtered = inventory.filterBy(filter,0); //should hold p1,p2 info
        assertEquals(2, filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p1.productId || pf.id == p2.productId);
        }

        //Low Rating
        filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategy,ratingLow)); //assign rating > 2

        filtered = inventory.filterBy(filter,0); //should hold p1,p2,p3 info
        assertEquals(3, filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p1.productId || pf.id == p2.productId || pf.id ==p3.productId);
        }
    }

    @Test
    void testFilterByPrice() throws Exception{
        //Setup
        String maxPrice = "8";
        String minPrice = "4";
        ProductFilter.strategies strategyMax = ProductFilter.strategies.PriceRangeMax;
        ProductFilter.strategies strategyMin = ProductFilter.strategies.PriceRangeMin;
        List<String> categories = new ArrayList<>();
        Product p1 = inventory.addProduct("prod1", "Description.", ids, 2, 5, categories);
        Product p2 = inventory.addProduct("prod2", "Nice Description.", ids, 4, 5, categories);
        Product p3 = inventory.addProduct("prod3", "Very Nice Description.", ids, 6, 5, categories);
        Product p4 = inventory.addProduct("prod4", "Not Very Nice Description.", ids, 8, 5, categories);
        Product p5 = inventory.addProduct("prod5", "Maybe Not Very Nice Description.", ids, 10, 5, categories);

        //Only Max <= 8 p1,p2,p3,p4  Size == 4
        ProductFilter filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategyMax,maxPrice));

        ArrayList<ProductInfo> filtered = inventory.filterBy(filter,0); //p1,p2,p3,p4
        assertEquals(4, filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p1.productId || pf.id == p2.productId || pf.id == p3.productId || pf.id == p4.productId);
        }

        //Only Min >= 4 p2,p3,p4,p5  Size == 4
        filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategyMin,minPrice));

        filtered = inventory.filterBy(filter,0); //p2,p3,p4,p5
        assertEquals(4, filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p5.productId || pf.id == p2.productId || pf.id == p3.productId || pf.id == p4.productId);
        }

        //Min&Max 4 <= x <= 8  p2,p3,4   Size == 3
        filter.addStrategy(filter.createStrategy(strategyMax,maxPrice));

        filtered = inventory.filterBy(filter,0);
        assertEquals(3,filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p2.productId || pf.id == p3.productId || pf.id == p4.productId);
        }
    }

    @Test
    void testFilterByCategories() throws Exception{
        //Setup
        enum categories {Dairy,Hardware,Electronics,Tools,Vegetables,Satanic_Rituals}
        ProductFilter.strategies strategy = ProductFilter.strategies.Categories;

        //List<String> categories = new ArrayList<>(Arrays.asList("Dairy","Hardware","Electronics","Tools","Vegetables","Satanic Rituals"));
        List<String> cat1 = new ArrayList<>(List.of("Dairy"));
        List<String> cat2 = new ArrayList<>(Arrays.asList("Hardware","Electronics","Tools"));
        List<String> cat3 = new ArrayList<>(Arrays.asList("Electronics","Tools","Vegetables"));
        List<String> cat4 = new ArrayList<>(Arrays.asList("Hardware", "SatanicRituals"));
        List<String> cat5 = new ArrayList<>(Arrays.asList("Dairy", "SatanicRituals"));
        Product p1 = inventory.addProduct("prod1", "Description.", ids, 10, 5, cat1);
        Product p2 = inventory.addProduct("prod2", "Nice Description.", ids, 10, 5, cat2);
        Product p3 = inventory.addProduct("prod3", "Very Nice Description.", ids, 10, 5, cat3);
        Product p4 = inventory.addProduct("prod4", "Not Very Nice Description.", ids, 10, 5, cat4);
        Product p5 = inventory.addProduct("prod5", "Maybe Not Very Nice Description.", ids, 10, 5, cat5);

        ProductFilter filter = new ProductFilter();
        String filterBy = "Dairy SatanicRituals";
        filter.addStrategy(filter.createStrategy(strategy,filterBy));

        ArrayList<ProductInfo> filtered = inventory.filterBy(filter,0); //p1,p4,p5
        assertEquals(3,filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(pf.id == p1.productId || pf.id == p4.productId || pf.id == p5.productId);
        }
    }

    @Test
    void testFilterProductsByKeywords() throws Exception {
        //Setup
        ProductFilter.strategies strategy = ProductFilter.strategies.Keywords;
        List<String> categories = new ArrayList<>();
        Product p1 = inventory.addProduct("prod1", "Our facial serum is the epitome of luxury - because who needs financial stability when you have flawless skin?.", ids, 10, 5, categories);
        Product p2 = inventory.addProduct("prod2", "Our memory foam mattress topper is the height of luxury - perfect for when you want to feel like you're sleeping on a cloud, instead of just dreaming about one..", ids, 10, 5, categories);
        Product p3 = inventory.addProduct("prod3", "This backpack will make you feel like a million bucks... or at least like you can afford the luxury toilet paper..", ids, 10, 5, categories);
        Product p4 = inventory.addProduct("prod4", "This backpack is tougher than your ex's heart, with more compartments than they had excuses..", ids, 10, 5, categories);
        Product p5 = inventory.addProduct("prod5", "No that's not 4kg of cocaine in my backpack, its just flour.", ids, 10, 5, categories);

        String keywords1 = "luxury"; //p1 p2 p3
        String keywords2 = "backpack"; //p3 p4 p5
        String keywords3 = "luxury backpack"; //p1 p2 p3 p4 p5

        //Keywords1
        ArrayList<Integer> allowedIds = new ArrayList<>(Arrays.asList(p1.getID(),p2.getID(),p3.getID()));
        ProductFilter filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategy,keywords1));
        ArrayList<ProductInfo> filtered = inventory.filterBy(filter,0); //p1 p2 p3
        assertEquals(3,filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(allowedIds.contains(pf.id));
        }

        //Keywords2
        allowedIds = new ArrayList<>(Arrays.asList(p4.getID(),p5.getID(),p3.getID()));
        filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategy,keywords2));
        filtered = inventory.filterBy(filter,0); //p3 p4 p5
        assertEquals(3,filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(allowedIds.contains(pf.id));
        }

        //Keywords3
        allowedIds = new ArrayList<>(Arrays.asList(p1.getID(),p2.getID(),p3.getID(),p4.getID(),p5.getID()));
        filter = new ProductFilter();
        filter.addStrategy(filter.createStrategy(strategy,keywords3));
        filtered = inventory.filterBy(filter,0); //p1 p2 p3 p4 p5
        assertEquals(5,filtered.size());
        for(ProductInfo pf: filtered){
            assertTrue(allowedIds.contains(pf.id));
        }
    }


}
