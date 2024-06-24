package yoon.docker.mapService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pin_member")
public class PinMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pinMemberIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members members;

    @Builder
    public PinMembers(Pin pin, Members members){
        this.pin = pin;
        this.members = members;
    }

}
