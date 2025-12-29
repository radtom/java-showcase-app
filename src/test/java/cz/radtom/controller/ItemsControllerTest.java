package cz.radtom.controller;

import cz.radtom.dto.*;
import cz.radtom.entity.Item;
import cz.radtom.exception.ItemNotFoundException;
import cz.radtom.service.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ItemsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemsService itemsService;

    @InjectMocks
    private ItemsController itemsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemsController)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver()
                )
                .build();
    }

    @Test
    @DisplayName("Should list all the items")
    void testGetItems() throws Exception {
        // Prepare test data
        ItemDto itemDto = new ItemDto(1L, 10, ZonedDateTime.now(), ZonedDateTime.now(), Set.of("tag1"));
        Page<ItemDto> page = new PageImpl<>(Collections.singletonList(itemDto), PageRequest.of(0, 10), 1);

        // Mock service call
        when(itemsService.getAllItems(any())).thenReturn(page);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/item")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].value").value(10))
                .andExpect(jsonPath("$.content[0].tags[0]").value("tag1"));
    }

    @Test
    @DisplayName("Should retrieve item by id")
    void testGetItemById() throws Exception {
        // Prepare test data
        ItemDto itemDto = new ItemDto(1L, 10, ZonedDateTime.now(), ZonedDateTime.now(), Set.of("tag1"));

        // Mock service call
        when(itemsService.getItemById(1L)).thenReturn(itemDto);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/item/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.value").value(10))
                .andExpect(jsonPath("$.tags[0]").value("tag1"));
    }

    @Test
    @DisplayName("Should return 404 when not found")
    void testGetItemById_NotFound() throws Exception {
        // Mock service call
        when(itemsService.getItemById(99L)).thenThrow(new ItemNotFoundException(99L));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/item/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create new item")
    void testCreateItem() throws Exception {
        Item createdItem = new Item();
        createdItem.setId(1L);

        // Mock service call
        when(itemsService.createItem(any(), any())).thenReturn(createdItem);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 10, \"tags\": [\"tag1\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @DisplayName("Should update item")
    void testUpdateItem() throws Exception {
        Item updatedItem = new Item();
        updatedItem.setId(1L);

        // Mock service call
        when(itemsService.updateItem(1L, 10)).thenReturn(updatedItem);

        // Perform the PUT request and verify the response
        mockMvc.perform(put("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"value\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @DisplayName("Should return searched items")
    void testSearchItems() throws Exception {
        // Prepare test data
        ItemDto itemDto = new ItemDto(1L, 10, ZonedDateTime.now(), ZonedDateTime.now(), Set.of("tag1"));
        Page<ItemDto> page = new PageImpl<>(Collections.singletonList(itemDto), PageRequest.of(0, 10), 1);

        // Mock service call
        when(itemsService.searchItems(any(), any(), any(), any())).thenReturn(page);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/item/search")
                        .param("value", "10")
                        .param("operation", "EQ")
                        .param("tags", "tag1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].value").value(10))
                .andExpect(jsonPath("$.content[0].tags[0]").value("tag1"));
    }

}