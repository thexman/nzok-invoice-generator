package com.a9ski;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

@NotThreadSafe
public class InvoiceGenerator {
	public void writeInvoce(File invoiceFile, List<ServiceRecord> serviceRecords, Map<String, Double> prices) throws ParserConfigurationException, TransformerException {
		final InvoiceXmlDoc doc = new InvoiceXmlDoc(XmlUtils.createDocument());
		serviceRecords.forEach(r -> doc.createBusinessOperation(r, prices.getOrDefault(r, 0D)));
		doc.writeToFile(invoiceFile);
	}
	
}
