package ro.alexk.backend.entities;

import jakarta.persistence.*;

@Entity
public class Util {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String value;
}
