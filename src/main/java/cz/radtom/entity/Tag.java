package cz.radtom.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "tags", schema = "public")
@Getter
public class Tag {

    @Id
    @Column(name = "value")
    private String value;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToMany(mappedBy = "tags")
    private Set<Item> items;

}
