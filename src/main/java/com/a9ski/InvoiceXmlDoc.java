package com.a9ski;

import java.io.File;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InvoiceXmlDoc {
	private final Document doc;
	private final Element root;

	public InvoiceXmlDoc(Document doc) {
		super();
		this.doc = doc;
		
		root = doc.createElement("ELECTRONIC_INVOICE");
		root.appendChild(createTextElement("fin_document_type_code", "INVOICE"));
		doc.appendChild(root);
		// TODO: append issuer, recipient, etc.
	}
	
	public void createBusinessOperation(ServiceRecord serviceRecord, double unitPrice) {
		final Element op = doc.createElement("Business_operation");
		op.appendChild(createTextElement("activity_code", serviceRecord.getActivityCode()));
		op.appendChild(createTextElement("activity_name", serviceRecord.getDescription()));
		op.appendChild(createTextElement("unfavorable_conditions", "N"));
		op.appendChild(createTextElement("measure_code", "BR"));
		op.appendChild(createTextElement("quantity", serviceRecord.getCount()));
		op.appendChild(createTextElement("unit_price", unitPrice));
		op.appendChild(createTextElement("value_price", unitPrice * Double.valueOf(serviceRecord.getCount())));
		root.appendChild(op);
	}
	
	
	private Element createTextElement(String tag, int value) {
		return createTextElement(tag, String.valueOf(value));
	}
	
	private Element createTextElement(String tag, double value) {
		return createTextElement(tag, String.valueOf(value));
	}
	
	private Element createTextElement(String tag, String value) {
		final Element el = doc.createElement(tag);
		el.appendChild(doc.createTextNode(value));
		return el;
	}
	
	public void writeToFile(File destFile) throws TransformerException {
		XmlUtils.writeFile(destFile, doc);
	}
	
	
}
