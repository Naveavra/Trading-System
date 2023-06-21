package store;

import database.daos.Dao;
import domain.store.product.Product;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    @BeforeAll
    public static void startUp(){
        Dao.setForTests(true);
    }

    @Test
    void testSetAndReplaceQuantity_positiveInputAndAddition() throws Exception
    {
        Product p = new Product(0, 1,"Watermelon","7kg of pure,red, juicy love that will never betray you.", 50, 50);
        assertEquals(50,p.getQuantity());
        p.setQuantity(10, null);
        assertEquals(60,p.getQuantity());
    }

    @Test
    void testSetAndReplaceQuantity_negativeInput()
    {
        Product p = new Product(0,1,"Avocado","Weird green sphere that everyone seems to pretend that they love.", 50, 50);
        assertThrows(Exception.class,()->{p.setQuantity(-51, null);});
        assertThrows(Exception.class,()->{p.replaceQuantity(-5);});
        try{
            p.setQuantity(-5, null);
        }
        catch (Exception e){
            assertEquals(e.getMessage(),"Invalid Quantity: New quantity for product <= 0.");
        }
        try{
            p.replaceQuantity(-5);
        }
        catch (Exception e){
            assertEquals(e.getMessage(),"Invalid Quantity: New quantity for product <= 0.");
        }

    }

    @Test
    void testSetPrice_ValidInput() throws Exception {
        Product p = new Product(0, 1,"Steak","A fine piece of meat filled with the blood of your enemies.", 50, 50);
        p.setPrice(20);
        assertEquals(20, p.getPrice());
        p.setPrice(15);
        assertEquals(15,p.getPrice());
    }

    @Test
    void testSetPrice_InvalidInput(){
        Product p = new Product(0, 1,"Steak","A fine piece of meat filled with the blood of your enemies.", 50, 50);
        try{
            p.setPrice(-20);
        }catch (Exception e){
            assertEquals(e.getMessage(),"Invalid Price: New price for product <= 0.");
        }
    }
}
