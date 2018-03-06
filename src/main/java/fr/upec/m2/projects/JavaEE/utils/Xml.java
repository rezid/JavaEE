package fr.upec.m2.projects.JavaEE.utils;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public final class Xml {

    private Xml() {
    }

    public static Document createDocument(List<URL> urls) throws IOException, SAXException {
        DocumentBuilder builder = createDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(document.createElement("root"));
        parseAndAppendChildren(builder, document, urls);
        return document;
    }

    public static DocumentBuilder createDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setExpandEntityReferences(false);

        try {
            return factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static void parseAndAppendChildren(DocumentBuilder builder, Document document, List<URL> urls) throws IOException, SAXException {
        for (URL url : urls) {
            if (url == null) {
                continue;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);

            try (InputStream input = connection.getInputStream()) {
                NodeList children = builder.parse(input).getDocumentElement().getChildNodes();

                for (int i = 0; i < children.getLength(); i++) {
                    document.getDocumentElement().appendChild(document.importNode(children.item(i), true));
                }
            }
        }
    }

    public static NodeList getNodeList(Node node, XPath xpath, String expression) throws XPathExpressionException {
        return (NodeList) xpath.compile(expression).evaluate(node, XPathConstants.NODESET);
    }

    public static String getTextContent(Node node) {
        return node.getFirstChild().getNodeValue().trim();
    }

    public static List<String> getNodeTextContents(URL url, String expression) {
        try {
            NodeList nodeList = getNodeList(createDocument(asList(url)).getDocumentElement(), XPathFactory.newInstance().newXPath(), expression);
            List<String> nodeTextContents = new ArrayList<>(nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                nodeTextContents.add(getTextContent(nodeList.item(i)));
            }

            return nodeTextContents;
        }
        catch (IOException | SAXException | XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

}