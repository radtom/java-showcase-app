package cz.radtom.service;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.dto.SearchOperation;
import cz.radtom.entity.Item;
import cz.radtom.entity.Tag;
import cz.radtom.exception.ItemNotFoundException;
import cz.radtom.repository.ItemsRepository;
import cz.radtom.repository.TagsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemsServiceTest {

    @Mock
    private ItemsRepository itemsRepository;

    @Mock
    private TagsRepository tagsRepository;

    @InjectMocks
    private ItemsService itemsService;

    @Test
    @DisplayName("Should return items")
    void getAllItems_ShouldReturnListofItems() {
        // 1. Arrange
        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> mockItems = Arrays.asList(item1, item2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> mockPage = new PageImpl<>(mockItems, pageable, mockItems.size());

        when(itemsRepository.findAll(pageable)).thenReturn(mockPage);

        // 2. Act
        Page<ItemDto> result = itemsService.getAllItems(pageable);

        // 3. Assert
        assertNotNull(result, "The returned list should not be null");
        assertEquals(2, result.getTotalElements(), "The list size should match the mock data");

        verify(itemsRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return item by ID")
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
    @DisplayName("Should throw exception when item not found")
    void getItemById_WhenNotFound_ShouldThrow() {
        // Arrange
        when(itemsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and assert
        assertThrows(ItemNotFoundException.class, () -> itemsService.getItemById(99L));
    }

    @Test
    @DisplayName("Should create item")
    void createItem_ShouldReturnSavedItem() {
        // Arrange
        ItemDto dto = new ItemDto(null, 123, null, null,  Set.of("test"));
        Item item = Item.fromDto(dto);
        Tag tag = new Tag();
        tag.setValue("test");
        when(itemsRepository.save(any(Item.class))).thenReturn(item);
        when(tagsRepository.save(any(Tag.class))).thenReturn(tag);

        // Act
        Item result = itemsService.createItem(123, Set.of("test"));

        // Assert
        assertNotNull(result);
        verify(itemsRepository).save(any(Item.class));
    }

    @Test
    @DisplayName("Should update item")
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

    @Test
    @DisplayName("Should search paginated items")
    void searchItems_ReturnsPageOfDtos() {
        // Arrange
        Integer value = 100;
        SearchOperation operation = SearchOperation.GT;
        Pageable pageable = PageRequest.of(0, 10);

        Item mockItem = new Item();
        mockItem.setValue(100);
        Page<Item> itemPage = new PageImpl<>(List.of(mockItem));

        // We mock the repository to return our page
        // Note: any(Specification.class) is used because Specifications are hard to match exactly
        when(itemsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(itemPage);

        // Act
        Page<ItemDto> result = itemsService.searchItems(value, operation, null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(100, result.getContent().getFirst().value());
        verify(itemsRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Should search empty page")
    void searchItems_ReturnsEmptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(itemsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(Page.empty());

        // Act
        Page<ItemDto> result = itemsService.searchItems(null, null, null, pageable);

        // Assert
        assertTrue(result.isEmpty());
        verify(itemsRepository).findAll(any(Specification.class), eq(pageable));
    }

}
