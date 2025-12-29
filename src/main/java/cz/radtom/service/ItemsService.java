package cz.radtom.service;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.dto.SearchOperation;
import cz.radtom.entity.Item;
import cz.radtom.entity.ItemSpecification;
import cz.radtom.entity.Tag;
import cz.radtom.exception.ItemNotFoundException;
import cz.radtom.repository.ItemsRepository;
import cz.radtom.repository.TagsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;
    private final TagsRepository tagsRepository;

    @Value("${item.expiration-seconds}")
    private Integer expirationSeconds;

    public Page<ItemDto> getAllItems(Pageable pageable) {
        return itemsRepository.findAll(pageable).map(Item::toDto);
    }

    public ItemDto getItemById(Long id) {
        return itemsRepository.findById(id).map(Item::toDto).orElseThrow(
                () -> new ItemNotFoundException(id));
    }

    /**
     * Creates the new Item. Fetches existing tags and creates new tags.
     * @param value Value of the item
     * @param tags Set of tags
     * @return Id of the created Item
     */
    public Item createItem(Integer value, Set<String> tags) {
        Item item = new Item();
        item.setValue(value);
        Set<Tag> managedTags = tags.stream()
                .map(name -> tagsRepository.findById(name) // Look for existing tag
                        .orElseGet(() -> {
                            Tag newTag = new Tag(); // Create if not found
                            newTag.setValue(name);
                            return tagsRepository.save(newTag);
                        }))
                .collect(Collectors.toSet());
        item.setTags(managedTags);
        item.setCreated(ZonedDateTime.now());
        item.setUpdated(item.getCreated());

        return itemsRepository.save(item);
    }

    /**
     * Updates given item
     * @param id Id of the item
     * @param value New value of the item
     * @return Id of the Item
     */
    public Item updateItem(Long id, Integer value) {
        Item item = itemsRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        item.setValue(value);
        item.setUpdated(ZonedDateTime.now());
        return itemsRepository.save(item);
    }

    /**
     * Search items based on criteria. Cached using coffee cache
     * with configure TTL -> no explicit cache evicts on insert and update
     * @param value search by value, must be in combination with operation
     * @param operation either GT, LT, EQ
     * @param tags list of tags - will return all items that have at least one. Case sensitive.
     * @param pageable allows pagination
     * @return Search result
     */
    @Cacheable(value = "itemSearches", key = "{#value, #operation, #tags, #pageable}")
    public Page<ItemDto> searchItems(Integer value, SearchOperation operation, Set<String> tags, Pageable pageable) {
        return itemsRepository.findAll(
                Specification.where(ItemSpecification.hasValue(value, operation))
                        .and(ItemSpecification.hasTags(tags)), pageable).map(Item::toDto);
    }

    /**
     * Scheduled task for deleting expired items - older than item.expiration-seconds, based o cron expression
     */
    @Scheduled(cron = "${item.delete-cron}")
    @Transactional
    public void deleteExpiredItems() {
        ZonedDateTime cutoff = ZonedDateTime.now().minusSeconds(expirationSeconds);
        itemsRepository.deleteOlderThan(cutoff);
    }

}
