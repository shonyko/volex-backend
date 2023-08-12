package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;
    @Column(nullable = false)
    private String name;

    // references
    @OneToMany(mappedBy = "agent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AgentParam> params;
    @OneToMany(mappedBy = "agent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AgentPin> pins;
}
