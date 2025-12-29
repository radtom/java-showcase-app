package cz.radtom.entity;

import module java.base;


import cz.radtom.dto.ItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "items", schema = "public")
@Setter
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "`value`")
    private Integer value;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "items_tags",
            schema = "public",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_value")
    )
    private Set<Tag> tags;

    @Version
    @Column(name = "version")
    private Long version;

    public ItemDto toDto() {
        Set<String> tagValues = Set.of();
        if (tags != null) {
            tagValues = tags.stream().map(Tag::getValue).collect(Collectors.toSet());
        }
        return new ItemDto(id, value, created, updated, tagValues);
    }

    public static Item fromDto(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.id());
        item.setValue(dto.value());
        item.setCreated(dto.created());
        item.setUpdated(dto.updated());
        if (dto.tags() != null) {
            item.setTags(dto.tags().stream().map(Tag::of).collect(Collectors.toSet()));
        }
        return item;
    }
}
