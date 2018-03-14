package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Point2D {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private @NotNull String coordinates_latitude; // 48,...

    @Column(nullable = false)
    private @NotNull String coordinates_longitude; // 2,...

    public Point2D() {
    }

    public Point2D(@NotNull String coordinates_latitude, @NotNull String coordinates_longitude) {
        this.coordinates_latitude = coordinates_latitude;
        this.coordinates_longitude = coordinates_longitude;
    }

    public String getCoordinates_latitude() {
        return coordinates_latitude;
    }

    public void setCoordinates_latitude(String coordinates_latitude) {
        this.coordinates_latitude = coordinates_latitude;
    }

    public String getCoordinates_longitude() {
        return coordinates_longitude;
    }

    public void setCoordinates_longitude(String coordinates_longitude) {
        this.coordinates_longitude = coordinates_longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
