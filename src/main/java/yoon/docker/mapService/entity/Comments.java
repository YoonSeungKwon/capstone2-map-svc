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
@Table(name = "comment")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentIdx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_idx")
    private Members members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_idx")
    private Pin pin;

    @Column(nullable = false, length = 250)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    Comments(Members members, Pin pin, String content){
        this.members = members;
        this.pin = pin;
        this.content = content;
    }


}
