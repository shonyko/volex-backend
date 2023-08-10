package ro.alexk.backend.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ParamType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Name name;

    // references
    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Param> params;

    public enum Name {
        INTEGER, STRING, BOOLEAN
    }
}
