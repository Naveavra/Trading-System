package domain.store.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    @DisplayName("addProduct should add a new product to the product list")
    void addProduct() {
        String name = "Product 1";
        String description = "This is product 1";
        AtomicInteger prod_id = new AtomicInteger(0);
        inventory.addProduct(name, description, prod_id);

        Product p = inventory.getProduct(0);
        assertNotNull(p);
        assertEquals(name, p.getName());
        assertEquals(description, p.getDescription());
    }

    @Test
    @DisplayName("setDescription should set the description of an existing product")
    void setDescription() {
        String name = "Product 1";
        String description = "This is product 1";
        AtomicInteger prod_id = new AtomicInteger(0);
        inventory.addProduct(name, description, prod_id);

        int prodID = 0;
        String newDescription = "This is the new description";
        inventory.setDescription(prodID, newDescription);

        Product p = inventory.getProduct(prodID);
        assertEquals(newDescription, p.getDescription());
    }

    @Test
    @DisplayName("setPrice should set the price of an existing product")
    void setPrice() throws Exception {
        String name = "Product 1";
        String description = "This is product 1";
        AtomicInteger prod_id = new AtomicInteger(0);
        inventory.addProduct(name, description, prod_id);

        int prodID = 0;
        int newPrice = 100;
        inventory.setPrice(prodID, newPrice);

        Product p = inventory.getProduct(prodID);
        assertEquals(newPrice, p.getPrice());
    }

    @Test
    @DisplayName("setPrice should throw an exception if product doesn't exist")
    void setPriceThrowsException() {
        int prodID = 0;
        int newPrice = 100;

        Exception exception = assertThrows(Exception.class, () -> {
            inventory.setPrice(prodID, newPrice);
        });

        String expectedMessage = "product doesnt exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("addQuantity should add the quantity to an existing product")
    void addQuantity() {
        String name = "Product 1";
        String description = "This is product 1";
        AtomicInteger prod_id = new AtomicInteger(0);
        inventory.addProduct(name, description, prod_id);

        int prodID = 0;
        int initialQuantity = inventory.getProduct(prodID).getQuantity();
        int quantityToAdd = 10;
        inventory.addQuantity(prodID, quantityToAdd);

        Product p = inventory.getProduct(prodID);
        assertEquals(initialQuantity + quantityToAdd, p.getQuantity());
    }

    @Test
    void addProduct_shouldAddNewProduct() {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();

        // Act
        controller.addProduct("Product A", "Description A", prod_id);
        Product actualProduct = controller.getProduct(0);

        // Assert
        assertNotNull(actualProduct);
        assertEquals("Product A", actualProduct.getName());
        assertEquals("Description A", actualProduct.getDescription());
    }

    @Test
    void addProduct_shouldNotAddDuplicateProduct() {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();
        controller.addProduct("Product A", "Description A", prod_id);

        // Act
        controller.addProduct("Product A", "Description B", prod_id);
        Product actualProduct = controller.getProduct(0);

        // Assert
        assertNotNull(actualProduct);
        assertEquals("Product A", actualProduct.getName());
        assertEquals("Description A", actualProduct.getDescription());
    }

    @Test
    void setDescription_shouldUpdateProductDescription() {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();
        controller.addProduct("Product A", "Description A", prod_id);

        // Act
        controller.setDescription(0, "New Description");
        Product actualProduct = controller.getProduct(0);

        // Assert
        assertNotNull(actualProduct);
        assertEquals("New Description", actualProduct.getDescription());
    }

    @Test
    void setPrice_shouldUpdateProductPrice() throws Exception {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();
        controller.addProduct("Product A", "Description A", prod_id);

        // Act
        controller.setPrice(0, 10);
        Product actualProduct = controller.getProduct(0);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(10, actualProduct.getPrice());
    }

    @Test
    void setPrice_shouldThrowExceptionForInvalidProduct() {
        // Arrange
        Inventory controller = new Inventory();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            controller.setPrice(0, 10);
        });
    }

    @Test
    void addQuantity_shouldUpdateProductQuantity() {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();
        controller.addProduct("Product A", "Description A", prod_id);

        // Act
        controller.addQuantity(0, 5);
        Product actualProduct = controller.getProduct(0);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(5, actualProduct.getQuantity());
    }

    @Test
    void getProductByName_shouldReturnProductWithMatchingName() {
        // Arrange
        Inventory controller = new Inventory();
        AtomicInteger prod_id = new AtomicInteger();
        controller.addProduct("Product A", "Description A", prod_id);

        // Act
        Product actualProduct = controller.getProductByName("Product A");

        // Assert
        assertNotNull(actualProduct);
        assertEquals("Product A", actualProduct.getName());
    }
}