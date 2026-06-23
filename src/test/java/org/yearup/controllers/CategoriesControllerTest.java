package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoriesControllerTest
{
    @Test
    public void getAll_shouldReturnAllCategoriesFromService()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);

        List<Category> categories = Arrays.asList(
                new Category(1, "Electronics", "Explore the latest gadgets and electronic devices."),
                new Category(2, "Fashion", "Discover trendy clothing and accessories for men and women."),
                new Category(3, "Home & Kitchen", "Find everything you need to decorate and equip your home.")
        );

        when(categoryService.getAllCategories()).thenReturn(categories);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        List<Category> actual = controller.getAll();

        // assert
        assertEquals(3, actual.size(), "Controller should return all categories from the service");
        assertEquals("Fashion", actual.get(1).getName(), "Second category should be Fashion");
    }

    @Test
    public void getById_withValidId_shouldReturnCategoryFromService()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        Category category = new Category(1, "Electronics", "Explore the latest gadgets and electronic devices.");

        when(categoryService.getById(1)).thenReturn(category);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        Category actual = controller.getById(1);

        // assert
        assertEquals("Electronics", actual.getName(), "Controller should return the category from the service");
    }

    @Test
    public void getById_withInvalidId_shouldThrowNotFound()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);

        when(categoryService.getById(99999)).thenReturn(null);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getById(99999));

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Missing category should return 404");
    }

    @Test
    public void getProductsById_shouldReturnProductsFromService()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);

        List<Product> products = Arrays.asList(
                createProduct(1, "Smartphone", 499.99, 1),
                createProduct(2, "Laptop", 899.99, 1)
        );

        when(productService.listByCategoryId(1)).thenReturn(products);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        List<Product> actual = controller.getProductsById(1);

        // assert
        assertEquals(2, actual.size(), "Controller should return products from the selected category");
        assertEquals("Smartphone", actual.get(0).getName(), "First product should be Smartphone");
    }

    @Test
    public void getProductsById_withEmptyCategory_shouldReturnEmptyList()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);

        when(productService.listByCategoryId(99999)).thenReturn(Collections.emptyList());

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        List<Product> actual = controller.getProductsById(99999);

        // assert
        assertEquals(0, actual.size(), "Category with no products should return an empty list");
    }

    @Test
    public void addCategory_shouldReturnCreatedCategory()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        Category category = new Category(0, "Books", "Read books from different categories.");
        Category saved = new Category(4, "Books", "Read books from different categories.");

        when(categoryService.create(category)).thenReturn(saved);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        ResponseEntity<Category> response = controller.addCategory(category);

        // assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Adding a category should return 201 Created");
        assertEquals(4, response.getBody().getCategoryId(), "Saved category should include the new id");
        assertEquals("Books", response.getBody().getName(), "Saved category should keep the category name");
    }

    @Test
    public void addCategory_shouldRequireAdminRole() throws NoSuchMethodException
    {
        // arrange
        Method method = CategoriesController.class.getMethod("addCategory", Category.class);

        // act
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);

        // assert
        assertEquals("hasRole('ROLE_ADMIN')", annotation.value(), "Adding a category should require admin role");
    }

    @Test
    public void updateCategory_withValidId_shouldReturnUpdatedCategory()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        Category category = new Category(1, "Updated Electronics", "Updated category description.");

        when(categoryService.getById(1)).thenReturn(new Category(1, "Electronics", "Old description."));
        when(categoryService.update(1, category)).thenReturn(category);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        Category actual = controller.updateCategory(1, category);

        // assert
        assertEquals(" Electronics", actual.getName(), "Controller should return the updated category name");
        assertEquals(" category description.", actual.getDescription(), "Controller should return the updated description");
    }

    @Test
    public void updateCategory_withInvalidId_shouldThrowNotFound()
    {
        // arrange
        CategoryService categoryService = mock(CategoryService.class);
        ProductService productService = mock(ProductService.class);
        Category category = new Category(99999, "Missing", "Missing category.");

        when(categoryService.getById(99999)).thenReturn(null);

        CategoriesController controller = new CategoriesController(categoryService, productService);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.updateCategory(99999, category));

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Missing category should return 404");
    }

    @Test
    public void updateCategory_shouldRequireAdminRole() throws NoSuchMethodException
    {
        // arrange
        Method method = CategoriesController.class.getMethod("updateCategory", int.class, Category.class);

        // act
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);

        // assert
        assertEquals("hasRole('ROLE_ADMIN')", annotation.value(), "Updating a category should require admin role");
    }

    private Product createProduct(int id, String name, double price, int categoryId)
    {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        return product;
    }
}
