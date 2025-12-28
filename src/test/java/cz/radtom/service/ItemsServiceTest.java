package cz.radtom.service;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.entity.Item;
import cz.radtom.repository.ItemsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

        when(itemsRepository.findAll()).thenReturn(mockItems);

        // 2. Act: Call the service method
        List<ItemDto> result = itemsService.getAllItems();

        // 3. Assert: Verify the results
        assertNotNull(result, "The returned list should not be null");
        assertEquals(2, result.size(), "The list size should match the mock data");

        // Verify that the repository method was actually called
        verify(itemsRepository).findAll();
    }

    @Test
    void getItemById_WhenFound_ShouldReturnDto() {
        // Arrange
        Long id = 1L;
        Item item = new Item();
        item.setId(id);
        when(itemsRepository.findById(id)).thenReturn(Optional.of(item));

        // Act
        ItemDto result = itemsService.getItemById(id);

        // Assert
        assertNotNull(result);
        verify(itemsRepository).findById(id);
    }

    @Test
    void getItemById_WhenNotFound_ShouldReturnNull() {
        // Arrange
        when(itemsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        ItemDto result = itemsService.getItemById(99L);

        // Assert
        assertNull(result);
    }

    @Test
    void createItem_ShouldReturnSavedItem() {
        // Arrange
        ItemDto dto = new ItemDto(null, 123, null, null,  Set.of("test"));
        Item item = Item.fromDto(dto);
        when(itemsRepository.save(any(Item.class))).thenReturn(item);

        // Act
        Item result = itemsService.createItem(123, Set.of("test"));

        // Assert
        assertNotNull(result);
        verify(itemsRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnSavedItem() {
        // Arrange
        ItemDto dto = new ItemDto(1L, 123, null, null, null);
        Item item = Item.fromDto(dto);
        when(itemsRepository.save(any(Item.class))).thenReturn(item);
        when(itemsRepository.findById(1L)).thenReturn(Optional.of(item));

        // Act
        Item result = itemsService.updateItem(1L, 10);

        // Assert
        assertNotNull(result);
        verify(itemsRepository).save(any(Item.class));
    }

}
