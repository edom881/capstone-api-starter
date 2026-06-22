package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

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
