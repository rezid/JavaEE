package fr.upec.m2.projects.JavaEE.view;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import fr.upec.m2.projects.JavaEE.business.ZoneService;
import fr.upec.m2.projects.JavaEE.model.Address;
import fr.upec.m2.projects.JavaEE.model.Zone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@Named
public class MapBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(MapBean.class);
    private MapModel mapModel = new DefaultMapModel();
    private String centerMapCoordinate;
    private List<Zone> zone_list;

    private int state = 0; // we use an automate to make transition

    @Inject
    private ZoneService zoneService;

    @Inject
    private AddressBean addressBean;

    //------------------------------------------------------------------------------------------------------------------

    public MapBean() {
    }

    @PostConstruct
    public void init() {
        zone_list = zoneService.getAllZone();
        putAllOfficeInMapModel();
    }

    //------------------------------------------------------------------------------------------------------------------

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public String getCenterMapCoordinate() {
        return centerMapCoordinate;
    }

    public void setCenterMapCoordinate(String centerMapCoordinate) {
        this.centerMapCoordinate = centerMapCoordinate;
    }

    public List<Zone> getZone_list() {
        return zone_list;
    }

    public void setZone_list(List<Zone> zone_list) {
        this.zone_list = zone_list;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void searchAddressAndShow() {
        List<Address> address_list_found = addressBean.get_exact_address_list();

        LOG.error("searchAddressAndShow() is called.");
        LOG.error("{} address found.", address_list_found.size());

        if (address_list_found.size() == 0)
            return;

        Double lat = address_list_found.get(0).getLatitude_gps_coordinate();
        Double lon = address_list_found.get(0).getLongitude_gps_coordinate();

        if (state == 1) {
            for (Marker marker: mapModel.getMarkers()) {
                LatLng latLng = marker.getLatlng();
                if(latLng.getLng() == lon && latLng.getLat() == lat)
                    return;
            }
        }

        mapModel.getMarkers().clear();
        mapModel.addOverlay(new Marker(new LatLng(lat, lon)));
        centerMapCoordinate = lat + ", " + lon;

        /* Debug only: show all polygon in map
        zone_list.forEach(zone -> {
            org.primefaces.model.map.Polygon polygon = new org.primefaces.model.map.Polygon();
            List<LatLng> lat_lng_list = new ArrayList<>();
            for(int i = 0; i < zone.getPolygon_latitude_gps_coordinate_list().size(); i++)
                lat_lng_list.add(new LatLng(zone.getPolygon_latitude_gps_coordinate_list().get(i),
                        zone.getPolygon_longitude_gps_coordinate_list().get(i)));

            polygon.setPaths(lat_lng_list);
            mapModel.addOverlay(polygon);
        });
        */

        //test
        GeometryFactory geometryFactory = new GeometryFactory();
        Point my_address_point = geometryFactory.createPoint(new Coordinate(lat, lon));

        zone_list.forEach( zone -> {
            List<Coordinate> coordinate_list = new ArrayList<>();
            for(int i = 0; i < zone.getPolygon_latitude_gps_coordinate_list().size(); i++)
                coordinate_list.add(new Coordinate(
                        zone.getPolygon_latitude_gps_coordinate_list().get(i),
                        zone.getPolygon_longitude_gps_coordinate_list().get(i)
                ));
            LinearRing ring = geometryFactory.createLinearRing(coordinate_list.toArray(new Coordinate[0]));
            Polygon polygon = geometryFactory.createPolygon(ring);
            if (polygon.covers(my_address_point)) {
                List<LatLng> lat_lng_list = new ArrayList<>();
                for(int i = 0; i < zone.getPolygon_latitude_gps_coordinate_list().size(); i++)
                    lat_lng_list.add(new LatLng(zone.getPolygon_latitude_gps_coordinate_list().get(i),
                            zone.getPolygon_longitude_gps_coordinate_list().get(i)));

                org.primefaces.model.map.Polygon polygon1 = new org.primefaces.model.map.Polygon();
                polygon1.setPaths(lat_lng_list);
                polygon1.setFillOpacity(0.2);
                mapModel.addOverlay(polygon1);
                mapModel.addOverlay(new Marker(new LatLng(
                                zone.getLatitude_gps_coordinate(),
                                zone.getLongitude_gps_coordinate()), zone.getOffice_number(), null,
                                "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
                LOG.error("contain: {}", polygon.contains(my_address_point));
                LOG.error("covers: {}", polygon.covers(my_address_point));
            }
        });

        state = 1;
        PrimeFaces.current().ajax().update("gmapid");
    }

    public void showAllOffice() {
        if (state == 0)
            return;

        mapModel.getMarkers().clear();
        mapModel.getPolygons().clear();
        putAllOfficeInMapModel();
        state = 0;


        PrimeFaces.current().ajax().update("gmapid");
    }

    //------------------------------------------------------------------------------------------------------------------

    private void putAllOfficeInMapModel() {
        zone_list.forEach(zone -> mapModel.addOverlay(new Marker(new LatLng(
                zone.getLatitude_gps_coordinate(),
                zone.getLongitude_gps_coordinate()), zone.getOffice_number(), null,
                "http://maps.google.com/mapfiles/ms/micons/blue-dot.png")
        ));
        centerMapCoordinate = "48.856614, 2.3522219000000177"; // Paris
    }

    //------------------------------------------------------------------------------------------------------------------
}
