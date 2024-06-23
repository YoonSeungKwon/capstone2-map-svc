package yoon.docker.mapService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yoon.docker.mapService.entity.MapMembers;
import yoon.docker.mapService.entity.Maps;
import yoon.docker.mapService.entity.Members;

import java.util.List;

@Repository
public interface MapMemberRepository extends JpaRepository<MapMembers, Long> {

    boolean existsByMaps(Maps maps);
    List<MapMembers> findAllByMaps(Maps maps);

    List<MapMembers> findAllByMembers(Members members);

    MapMembers findMapMembersByMapsAndMembers(Maps maps, Members members);

    @Query("SELECT mm FROM MapMembers mm WHERE mm.maps.isPrivate = :isPrivate AND mm.members = :Members")
    List<MapMembers> findMapMembersByMembersAndMaps_Private(@Param("Members") Members members, @Param("isPrivate") boolean isPrivate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByMapsAndMembers(Maps maps, Members members);

}
