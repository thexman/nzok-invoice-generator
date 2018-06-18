package com.a9ski;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.collections4.IteratorUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.a9ski.utils.ExtCollectionUtils;

@NotThreadSafe
public class ReportParser {

	
	
	public List<ServiceRecord> parseFile(final File xmlFile) throws SAXException, IOException, XPathExpressionException, ParserConfigurationException {
		final DocumentBuilder builder = XmlUtils.createDocumentBuilder(); 
		final XPathExpression serviceExpr = XmlUtils.createXpathExpression("/report/dentalCareServices/dentalCareService/services/service");
		
		final Map<String, ServiceRecord> services = new TreeMap<>();
		final FileInputStream is = new FileInputStream(xmlFile);
		final Document xmlDocument = builder.parse(is);
		final NodeList nodes = (NodeList)serviceExpr.evaluate(xmlDocument, XPathConstants.NODESET);
		for(final Node node : IteratorUtils.asIterable(IteratorUtils.nodeListIterator(nodes))) {
			final boolean minor = isMinor(node.getParentNode().getParentNode());
			final String codeSuffix = (minor ? "11" : "12");
			final String code = attr(node, "activityCode") + codeSuffix;
			services.computeIfAbsent(code, (c -> new ServiceRecord(code, attr(node, "diagnosis")))).incrementCount();
		}
		return ExtCollectionUtils.toList(services.values());
	}
	
	private String attr(Node node, String attributeName) {
		return node.getAttributes().getNamedItem(attributeName).getTextContent();
	}
	
	private boolean isMinor(Node node) {
		return parseBirthDate(node).plusYears(18).isAfter(LocalDate.now());
	}

	private LocalDate parseBirthDate(Node node) {
		final LocalDate birthDate;
		if (Integer.parseInt(attr(node, "personType")) == 1) {
			final String personIdentifier = attr(node, "personIdentifier");
			birthDate = PersonalIdentifierUtils.parseBirthDate(personIdentifier);
		} else {
			 //birthDate="1950-01-31"
			birthDate = LocalDate.parse(attr(node, "birthDate"));
		}
		return birthDate;
	}
	
	public static void main(String[] args) throws Exception {
		final PriceParser pr = new PriceParser();
		final Map<String, Double> prices = pr.loadPrices();
		final File fin = new File("/Users/thexman/Documents/ElectronicInvoice/May/STOM.xml");
		final File fout = new File("/Users/thexman/Documents/ElectronicInvoice/May/invoice.xml");
		final ReportParser p = new ReportParser();
		final List<ServiceRecord> serviceRecords = p.parseFile(fin);
		final InvoiceGenerator g = new InvoiceGenerator();
		g.writeInvoce(fout, serviceRecords, prices);
		System.out.println("Invoice generated in " + fout.getAbsolutePath());
	}
}
