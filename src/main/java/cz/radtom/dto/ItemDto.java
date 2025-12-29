package cz.radtom.dto;

import module java.base;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

public record ItemDto(
        @Schema(description = "Id of the item", example = "42")
        Long id,
        @Schema(description = "The numeric value of the item", example = "42")
        Integer value,
        @Schema(description = "Time when the item was created", example = "2025-12-29T16:15:18.000Z")
        ZonedDateTime created,
        @Schema(description = "Time when the item was updated", example = "2025-12-29T16:15:18.000Z")
        ZonedDateTime updated,
        @Schema(description = "List of tags associated with the item", example = "[\"electronics\", \"sale\"]")
        Set<String> tags
) {
}
