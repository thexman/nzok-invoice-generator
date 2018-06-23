package com.a9ski;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

@NotThreadSafe
public class InvoiceGenerator {
	public void writeInvoce(File invoiceFile, List<ServiceRecord> serviceRecords, Map<String, Double> prices) throws ParserConfigurationException, TransformerException, SAXException, IOException {
		final InvoiceXmlDoc doc = new InvoiceXmlDoc(XmlUtils.createDocument());
		doc.createHeaders(1,1, LocalDate.now());
		doc.createRecipient(44, "rzokName", "rzokAddress", 111111);
		doc.createIssuer("legalForm", "company", "address", true, "noVatChanging", 123, "bulstatNoVat", 611, LocalDate.now(), 42);
		doc.appendPeriod(LocalDate.of(2018, 01, 01), LocalDate.of(2018, 01, 31));
		serviceRecords.forEach(r -> doc.createBusinessOperation(r, prices.getOrDefault(r, 0D)));
		final double total = serviceRecords.stream().mapToDouble(r -> prices.getOrDefault(r, 0D)).sum();
		doc.createAggregatedAmmount(total, 0.22D, LocalDate.now());
		doc.writeToFile(invoiceFile);
	}
	
	
}
