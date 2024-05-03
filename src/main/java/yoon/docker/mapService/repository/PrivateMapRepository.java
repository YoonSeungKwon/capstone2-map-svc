package yoon.docker.mapService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.docker.mapService.entity.PrivateMap;

@Repository
public interface PrivateMapRepository extends JpaRepository<PrivateMap, Long> {

}
