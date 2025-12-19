package cz.radtom.controller;

import module java.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemsController {

    @GetMapping
    public List<String> getItems() {
        return List.of("Hey", "cute", "bear");
    }
}
