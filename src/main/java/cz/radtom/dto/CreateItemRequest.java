package cz.radtom.dto;

import module java.base;

public record CreateItemRequest(
        Integer value,
        Set<String> tags
) {
}
