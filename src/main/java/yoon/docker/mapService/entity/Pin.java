package yoon.docker.mapService.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import yoon.docker.mapService.enums.Category;
import yoon.docker.mapService.enums.Method;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pin")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pinIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_map")
    private Maps maps;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "pin")
    private List<Comments> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_member")
    private Members members;

    @Column(nullable = false, length = 250)
    private String header;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = true, length = 1000)
    private String memo;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Method method;

    @Column(nullable = false)
    private int cost;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private double latitude;

    private double longitude;

    private String file;



    @Builder
    public Pin(Maps maps, Members members, String header, String title, String memo, Category category, Method method, int cost, double lat, double lon, String file){
        this.maps = maps;
        this.members = members;
        this.header = header;
        this.title = title;
        this.memo = memo;
        this.category = category;
        this.method = method;
        this.cost = cost;
        this.latitude = lat;
        this.longitude = lon;
        this.file = file;
    }

}
