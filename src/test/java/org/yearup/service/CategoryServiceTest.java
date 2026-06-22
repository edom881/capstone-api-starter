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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
