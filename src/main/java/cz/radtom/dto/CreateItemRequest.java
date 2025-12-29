package cz.radtom.dto;

import module java.base;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateItemRequest(

        @Schema(description = "The numeric value of the item", example = "42")
        Integer value,

        @Schema(description = "List of tags associated with the item", example = "[\"electronics\", \"sale\"]")
        Set<String> tags
) {
}
