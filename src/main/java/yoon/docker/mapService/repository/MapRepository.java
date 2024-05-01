package yoon.docker.mapService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.docker.mapService.entity.Maps;

@Repository
public interface MapRepository extends JpaRepository<Maps, Long> {
}
