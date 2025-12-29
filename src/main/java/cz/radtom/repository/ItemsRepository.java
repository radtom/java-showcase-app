package cz.radtom.repository;

import module java.base;

import cz.radtom.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    @Modifying
    @Query("DELETE FROM Item i WHERE i.updated < :cutoff")
    void deleteOlderThan(ZonedDateTime cutoff);
}
