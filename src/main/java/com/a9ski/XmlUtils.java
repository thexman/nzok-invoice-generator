package com.a9ski;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XmlUtils {
	public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = builderFactory.newDocumentBuilder();
		return builder;
	}

	public static XPathExpression createXpathExpression(String expr) throws XPathExpressionException {
		final XPath xPath = XPathFactory.newInstance().newXPath();
		final XPathExpression serviceExpr = xPath.compile(expr);
		return serviceExpr;
	}

	public static Document createDocument() throws ParserConfigurationException {
		return createDocumentBuilder().newDocument();
	}

	public static void writeFile(File file, Document doc) throws TransformerException {
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		final DOMSource source = new DOMSource(doc);
		final StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}
}
