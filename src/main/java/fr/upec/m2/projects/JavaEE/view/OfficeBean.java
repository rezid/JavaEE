package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.business.OfficeService;
import fr.upec.m2.projects.JavaEE.business.ZoneService;
import fr.upec.m2.projects.JavaEE.model.*;
import fr.upec.m2.projects.JavaEE.view.utils.Filter;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class OfficeBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(OfficeBean.class);
    private List<Office> office_list;
    private FilterList filter_list;
    private String zip_code;

    @Inject
    private OfficeService officeService;

    @Inject
    private ZoneService zoneService;

    //------------------------------------------------------------------------------------------------------------------

    // Default c'tor for CDI.
    public OfficeBean() {
        filter_list = new FilterList();
    }

    @PostConstruct
    public void init() {
        update_office_list_by_zip_code("75000");
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<Office> getOffice_list() {
        return office_list;
    }

    public List<Filter> getFilter_list() {
        return filter_list.getFilterList();
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        update_office_list_by_zip_code(zip_code);
        this.zip_code = zip_code;
    }

    //------------------------------------------------------------------------------------------------------------------

    public int getListSize() {
        return office_list.size();
    }

    // 0 = Tout les Arrondissement
    public void update_office_list_by_zip_code(String zip_code) {
        filter_list.addFilter("Code_postale", zip_code);
        this.zip_code = zip_code;

        if (zip_code.equals("75000"))
            office_list = officeService.getAllOffice();
        else
            office_list = officeService.getAllOfficeByDistrict(zip_code);

    }

    //------------------------------------------------------------------------------------------------------------------
}
