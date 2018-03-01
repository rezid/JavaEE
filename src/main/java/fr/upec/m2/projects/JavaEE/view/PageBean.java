package fr.upec.m2.projects.JavaEE.view;

import fr.upec.m2.projects.JavaEE.annotation.Trace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@Dependent
@Trace
public class PageBean {
    private static final Logger LOG = LogManager.getLogger();
    private static final Map<String, PageBean> PAGES = new ConcurrentHashMap<>();

    private PageBean current;
    private String path;
    private String name;
    private boolean home;

    @Inject
    private FacesContext facesContext;

    // Default c'tor for CDI.
    public PageBean() {
    }

    @PostConstruct
    public void init() {
        String viewId = facesContext.getViewRoot().getViewId(); // Exemple: viewId = /test/home.xhtml
        current = get(viewId.substring(1, viewId.lastIndexOf('.')));

        // print all PAGES in log
        if(LOG.isDebugEnabled()) {
            LOG.debug("PAGES = {");
            PAGES.forEach((s, pageBean) -> {
                LOG.debug("  path = {}", s);
            });
            LOG.debug("}");
            LOG.debug("Current path is {}", current.path);
        }
    }

    public PageBean get(String path) {
        return PAGES.computeIfAbsent(path, k -> new PageBean(path));
    }


    public PageBean(String path) {
        this.path = path;
        this.name = path;
        this.home = name.equals("home");
        current = this;
    }

    public boolean is(String path) {
        return path.equals(current.path);
    }

    public String getPath() {
        return current.path;
    }

    public String getName() {
        return current.name;
    }

    public boolean isHome() {
        return current.home;
    }

}
