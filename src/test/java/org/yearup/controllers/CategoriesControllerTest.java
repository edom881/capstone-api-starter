package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.yearup.models.Category;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
