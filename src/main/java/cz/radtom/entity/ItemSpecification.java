package cz.radtom.entity;

import module java.base;

import cz.radtom.dto.SearchOperation;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ItemSpecification {
    public static Specification<Item> hasValue(Integer value, SearchOperation operator) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }
            return switch (operator) {
                case SearchOperation.GT -> cb.greaterThan(root.get("value"), value);
                case SearchOperation.LT -> cb.lessThan(root.get("value"), value);
                case SearchOperation.EQ -> cb.equal(root.get("value"), value);
            };
        };
    }

    public static Specification<Item> hasTags(Set<String> tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) {
                return null;
            }
            return root.join("tags").get("value").in(tags);
        };
    }
}
