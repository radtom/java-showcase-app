package cz.radtom.dto;

import module java.base;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateItemRequest(

        @Schema(description = "The numeric value of the item", example = "42")
        @NotNull(message = "Value is required")
        Integer value,

        @Schema(description = "List of tags associated with the item", example = "[\"electronics\", \"sale\"]")
        Set<@NotBlank @Size(max = 255, message = "Each tag must be under 255 characters")String> tags
) {
}
