package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Param {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private ParamType type;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;
    @Column(nullable = false)
    private String defaultValue;
}
