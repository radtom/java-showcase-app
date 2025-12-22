package cz.radtom.service;

import module java.base;

import cz.radtom.entity.Item;
import cz.radtom.repository.ItemsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemsServiceTest {

    @Mock
    private ItemsRepository itemsRepository;

    @InjectMocks
    private ItemsService itemsService;

    @Test
    void getAllItems_ShouldReturnListofItems() {
        // 1. Arrange: Setup mock data and behavior
        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> mockItems = Arrays.asList(item1, item2);

        when(itemsRepository.findAllWithTags()).thenReturn(mockItems);

        // 2. Act: Call the service method
        List<Item> result = itemsService.getAllItems();

        // 3. Assert: Verify the results
        assertNotNull(result, "The returned list should not be null");
        assertEquals(2, result.size(), "The list size should match the mock data");

        // Verify that the repository method was actually called
        verify(itemsRepository).findAllWithTags();
    }

}
