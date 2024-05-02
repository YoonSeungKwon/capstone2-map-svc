package yoon.docker.mapService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "map_member")
public class MapMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mapMembersIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_map_member_members")
    private Members members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_map_member_maps")
    private Maps maps;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    MapMembers(Members members, Maps maps){
        this.maps = maps;
        this.members = members;
    }

}
