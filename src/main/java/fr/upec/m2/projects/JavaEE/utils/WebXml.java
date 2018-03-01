package fr.upec.m2.projects.JavaEE.utils;


import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;


import static fr.upec.m2.projects.JavaEE.utils.Xml.createDocument;
import static fr.upec.m2.projects.JavaEE.utils.Xml.getNodeList;
import static fr.upec.m2.projects.JavaEE.utils.Xml.getTextContent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public enum WebXml {

    INSTANCE;

    private static final Logger logger = LogManager.getLogger(WebXml.class.getName());

    private static final String WEB_XML = "/WEB-INF/web.xml";
    private static final String WEB_FRAGMENT_XML = "META-INF/web-fragment.xml";

    private static final String XPATH_WELCOME_FILE =
            "welcome-file-list/welcome-file";
    private static final String XPATH_EXCEPTION_TYPE =
            "error-page/exception-type";
    private static final String XPATH_LOCATION =
            "location";
    private static final String XPATH_ERROR_PAGE_500_LOCATION =
            "error-page[error-code=500]/location";
    private static final String XPATH_ERROR_PAGE_DEFAULT_LOCATION =
            "error-page[not(error-code) and not(exception-type)]/location";
    private static final String XPATH_FORM_LOGIN_PAGE =
            "login-config[auth-method='FORM']/form-login-config/form-login-page";
    private static final String XPATH_FORM_ERROR_PAGE =
            "login-config[auth-method='FORM']/form-login-config/form-error-page";
    private static final String XPATH_DEFAULT_FORM_LOGIN_PAGE =
            "login-config/form-login-config/form-login-page";
    private static final String XPATH_DEFAULT_FORM_ERROR_PAGE =
            "login-config/form-login-config/form-error-page";
    private static final String XPATH_SECURITY_CONSTRAINT =
            "security-constraint";
    private static final String XPATH_WEB_RESOURCE_URL_PATTERN =
            "web-resource-collection/url-pattern";
    private static final String XPATH_AUTH_CONSTRAINT =
            "auth-constraint";
    private static final String XPATH_AUTH_CONSTRAINT_ROLE_NAME =
            "auth-constraint/role-name";
    private static final String XPATH_SESSION_TIMEOUT =
            "session-config/session-timeout";

    private static final String ERROR_NOT_INITIALIZED =
            "WebXml is not initialized yet. Please use #init(ServletContext) method to manually initialize it.";
    private static final String ERROR_URL_MUST_START_WITH_SLASH =
            "URL must start with '/': '%s'";
    private static final String LOG_INITIALIZATION_ERROR =
            "WebXml failed to initialize. Perhaps your web.xml contains a typo?";

    // Properties -----------------------------------------------------------------------------------------------------

    private final AtomicBoolean initialized = new AtomicBoolean();
    private List<String> welcomeFiles;
    private Map<Class<Throwable>, String> errorPageLocations;
    private String formLoginPage;
    private String formErrorPage;
    private Map<String, Set<String>> securityConstraints;
    private int sessionTimeout;

    // Init -----------------------------------------------------------------------------------------------------------

    /**
     * Perform automatic initialization whereby the servlet context is obtained from the faces context.
     * TODO: obtain it from CDI instead a la FacesConfigXml.
     */
    private void init() {
        if (!initialized.get() && hasContext()) {
            init(getServletContext());
        }
    }

    public WebXml init(ServletContext servletContext) {
        if (servletContext != null && !initialized.getAndSet(true)) {
            try {
                Element webXml = loadWebXml(servletContext).getDocumentElement();
                XPath xpath = XPathFactory.newInstance().newXPath();
                welcomeFiles = parseWelcomeFiles(webXml, xpath);
                errorPageLocations = parseErrorPageLocations(webXml, xpath);
                formLoginPage = parseFormLoginPage(webXml, xpath);
                formErrorPage = parseFormErrorPage(webXml, xpath);
                securityConstraints = parseSecurityConstraints(webXml, xpath);
                sessionTimeout = parseSessionTimeout(webXml, xpath);
            }
            catch (Exception e) {
                initialized.set(false);
                logger.error(LOG_INITIALIZATION_ERROR + ": {}", e.getMessage());
                throw new UnsupportedOperationException(e);
            }
        }

        return this;
    }

    public String findErrorPageLocation(Throwable exception) {
        checkInitialized();
        String location = null;

        for (Class<?> cls = exception.getClass(); cls != null && location == null; cls = cls.getSuperclass()) {
            location = errorPageLocations.get(cls);
        }

        return (location == null) ? errorPageLocations.get(null) : location;
    }

    public boolean isAccessAllowed(String url, String role) {
        checkInitialized();

        if (url.charAt(0) != ('/')) {
            throw new IllegalArgumentException(format(ERROR_URL_MUST_START_WITH_SLASH, url));
        }

        String uri = url;

        if (url.length() > 1 && url.charAt(url.length() - 1) == '/') {
            uri = url.substring(0, url.length() - 1); // Trim trailing slash.
        }

        Set<String> roles = findExactMatchRoles(uri);

        if (roles.isEmpty()) {
            roles = findPrefixMatchRoles(uri);
        }

        if (roles.isEmpty()) {
            roles = findSuffixMatchRoles(uri);
        }

        return isRoleMatch(roles, role);
    }

    private Set<String> findExactMatchRoles(String url) {
        for (Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
            if (isExactMatch(entry.getKey(), url)) {
                return entry.getValue();
            }
        }

        return emptySet();
    }

    private Set<String> findPrefixMatchRoles(String url) {
        String urlMatch = "";

        for (String path = url; !path.isEmpty(); path = path.substring(0, path.lastIndexOf('/'))) {
            Set<String> roles = null;

            for (Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
                if (urlMatch.length() < entry.getKey().length() && isPrefixMatch(entry.getKey(), path)) {
                    urlMatch = entry.getKey();
                    roles = entry.getValue();
                }
            }

            if (roles != null) {
                return roles;
            }
        }

        return emptySet();
    }

    private Set<String> findSuffixMatchRoles(String url) {
        if (url.contains(".")) {
            for (Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
                if (isSuffixMatch(url, entry.getKey())) {
                    return entry.getValue();
                }
            }
        }

        return emptySet();
    }

    private static boolean isExactMatch(String urlPattern, String url) {
        return url.equals(urlPattern.endsWith("/*") ? urlPattern.substring(0, urlPattern.length() - 2) : urlPattern);
    }

    private static boolean isPrefixMatch(String urlPattern, String url) {
        return urlPattern.endsWith("/*") && (url + "/").startsWith(urlPattern.substring(0, urlPattern.length() - 1));
    }

    private static boolean isSuffixMatch(String urlPattern, String url) {
        return urlPattern.startsWith("*.") && url.endsWith(urlPattern.substring(1));
    }

    private static boolean isRoleMatch(Set<String> roles, String role) {
        return roles.isEmpty() || roles.contains(role) || (role != null && roles.contains("*"));
    }

    // Getters --------------------------------------------------------------------------------------------------------

    public List<String> getWelcomeFiles() {
        checkInitialized();
        return welcomeFiles;
    }

    public Map<Class<Throwable>, String> getErrorPageLocations() {
        checkInitialized();
        return errorPageLocations;
    }

    public String getFormLoginPage() {
        checkInitialized();
        return formLoginPage;
    }

    public String getFormErrorPage() {
        checkInitialized();
        return formErrorPage;
    }

    public Map<String, Set<String>> getSecurityConstraints() {
        checkInitialized();
        return securityConstraints;
    }

    public int getSessionTimeout() {
        checkInitialized();
        return sessionTimeout;
    }

    private void checkInitialized() {
        init();

        if (!initialized.get()) {
            throw new IllegalStateException(ERROR_NOT_INITIALIZED);
        }
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    private static Document loadWebXml(ServletContext context) throws IOException, SAXException {
        List<URL> webXmlURLs = new ArrayList<>();
        webXmlURLs.add(context.getResource(WEB_XML));
        webXmlURLs.addAll(Collections.list(Thread.currentThread().getContextClassLoader().getResources(WEB_FRAGMENT_XML)));
        return createDocument(webXmlURLs);
    }

    private static List<String> parseWelcomeFiles(Element webXml, XPath xpath) throws XPathExpressionException {
        NodeList welcomeFileList = getNodeList(webXml, xpath, XPATH_WELCOME_FILE);
        List<String> welcomeFiles = new ArrayList<>(welcomeFileList.getLength());

        for (int i = 0; i < welcomeFileList.getLength(); i++) {
            welcomeFiles.add(getTextContent(welcomeFileList.item(i)));
        }

        return Collections.unmodifiableList(welcomeFiles);
    }

    private static Map<Class<Throwable>, String> parseErrorPageLocations(Element webXml, XPath xpath) throws XPathExpressionException, ClassNotFoundException {
        Map<Class<Throwable>, String> errorPageLocations = new HashMap<>();
        NodeList exceptionTypes = getNodeList(webXml, xpath, XPATH_EXCEPTION_TYPE);

        for (int i = 0; i < exceptionTypes.getLength(); i++) {
            Node node = exceptionTypes.item(i);
            Class<Throwable> exceptionClass = (Class<Throwable>) Class.forName(getTextContent(node));
            String exceptionLocation = xpath.compile(XPATH_LOCATION).evaluate(node.getParentNode()).trim();
            Class<Throwable> key = (exceptionClass == Throwable.class) ? null : exceptionClass;

            if (!errorPageLocations.containsKey(key)) {
                errorPageLocations.put(key, exceptionLocation);
            }
        }

        if (!errorPageLocations.containsKey(null)) {
            String defaultLocation = xpath.compile(XPATH_ERROR_PAGE_500_LOCATION).evaluate(webXml).trim();

            if (isEmpty(defaultLocation)) {
                defaultLocation = xpath.compile(XPATH_ERROR_PAGE_DEFAULT_LOCATION).evaluate(webXml).trim();
            }

            if (!isEmpty(defaultLocation)) {
                errorPageLocations.put(null, defaultLocation);
            }
        }

        return Collections.unmodifiableMap(errorPageLocations);
    }

    private static String parseFormLoginPage(Element webXml, XPath xpath) throws XPathExpressionException {
        String formLoginPage = xpath.compile(XPATH_FORM_LOGIN_PAGE).evaluate(webXml).trim();

        if (isEmpty(formLoginPage)) {
            formLoginPage = xpath.compile(XPATH_DEFAULT_FORM_LOGIN_PAGE).evaluate(webXml).trim();
        }

        return isEmpty(formLoginPage) ? null : formLoginPage;
    }

    private static String parseFormErrorPage(Element webXml, XPath xpath) throws XPathExpressionException {
        String formErrorPage = xpath.compile(XPATH_FORM_ERROR_PAGE).evaluate(webXml).trim();

        if (isEmpty(formErrorPage)) {
            formErrorPage = xpath.compile(XPATH_DEFAULT_FORM_ERROR_PAGE).evaluate(webXml).trim();
        }

        return isEmpty(formErrorPage) ? null : formErrorPage;
    }

    private static Map<String, Set<String>> parseSecurityConstraints(Element webXml, XPath xpath) throws XPathExpressionException {
        Map<String, Set<String>> securityConstraints = new LinkedHashMap<>();
        NodeList constraints = getNodeList(webXml, xpath, XPATH_SECURITY_CONSTRAINT);

        for (int i = 0; i < constraints.getLength(); i++) {
            Node constraint = constraints.item(i);
            Set<String> roles = emptySet();
            NodeList auth = getNodeList(constraint, xpath, XPATH_AUTH_CONSTRAINT);

            if (auth.getLength() > 0) {
                NodeList authRoles = getNodeList(constraint, xpath, XPATH_AUTH_CONSTRAINT_ROLE_NAME);
                roles = new HashSet<>(authRoles.getLength());

                for (int j = 0; j < authRoles.getLength(); j++) {
                    roles.add(getTextContent(authRoles.item(j)));
                }
            }

            NodeList urlPatterns = getNodeList(constraint, xpath, XPATH_WEB_RESOURCE_URL_PATTERN);

            for (int j = 0; j < urlPatterns.getLength(); j++) {
                String urlPattern = getTextContent(urlPatterns.item(j));
                Set<String> allRoles = securityConstraints.get(urlPattern);

                if (allRoles != null) {
                    allRoles = new HashSet<>(allRoles);
                    allRoles.addAll(roles);
                }
                else {
                    allRoles = roles;
                }

                securityConstraints.put(urlPattern, unmodifiableSet(allRoles));
            }
        }

        return Collections.unmodifiableMap(securityConstraints);
    }

    private static int parseSessionTimeout(Element webXml, XPath xpath) throws XPathExpressionException {
        String sessionTimeout = xpath.compile(XPATH_SESSION_TIMEOUT).evaluate(webXml).trim();
        return isNumber(sessionTimeout) ? Integer.parseInt(sessionTimeout) : -1;
    }

    public static ServletContext getServletContext() {
        return getServletContext(getContext());
    }

    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ServletContext getServletContext(FacesContext context) {
        return (ServletContext) context.getExternalContext().getContext();
    }

    public static boolean hasContext() {
        return getContext() != null;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNumber(String string) {
        try {
            // Performance tests taught that this approach is in general faster than regex or char-by-char checking.
            return Long.valueOf(string) != null;
        }
        catch (Exception ignore) {
            logger.info("Ignoring thrown exception; the sole intent is to return false instead: {}", ignore.getMessage());
            return false;
        }
    }
}