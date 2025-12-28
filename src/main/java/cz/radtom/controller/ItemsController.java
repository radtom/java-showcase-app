package cz.radtom.controller;

import module java.base;

import cz.radtom.dto.CreateItemRequest;
import cz.radtom.dto.ItemDto;
import cz.radtom.dto.UpdateItemRequest;
import cz.radtom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    public List<ItemDto> getItems() {
        return itemsService.getAllItems();
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Long id) {
        return itemsService.getItemById(id);
    }

    @PostMapping
    public Long createItem(@RequestBody CreateItemRequest request) {
        return itemsService.createItem(request.value(), request.tags()).getId();
    }

    @PutMapping
    public Long updateItem(@RequestBody UpdateItemRequest request) {
        return itemsService.updateItem(request.id(), request.value()).getId();
    }
}
