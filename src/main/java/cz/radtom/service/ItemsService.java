package cz.radtom.service;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.entity.Item;
import cz.radtom.entity.Tag;
import cz.radtom.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public List<ItemDto> getAllItems() {
        return itemsRepository.findAll().stream().map(Item::toDto).toList();
    }

    public ItemDto getItemById(Long id) {
        return itemsRepository.findById(id).map(Item::toDto).orElse(null);
    }

    public Item createItem(Integer value, Set<String> tags) {
        Item item = new Item();
        item.setValue(value);
        if (tags != null) {
            item.setTags(tags.stream().map(Tag::of).collect(Collectors.toSet()));
        }
        item.setCreated(ZonedDateTime.now());

        return itemsRepository.save(item);
    }

    public Item updateItem(Long id, Integer value) {
        Item item = itemsRepository.findById(id).orElseThrow(NoSuchElementException::new);
        item.setValue(value);
        item.setUpdated(ZonedDateTime.now());
        return itemsRepository.save(item);
    }

}
