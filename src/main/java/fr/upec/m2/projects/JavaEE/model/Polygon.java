package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Polygon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point2D> point2DList = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> point2DList) {
        this.point2DList = point2DList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Point2D> getPoint2DList() {
        return point2DList;
    }

    public void setPoint2DList(List<Point2D> point2DList) {
        this.point2DList = point2DList;
    }

    @Transient
    public void addPoint(Point2D point2D) {
        point2DList.add(point2D);
    }
}
