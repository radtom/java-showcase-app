package cz.radtom.controller;

import module java.base;

import cz.radtom.dto.ItemDto;
import cz.radtom.entity.Item;
import cz.radtom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    public List<ItemDto> getItems() {
        return itemsService.getAllItems().stream().map(Item::toDto).toList();
    }
}
