package cz.radtom.controller;

import module java.base;

import cz.radtom.dto.*;
import cz.radtom.service.ItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Main Rest controller of the application handling operations with Items
 */
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    @Operation(summary = "List all items", description = "Retrieves a paginated list of all items in the database.")
    public Page<ItemDto> getItems(@ParameterObject Pageable pageable) {
        return itemsService.getAllItems(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get item by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found the item"),
                    @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
            }
    )
    public ItemDto getItem(@PathVariable Long id) {
        return itemsService.getItemById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new item", description = "Creates a record and returns the new unique identifier.")
    public Long createItem(@RequestBody CreateItemRequest request) {
        return itemsService.createItem(request.value(), request.tags()).getId();
    }

    @PutMapping
    @Operation(summary = "Update an item", description = "Updates an existing item's value based on the provided ID.")
    public Long updateItem(@RequestBody UpdateItemRequest request) {
        return itemsService.updateItem(request.id(), request.value()).getId();
    }

    @GetMapping("/search")
    @Operation(summary = "Search items with filters", description = "Filter items by numerical value, comparison operators, and tags.")
    public Page<ItemDto> searchItems(
            @RequestParam(required = false) Integer value,
            @RequestParam(required = false) SearchOperation operation,
            @RequestParam(required = false) Set<String> tags,
            @ParameterObject Pageable pageable
    ) {
        return itemsService.searchItems(value, operation, tags, pageable);
    }

}
