package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Blueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private int noInputPins;
    @Column(nullable = false)
    private int noOutputPins;
    // TODO: is this needed? we can get count of params
    @Column(nullable = false)
    private int noParams;
    @Column(nullable = false)
    private boolean isHardware;
    @Column(nullable = false)
    private boolean isValid;

    // references
    @OneToMany(mappedBy = "blueprint", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Param> params;
    @OneToMany(mappedBy = "blueprint", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Agent> agents;
}
