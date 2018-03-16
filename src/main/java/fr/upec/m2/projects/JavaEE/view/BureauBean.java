package fr.upec.m2.projects.JavaEE.view;

import com.snatik.polygon.Point;
import fr.upec.m2.projects.JavaEE.business.service.AdresseService;
import fr.upec.m2.projects.JavaEE.business.service.BureauService;
import fr.upec.m2.projects.JavaEE.business.service.ZoneService;
import fr.upec.m2.projects.JavaEE.model.*;
import fr.upec.m2.projects.JavaEE.view.utils.Filter;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.map.LatLng;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@ViewScoped
@Named
public class BureauBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LogManager.getLogger(BureauBean.class);

    private String selectedAddress;

    public String getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(String selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    private List<Bureau> bureauList;

    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;

    private FilterList filterList;
    private String code_postale;

    @Inject
    private BureauService bureauService;

    @Inject
    private AdresseService adresseService;

    @Inject
    private ZoneService zoneService;

    // Default c'tor for CDI.
    public BureauBean() {
        filterList = new FilterList();
    }

    @PostConstruct
    public void init() {
        setCodePostale("75000");
        sortByOrder("BY_LIB");
    }

    public List<Bureau> getBureauList() {
        return bureauList;
    }

    public List<Filter> getFilterList() {
        return filterList.getFilterList();
    }

    public int getListSize() {
        return bureauList.size();
    }

    // 0 = Tout les Arrondissement
    public void setCodePostale(String code_postale) {
        this.selectedAddress = "";
        filterList.addFilter("Mon_adresse", selectedAddress);

        this.code_postale = code_postale;
        filterList.addFilter("Code_postale", code_postale);

        if (code_postale.equals("75000"))
            bureauList = bureauService.getAllBureau();
        else
            bureauList = bureauService.getBurreauByCodePostale(code_postale);

        sortByOrder("BY_LIB");
        sortByOrder("BY_LIB");
    }

    public String getCodePostale() {
        return code_postale;
    }

    public void sortByOrder(String order) {
        if (bureauList.isEmpty())
            return;

        switch (order) {
            case "BY_ADR":
                if (is_adr_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getAdresse_bureau));
                    filterList.addFilter("Order", "BY_ADR_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getAdresse_bureau).reversed());
                    filterList.addFilter("Order", "BY_ADR_DSC");
                }
                is_adr_asc = !is_adr_asc;
                break;

            case "BY_LIB":
                if (is_label_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getLabel_bureau));
                    filterList.addFilter("Order", "BY_LIB_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getLabel_bureau).reversed());
                    filterList.addFilter("Order", "BY_LIB_DSC");
                }
                is_label_asc = !is_label_asc;
                break;

            case "BY_CP":
                if (is_cp_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getCode_postal_bureau));
                    filterList.addFilter("Order", "BY_CP_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getCode_postal_bureau).reversed());
                    filterList.addFilter("Order", "BY_CP_DSC");
                }
                is_cp_asc = !is_cp_asc;
                break;

            default:
                break;
        }
    }

    public void searchBureau() {
        LOG.error("Finding bureau for adresse: {}", selectedAddress);

        this.code_postale = "75000";
        filterList.addFilter("Code_postale", code_postale);

        filterList.addFilter("Mon_adresse", selectedAddress);



        String[] list = selectedAddress.split(" -- ");

        if (list.length >= 2 && list[1].length() == 5) {
            String cp = list[1];
            cp = cp.substring(3, cp.length());
            List<Point2D> points = adresseService.getAllcoordinatesByAdresse(list[0], cp);

            if (points.size() == 0) {
                // no result ==> empty list
                bureauList = new ArrayList<>();
                return;
            }

            double lat = Double.valueOf(points.get(0).getCoordinates_latitude());
            double lon = Double.valueOf(points.get(0).getCoordinates_longitude());
            Point point = new Point(lat, lon);


            List<Zone> zoneList = zoneService.getAllZone();
            for (Zone zone : zoneList) {

                Polygon polygon = zone.getPolygon();
                com.snatik.polygon.Polygon.Builder builder = new com.snatik.polygon.Polygon.Builder();

                polygon.getPoint2DList().forEach(p -> {
                    double lat1 = Double.valueOf(p.getCoordinates_latitude());
                    double lon1 = Double.valueOf(p.getCoordinates_longitude());
                    Point point1 = new Point(lat1, lon1);
                    // LOG.error("Point latitude: {}, Point longitude: {}", point1.x, point1.y);
                    builder.addVertex(point1);
                });


                com.snatik.polygon.Polygon poly = builder.build();

                if (poly.contains(point)) {
                    LOG.error("zone found: {}", zone.getNumero_bureau());
                    bureauList = bureauService.getBurreauByNum(zone.getNumero_bureau());
                    return;
                }
            }
        }
        // no result ==> empty list
        bureauList = new ArrayList<>();
    }

    public void cancelSearchBureau() {
        setCodePostale("75000");
    }
}
