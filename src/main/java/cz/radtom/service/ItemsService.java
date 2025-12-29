package cz.radtom.service;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.dto.SearchOperation;
import cz.radtom.entity.Item;
import cz.radtom.entity.ItemSpecification;
import cz.radtom.entity.Tag;
import cz.radtom.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public Page<ItemDto> getAllItems(Pageable pageable) {
        return itemsRepository.findAll(pageable).map(Item::toDto);
    }

    public ItemDto getItemById(Long id) {
        return itemsRepository.findById(id).map(Item::toDto).orElse(null);
    }

    public Item createItem(Integer value, Set<String> tags) {
        Item item = new Item();
        item.setValue(value);
        item.setTags(Tag.fromStringSet(tags));
        item.setCreated(ZonedDateTime.now());

        return itemsRepository.save(item);
    }

    public Item updateItem(Long id, Integer value) {
        Item item = itemsRepository.findById(id).orElseThrow(NoSuchElementException::new);
        item.setValue(value);
        item.setUpdated(ZonedDateTime.now());
        return itemsRepository.save(item);
    }

    @Cacheable(value = "itemSearches", key = "{#value, #operation, #tags, #pageable}")
    public Page<ItemDto> searchItems(Integer value, SearchOperation operation, Set<String> tags, Pageable pageable) {
        return itemsRepository.findAll(
                Specification.where(ItemSpecification.hasValue(value, operation))
                        .and(ItemSpecification.hasTags(tags)), pageable).map(Item::toDto);
    }

}
