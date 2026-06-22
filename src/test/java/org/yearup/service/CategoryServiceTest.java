package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest
{
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void getAllCategories_shouldReturnAllCategories()
    {
        // arrange
        List<Category> categories = Arrays.asList(
                new Category(1, "Electronics", "Explore the latest gadgets and electronic devices."),
                new Category(2, "Fashion", "Discover trendy clothing and accessories for men and women."),
                new Category(3, "Home & Kitchen", "Find everything you need to decorate and equip your home.")
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        // act
        List<Category> actual = categoryService.getAllCategories();

        // assert
        assertEquals(3, actual.size(), "Should return all categories");
        assertEquals("Electronics", actual.get(0).getName(), "First category should be Electronics");
    }

    @Test
    public void getById_withValidId_shouldReturnCategory()
    {
        // arrange
        Category category = new Category(1, "Electronics", "Explore the latest gadgets and electronic devices.");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // act
        Category actual = categoryService.getById(1);

        // assert
        assertEquals("Electronics", actual.getName(), "Should return the category with id 1");
    }

    @Test
    public void getById_withInvalidId_shouldReturnNull()
    {
        // arrange
        when(categoryRepository.findById(99999)).thenReturn(Optional.empty());

        // act
        Category actual = categoryService.getById(99999);

        // assert
        assertNull(actual, "Missing category id should return null");
    }

    @Test
    public void create_shouldSaveNewCategory()
    {
        // arrange
        Category category = new Category(99, "Books", "Read books from different categories.");
        Category saved = new Category(4, "Books", "Read books from different categories.");

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        // act
        Category actual = categoryService.create(category);

        // assert
        assertEquals(4, actual.getCategoryId(), "Saved category should include the new id");
        assertEquals("Books", actual.getName(), "Saved category should keep the category name");
    }
}
