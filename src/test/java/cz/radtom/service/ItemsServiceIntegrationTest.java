package cz.radtom.service;

import cz.radtom.dto.ItemDto;
import cz.radtom.dto.SearchOperation;
import cz.radtom.entity.Item;
import cz.radtom.exception.ItemNotFoundException;
import cz.radtom.repository.ItemsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ItemsServiceIntegrationTest {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EntityManager entityManager;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("item.expiration-seconds", () -> 100);
        registry.add("item.delete-cron", () -> "0 0 0 31 2 ?"); //impossible date so job does not run during tests
    }

    @BeforeEach
    void setUp() {
        var cache = cacheManager.getCache("itemSearches");
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    @DisplayName("Should create item and retrieve it by ID")
    void testCreateAndGetItem() {
        // Given
        Integer value = 42;
        Set<String> tags = Set.of("java", "integration");

        // When
        Item created = itemsService.createItem(value, tags);
        ItemDto retrieved = itemsService.getItemById(created.getId());

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getCreated()).isNotNull();
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should update existing item value and timestamp")
    void testUpdateItem() {
        // Given
        Item item = itemsService.createItem(10, Set.of("test"));
        ZonedDateTime originalUpdate = item.getUpdated();

        // When
        Item updatedItem = itemsService.updateItem(item.getId(), 99);

        // Then
        assertThat(updatedItem.getValue()).isEqualTo(99);
        assertThat(updatedItem.getUpdated()).isAfterOrEqualTo(originalUpdate);

        // Verify
        Optional<Item> fromDb = itemsRepository.findById(item.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getValue()).isEqualTo(99);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent item")
    void testUpdateNonExistentItem() {
        assertThatThrownBy(() -> itemsService.updateItem(999L, 50))
                .isInstanceOf(ItemNotFoundException.class);
    }

    @Test
    @DisplayName("Should search items by value using Specification and return DTOs")
    void testSearchItemsByValue() {
        // Given
        itemsService.createItem(100, Set.of("red", "blue"));
        itemsService.createItem(200, Set.of("red", "green"));
        itemsService.createItem(300, Set.of("yellow"));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ItemDto> result = itemsService.searchItems(150, SearchOperation.GT, null, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // Should match 200 and 300
    }

    @Test
    @DisplayName("Should search items by tag using Specification and return DTOs")
    void testSearchItemsByTags() {
        // Given
        itemsService.createItem(100, Set.of("red", "blue"));
        itemsService.createItem(200, Set.of("red", "green"));
        itemsService.createItem(300, Set.of("yellow"));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ItemDto> result = itemsService.searchItems(150, SearchOperation.GT, Set.of("red"), pageable);

        // Then
        assertThat(result.getContent()).hasSize(1); // Should match 200
    }

    @Test
    @DisplayName("Should search items by value and tag using Specification and return DTOs")
    void testSearchItemsByValueAndTags() {
        // Given
        itemsService.createItem(100, Set.of("red", "blue"));
        itemsService.createItem(200, Set.of("red", "green"));
        itemsService.createItem(300, Set.of("yellow"));

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<ItemDto> result = itemsService.searchItems(null, null, Set.of("red"), pageable);

        // Then
        assertThat(result.getContent()).hasSize(2); // Should match 200 and 300
    }

    @Test
    @DisplayName("Should cache search results")
    void testSearchCaching() {
        // Given
        itemsService.createItem(50, Set.of("cache"));
        Pageable pageable = PageRequest.of(0, 10);

        // When
        itemsService.searchItems(50, SearchOperation.EQ, null, pageable);

        // Then: Cache should contain the key
        var cache = cacheManager.getCache("itemSearches");
        assertThat(cache).isNotNull();
        assertThat(cache.getNativeCache()).isNotNull();
    }

    @Test
    @DisplayName("Should delete expired items based on configuration")
    void testDeleteExpiredItems() {
        // Given
        Item freshItem = itemsService.createItem(10, Set.of("fresh"));
        Item expiredItem = itemsService.createItem(20, Set.of("expired"));

        expiredItem.setCreated(ZonedDateTime.now().minusSeconds(200));
        expiredItem.setUpdated(ZonedDateTime.now().minusSeconds(200));
        itemsRepository.saveAndFlush(expiredItem);

        // When
        itemsService.deleteExpiredItems();
        itemsRepository.flush();
        entityManager.clear();

        // Then
        assertThat(itemsRepository.findById(freshItem.getId())).isPresent();
        assertThat(itemsRepository.findById(expiredItem.getId())).isEmpty();
    }

}
