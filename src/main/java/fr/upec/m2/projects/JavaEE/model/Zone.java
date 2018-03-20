package fr.upec.m2.projects.JavaEE.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Zone.getAllZone",
                query = "SELECT _zone FROM Zone _zone"),
 })
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String office_number; // ex: 18-46

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Double> polygon_latitude_gps_coordinate_list;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Double> polygon_longitude_gps_coordinate_list;

    @Column(nullable = false)
    private @NotNull Double latitude_gps_coordinate; // ex: 48.4598

    @Column(nullable = false)
    private @NotNull Double longitude_gps_coordinate; // ex: 2.6758

    //------------------------------------------------------------------------------------------------------------------

    public Zone() {
    }

    public Zone(String office_number, List<Double> polygon_latitude_gps_coordinate_list, List<Double> polygon_longitude_gps_coordinate_list, @NotNull Double latitude_gps_coordinate, @NotNull Double longitude_gps_coordinate) {
        this.office_number = office_number;
        this.polygon_latitude_gps_coordinate_list = polygon_latitude_gps_coordinate_list;
        this.polygon_longitude_gps_coordinate_list = polygon_longitude_gps_coordinate_list;
        this.latitude_gps_coordinate = latitude_gps_coordinate;
        this.longitude_gps_coordinate = longitude_gps_coordinate;
    }

    //------------------------------------------------------------------------------------------------------------------


    public String getOffice_number() {
        return office_number;
    }

    public void setOffice_number(String office_number) {
        this.office_number = office_number;
    }

    public List<Double> getPolygon_latitude_gps_coordinate_list() {
        return polygon_latitude_gps_coordinate_list;
    }

    public void setPolygon_latitude_gps_coordinate_list(List<Double> polygon_latitude_gps_coordinate_list) {
        this.polygon_latitude_gps_coordinate_list = polygon_latitude_gps_coordinate_list;
    }

    public List<Double> getPolygon_longitude_gps_coordinate_list() {
        return polygon_longitude_gps_coordinate_list;
    }

    public void setPolygon_longitude_gps_coordinate_list(List<Double> polygon_longitude_gps_coordinate_list) {
        this.polygon_longitude_gps_coordinate_list = polygon_longitude_gps_coordinate_list;
    }

    public Double getLatitude_gps_coordinate() {
        return latitude_gps_coordinate;
    }

    public void setLatitude_gps_coordinate(Double latitude_gps_coordinate) {
        this.latitude_gps_coordinate = latitude_gps_coordinate;
    }

    public Double getLongitude_gps_coordinate() {
        return longitude_gps_coordinate;
    }

    public void setLongitude_gps_coordinate(Double longitude_gps_coordinate) {
        this.longitude_gps_coordinate = longitude_gps_coordinate;
    }

    //------------------------------------------------------------------------------------------------------------------
}
