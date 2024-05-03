package yoon.docker.mapService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "privateMap")
public class PrivateMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long privateMapIdx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_privateMap")
    private Members members;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public PrivateMap(Members members){
        this.members = members;
    }

}
