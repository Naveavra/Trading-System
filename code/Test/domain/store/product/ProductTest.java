package domain.store.product;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class ProductTest {

    @Test
    public void testAddCategory() {
        Product p = new Product(1, "Test Product", "A test product");
        p.addCategory("Category 1");
        p.addCategory("Category 2");
        List<String> categories = p.getCategories();
        assertEquals(categories.size(), 2);
        assertTrue(categories.contains("Category 1"));
        assertTrue(categories.contains("Category 2"));
    }

    @Test
    public void testSetPrice() {
        Product p = new Product(1, "Test Product", "A test product");
        p.setPrice(10);
        assertEquals(p.price, 10);
    }

    @Test
    public void testClone() {
        Product p = new Product(1, "Test Product", "A test product");
        p.addCategory("Category 1");
        p.setPrice(10);
        Product clone = p.clone();
        assertEquals(clone.id, p.id);
        assertEquals(clone.name, p.name);
        assertEquals(clone.description, p.description);
        assertEquals(clone.price, p.price);
        List<String> cloneCategories = clone.getCategories();
        List<String> pCategories = p.getCategories();
        assertEquals(cloneCategories.size(), pCategories.size());
        for (int i = 0; i < cloneCategories.size(); i++) {
            assertEquals(cloneCategories.get(i), pCategories.get(i));
        }
    }

}