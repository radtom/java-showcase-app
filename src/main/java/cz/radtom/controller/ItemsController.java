package cz.radtom.controller;

import module java.base;

import cz.radtom.dto.*;
import cz.radtom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    public Page<ItemDto> getItems(@ParameterObject Pageable pageable) {
        return itemsService.getAllItems(pageable);
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

    @GetMapping("/search")
    public Page<ItemDto> searchItems(
            @RequestParam(required = false) Integer value,
            @RequestParam(required = false) SearchOperation operation,
            @RequestParam(required = false) Set<String> tags,
            @ParameterObject Pageable pageable
    ) {
        return itemsService.searchItems(value, operation, tags, pageable);
    }

}
