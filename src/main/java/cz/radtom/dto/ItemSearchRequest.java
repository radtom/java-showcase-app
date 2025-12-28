package cz.radtom.dto;

import module java.base;

public record ItemSearchRequest(Integer value, SearchOperation searchOperation, List<String> tags) {
}
