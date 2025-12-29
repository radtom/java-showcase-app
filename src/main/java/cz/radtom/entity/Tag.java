package cz.radtom.entity;

import module java.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tags", schema = "public")
@Getter
@Setter
public class Tag {

    @Id
    @Column(name = "`value`")
    private String value;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToMany(mappedBy = "tags")
    private Set<Item> items;

    public static Tag of(String value) {
        Tag tag = new Tag();
        tag.setValue(value);
        return tag;
    }
}
