package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.business.ZoneService;
import fr.upec.m2.projects.JavaEE.model.Zone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.map.*;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@ViewScoped
@Named
public class MapBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(MapBean.class);
    private MapModel mapModel = new DefaultMapModel();

    @Inject
    private ZoneService zoneService;

    //------------------------------------------------------------------------------------------------------------------

    public MapBean() {
    }

    @PostConstruct
    public void init() {
        List<Zone> zone_list = zoneService.getAllZone();
        zone_list.forEach(zone -> mapModel.addOverlay(new Marker(new LatLng(
                                    zone.getLatitude_gps_coordinate(),
                                    zone.getLongitude_gps_coordinate()), zone.getOffice_number())
        ));
    }

    //------------------------------------------------------------------------------------------------------------------

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    //------------------------------------------------------------------------------------------------------------------
}
