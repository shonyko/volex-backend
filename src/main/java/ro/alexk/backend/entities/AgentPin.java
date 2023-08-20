package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentPin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "pin_id")
    private Pin pin;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private String lastValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_pin_id")
    private AgentPin srcPin;

    // references
    @OneToMany(mappedBy = "srcPin")
    private List<AgentPin> connectedPins;
}
