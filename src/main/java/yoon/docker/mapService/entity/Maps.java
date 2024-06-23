package yoon.docker.mapService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import yoon.docker.mapService.enums.Colors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maps")
public class Maps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mapIdx;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "maps")
    private List<Pin> pins = new ArrayList<>();

    @Column
    private String title;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private Colors colors;

    @ColumnDefault("0")
    private boolean isPrivate;


    @Column(nullable = false, length = 250)
    private LocalDateTime selectedDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Maps(String title, Colors colors, double lat, double lon, boolean isPrivate, LocalDateTime selectedDate){
        this.title = title;
        this.colors = colors;
        this.latitude = lat;
        this.longitude = lon;
        this.isPrivate = isPrivate;
        this.selectedDate = selectedDate;
    }


}
