package cz.radtom.entity;

import module java.base;


import cz.radtom.dto.ItemDto;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private Integer value;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @ManyToMany(cascade = { CascadeType.PERSIST })
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
        return new ItemDto(id, value, created, updated, tags.stream().map(Tag::getValue).collect(Collectors.toSet()));
    }
}
