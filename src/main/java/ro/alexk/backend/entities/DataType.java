package ro.alexk.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Name name;

    // references
    @OneToMany(mappedBy = "dataType", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Param> params;
    @OneToMany(mappedBy = "dataType", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Pin> pins;

    public enum Name {
        BOOLEAN, INTEGER, JSON, RGB
    }
}
