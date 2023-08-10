package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HwAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Agent agent;
    @Column(unique = true, nullable = false)
    private String macAddr;
}
