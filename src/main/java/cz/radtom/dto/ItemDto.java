package cz.radtom.dto;

import module java.base;

import java.time.ZonedDateTime;

public record ItemDto(Long id, Integer value, ZonedDateTime created, ZonedDateTime updated, Set<String> tags) {
}
