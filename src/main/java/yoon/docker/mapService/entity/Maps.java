package yoon.docker.mapService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yoon.docker.mapService.enums.Category;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maps")
public class Maps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mapIdx;

    @ManyToOne
    @JoinColumn(name = "map_member")
    private MapMembers mapMembers;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Maps(String title, Category category){
        this.category = category;
        this.title = title;
    }

}
