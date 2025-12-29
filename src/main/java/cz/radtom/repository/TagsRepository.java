package cz.radtom.repository;

import cz.radtom.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository  extends JpaRepository<Tag, String> {
}
