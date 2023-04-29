package java.domain.store.product;

import domain.store.product.Product;
import domain.store.product.Inventory;
import domain.store.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        // Arrange
        int id = 1;
        String name = "Product 1";
        String desc = "Description of product 1";

        // Act
        Product product = new Product(id, name, desc);

        // Assert
        assertNotNull(product);
        assertEquals(id, product.getID());
        assertEquals(name, product.name);
        assertEquals(desc, product.description);
        assertEquals(0, product.price);
        assertEquals(0, product.getQuantity());
    }

    @Test
    public void testProductQuantity() {
        // Arrange
        Product product = new Product(1, "Product 1", "Description of product 1");

        // Act
        product.setQuantity(10);
        int quantity = product.getQuantity();

        // Assert
        assertEquals(10, quantity);
    }

    @Test
    public void testProductPrice() {
        // Arrange
        Product product = new Product(1, "Product 1", "Description of product 1");

        // Act
        product.setPrice(50);
        int price = product.price;

        // Assert
        assertEquals(50, price);
    }

    @Test
    public void testProductClone() {
        // Arrange
        Product product1 = new Product(1, "Product 1", "Description of product 1");
        product1.setPrice(50);
        product1.setQuantity(10);

        // Act
        Product product2 = product1.clone();

        // Assert
        assertNotNull(product2);
        assertNotSame(product1, product2);
        assertEquals(product1.getID(), product2.getID());
        assertEquals(product1.name, product2.name);
        assertEquals(product1.description, product2.description);
        assertEquals(product1.price, product2.price);
        assertEquals(0, product2.getQuantity()); //while cloning quantity should be 0
    }

    @Test
    public void testProductNegativePrice() {
        // Arrange
        Product product = new Product(1, "Product 1", "Description of product 1");

        // Act
        product.setPrice(-50);

        // Assert
        assertEquals(0, product.price);
    }

    @Test
    public void testProductNegativeQuantity() {
        // Arrange
        Product product = new Product(1, "Product 1", "Description of product 1");

        // Act
        product.setQuantity(-10);

        // Assert
        assertEquals(0, product.getQuantity());
    }
}