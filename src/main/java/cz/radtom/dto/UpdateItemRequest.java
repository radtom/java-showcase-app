package cz.radtom.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateItemRequest(
        @Schema(description = "Id of the item", example = "42")
        Long id,
        @Schema(description = "New numeric value of the item", example = "42")
        Integer value
) {
}
