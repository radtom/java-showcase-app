package cz.radtom.service;

import cz.radtom.entity.Item;
import cz.radtom.repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public List<Item> getAllItems() {
        return itemsRepository.findAllWithTags();
    }
}
