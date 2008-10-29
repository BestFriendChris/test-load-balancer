package com.thoughtworks.twist.core.execution.ant;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ErrorReportingModel {

    public String renderErrors(String scenarioAsHtml, String scenarioName, int lineNumber) {
        return injectErrorStyleToRenderedHtml(scenarioAsHtml, scenarioName, lineNumber);
    }

    private String injectErrorStyleToRenderedHtml(String scenarioAsHtml, String scenarioName, int lineNumber) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder newDocumentBuilder = builderFactory.newDocumentBuilder();
            Document divDocument = newDocumentBuilder.parse(new ByteArrayInputStream(scenarioAsHtml.getBytes()));
            Element div = divDocument.createElement("div");
            div.setAttribute("class", "error-line");

            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.evaluate("//*[@*='" + lineNumber + "']", divDocument, XPathConstants.NODE);
            if (node != null) {
                setErrorDivUnderTheErroredTag(div, node);
                node.appendChild(div);
                node.appendChild(addScreenShotToTheDocument(scenarioName, divDocument));
                return convertDocumentToAString(divDocument);
            }
            return scenarioAsHtml;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setErrorDivUnderTheErroredTag(Element div, Node node) {
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node removedChild = node.removeChild(nodes.item(i));
            div.appendChild(removedChild);
        }
    }

    private Element addScreenShotToTheDocument(String scenarioName, Document divDocument) {
        Element screenshot = divDocument.createElement("a");
        screenshot.setAttribute("href", "../screenshots/" + scenarioName + ".png");
        screenshot.appendChild(createThumbnailTag(scenarioName, divDocument));
        return screenshot;
    }

    private Element createThumbnailTag(String scenarioName, Document divDocument) {
        Element thumbnail = divDocument.createElement("img");
        thumbnail.setAttribute("class", "thumbnail");
        thumbnail.setAttribute("src", "../screenshots/" + scenarioName + "_thumb.png");
        thumbnail.setAttribute("alt", "Screenshot");
        return thumbnail;
    }

    private String convertDocumentToAString(Document divDocument) {
        StringWriter resultWriter = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(divDocument), new StreamResult(resultWriter));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return resultWriter.toString();
    }
}
