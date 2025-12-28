package cz.radtom.repository;

import module java.base;

import cz.radtom.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long> {
}
