package cz.radtom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateItemRequest(
        @Schema(description = "Id of the item", example = "42")
        @NotNull(message = "Id is required")
        Long id,
        @Schema(description = "New numeric value of the item", example = "42")
        @NotNull(message = "Value is required")
        Integer value
) {
}
