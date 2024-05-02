package yoon.docker.mapService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.docker.mapService.entity.MapMembers;
import yoon.docker.mapService.entity.Maps;
import yoon.docker.mapService.entity.Members;

import java.util.List;

@Repository
public interface MapMemberRepository extends JpaRepository<MapMembers, Long> {

    List<MapMembers> findAllByMaps(Maps maps);
    MapMembers findMapMembersByMapsAndMembers(Maps maps, Members members);

    boolean existsByMaps(Maps maps);
    boolean existsByMapsAndMembers(Maps maps, Members members);

}
