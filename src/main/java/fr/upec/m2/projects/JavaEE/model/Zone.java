package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Zone.getAllZone",
                query = "SELECT _zone FROM Zone _zone"),
 })
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(nullable=false)
    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private Polygon polygon;

    @Column(nullable = false)
    private @NotNull String numero_bureau;

    public Zone() {
    }

    public Zone(Polygon polygon, @NotNull String numero_bureau) {
        this.polygon = polygon;
        this.numero_bureau = numero_bureau;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public String getNumero_bureau() {
        return numero_bureau;
    }

    public void setNumero_bureau(String numero_bureau) {
        this.numero_bureau = numero_bureau;
    }
}
