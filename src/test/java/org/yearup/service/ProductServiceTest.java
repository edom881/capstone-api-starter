package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest
{
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> allProducts;

    @BeforeEach
    public void setUp()
    {
        allProducts = Arrays.asList(
                createProduct(1, "Smartphone", 499.99, 1, "Black", false),
                createProduct(2, "Laptop", 899.99, 1, "Gray", false),
                createProduct(3, "Headphones", 99.99, 1, "White", true),
                createProduct(4, "Men's T-Shirt", 29.99, 2, "Charcoal", true),
                createProduct(5, "Men's Jeans", 59.99, 2, "Blue", false),
                createProduct(6, "Men's Dress Shirt", 49.99, 2, "White", false),
                createProduct(7, "Women's Dress", 79.99, 2, "Mint", false),
                createProduct(8, "Women's Jeans", 69.99, 2, "Blue", true),
                createProduct(9, "Women's Blouse", 49.99, 2, "Lavender", false),
                createProduct(10, "Cookware Set", 149.99, 3, "Red", true),
                createProduct(11, "Coffee Maker", 79.99, 3, "Black", false),
                createProduct(12, "Kitchen Knife Set", 59.99, 3, "Silver", true)
        );

        when(productRepository.findAll()).thenReturn(allProducts);
        when(productRepository.findByCategoryId(1)).thenReturn(allProducts.stream()
                .filter(p -> p.getCategoryId() == 1).toList());
        when(productRepository.findByCategoryId(2)).thenReturn(allProducts.stream()
                .filter(p -> p.getCategoryId() == 2).toList());
    }

    @Test
    public void search_noFilters_shouldReturnAllProducts()
    {
        // act
        List<Product> actual = productService.search(null, null, null, null);

        // assert
        assertEquals(12, actual.size(), "Search with no filters should return all 12 products");
    }

    @Test
    public void search_byCategory_shouldReturnAllProductsInCategory()
    {
        // act
        List<Product> actual = productService.search(1, null, null, null);

        // assert
        assertEquals(3, actual.size(), "Electronics category should return 3 products");

        for (Product product : actual)
        {
            assertEquals(1, product.getCategoryId(), "All products should be from category 1");
        }

        boolean hasFeatured = actual.stream().anyMatch(Product::isFeatured);
        boolean hasNonFeatured = actual.stream().anyMatch(p -> !p.isFeatured());
        assertTrue(hasFeatured && hasNonFeatured, "Should include both featured and non-featured products");
    }

    @Test
    public void search_byCategoryFashion_shouldReturnAllProductsIncludingNonFeatured()
    {
        // act
        List<Product> actual = productService.search(2, null, null, null);

        // assert
        assertEquals(6, actual.size(), "Fashion category should return 6 products");

        long featuredCount = actual.stream().filter(Product::isFeatured).count();
        long nonFeaturedCount = actual.stream().filter(p -> !p.isFeatured()).count();
        assertEquals(2, featuredCount, "Fashion should have 2 featured products");
        assertEquals(4, nonFeaturedCount, "Fashion should have 4 non-featured products");
    }

    @Test
    public void search_byPriceRange_shouldReturnAllProductsInRange()
    {
        // act
        List<Product> actual = productService.search(null, 50.0, 100.0, null);

        // assert
        assertTrue(actual.size() > 0, "Should return products in price range");

        for (Product product : actual)
        {
            assertTrue(product.getPrice() >= 50.0 && product.getPrice() <= 100.0,
                    "Product price should be within the specified range");
        }

        boolean hasFeatured = actual.stream().anyMatch(Product::isFeatured);
        boolean hasNonFeatured = actual.stream().anyMatch(p -> !p.isFeatured());
        assertTrue(hasFeatured && hasNonFeatured, "Should include both featured and non-featured products");
    }

    @Test
    public void search_bySubCategory_shouldReturnAllMatchingProducts()
    {
        // act
        List<Product> actual = productService.search(null, null, null, "Blue");

        // assert
        assertTrue(actual.size() >= 2, "Should find at least 2 products with Blue subcategory");

        for (Product product : actual)
        {
            assertEquals("Blue", product.getSubCategory(),
                    "Product subcategory should match the search criteria");
        }

        boolean hasFeatured = actual.stream().anyMatch(Product::isFeatured);
        boolean hasNonFeatured = actual.stream().anyMatch(p -> !p.isFeatured());
        assertTrue(hasFeatured && hasNonFeatured, "Should include both featured and non-featured products");
    }

    @Test
    public void search_withCombinedFilters_shouldReturnAllMatchingProducts()
    {
        // act
        List<Product> actual = productService.search(2, 45.0, 80.0, null);

        // assert
        assertTrue(actual.size() > 0, "Should return products matching all filters");

        for (Product product : actual)
        {
            assertEquals(2, product.getCategoryId(), "Product should be from Fashion category");
            assertTrue(product.getPrice() >= 45.0 && product.getPrice() <= 80.0,
                    "Product price should be within the specified range");
        }

        boolean hasFeatured = actual.stream().anyMatch(Product::isFeatured);
        boolean hasNonFeatured = actual.stream().anyMatch(p -> !p.isFeatured());
        assertTrue(hasFeatured && hasNonFeatured, "Should include both featured and non-featured products");
    }

    @Test
    public void search_shouldNotExcludeNonFeaturedProducts()
    {
        // act
        List<Product> actual = productService.search(null, null, null, null);

        // assert
        boolean containsSmartphone = actual.stream()
                .anyMatch(p -> p.getProductId() == 1 && !p.isFeatured());
        assertTrue(containsSmartphone, "Search should include non-featured products like Smartphone");
    }

    @Test
    public void listByCategoryId_shouldReturnProductsInCategory()
    {
        // act
        List<Product> actual = productService.listByCategoryId(1);

        // assert
        assertEquals(3, actual.size(), "Electronics category should return 3 products");

        for (Product product : actual)
        {
            assertEquals(1, product.getCategoryId(), "All products should be from category 1");
        }
    }

    @Test
    public void update_shouldSaveEveryEditableProductField()
    {
        // arrange
        Product existing = createProduct(1, "Smartphone", 499.99, 1, "Black", false);
        existing.setDescription("Original description");
        existing.setImageUrl("old-phone.jpg");
        existing.setStock(50);

        Product update = createProduct(1, "Updated Smartphone", 429.99, 2, "Silver", true);
        update.setDescription("Updated description");
        update.setImageUrl("updated-phone.jpg");
        update.setStock(12);

        when(productRepository.findById(1)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Product actual = productService.update(1, update);

        // assert
        assertEquals("Updated Smartphone", actual.getName(), "Updating a product should persist the new name");
        assertEquals(429.99, actual.getPrice(), 0.001, "Updating a product should persist the new price");
        assertEquals(2, actual.getCategoryId(), "Updating a product should persist the new category");
        assertEquals("Updated description", actual.getDescription(), "Updating a product should persist the new description");
        assertEquals("Silver", actual.getSubCategory(), "Updating a product should persist the new subcategory");
        assertEquals(12, actual.getStock(), "Updating a product should persist the new stock value");
        assertTrue(actual.isFeatured(), "Updating a product should persist the new featured value");
        assertEquals("updated-phone.jpg", actual.getImageUrl(), "Updating a product should persist the new image URL");
    }

    private Product createProduct(int id, String name, double price, int categoryId, String subCategory, boolean featured)
    {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        product.setSubCategory(subCategory);
        product.setFeatured(featured);
        return product;
    }
}
