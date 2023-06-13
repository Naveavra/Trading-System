package store;

import domain.store.product.Product;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    @BeforeAll
    public static void startUp(){

    }

    @Test
    void testSetAndReplaceQuantity_positiveInputAndAddition() throws Exception
    {
        Product p = new Product(0, 1,"Watermelon","7kg of pure,red, juicy love that will never betray you.");
        p.setQuantity(5);
        assertEquals(5,p.getQuantity());
        p.setQuantity(10);
        assertEquals(15,p.getQuantity());
    }

    @Test
    void testSetAndReplaceQuantity_negativeInput()
    {
        Product p = new Product(0,1,"Avocado","Weird green sphere that everyone seems to pretend that they love.");
        assertThrows(Exception.class,()->{p.setQuantity(-5);});
        assertThrows(Exception.class,()->{p.replaceQuantity(-5);});
        try{
            p.setQuantity(-5);
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
        Product p = new Product(0, 1,"Steak","A fine piece of meat filled with the blood of your enemies.");
        p.setPrice(20);
        assertEquals(20, p.getPrice());
        p.setPrice(15);
        assertEquals(15,p.getPrice());
    }

    @Test
    void testSetPrice_InvalidInput(){
        Product p = new Product(0, 1,"Steak","A fine piece of meat filled with the blood of your enemies.");
        try{
            p.setPrice(-20);
        }catch (Exception e){
            assertEquals(e.getMessage(),"Invalid Price: New price for product <= 0.");
        }
    }
}
