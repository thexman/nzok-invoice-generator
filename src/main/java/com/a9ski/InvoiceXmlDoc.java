package com.a9ski;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class InvoiceXmlDoc {
	private final Document doc;
	private final Element root;

	public InvoiceXmlDoc(Document doc) {
		super();
		this.doc = doc;
		root = doc.createElementNS("http://pis.technologica.com/electronic_invoice.xsd", "ELECTRONIC_INVOICE");
		//root = doc.createElement("ELECTRONIC_INVOICE");
		root.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xsd","http://www.w3.org/2001/XMLSchema");
		doc.appendChild(root);
		// TODO: append issuer, recipient, etc.
	}

	public void createHeaders(int docNumber, int monthNumber, LocalDate date) {
		root.appendChild(createTextElement("fin_document_type_code", "INVOICE"));
		root.appendChild(createTextElement("fin_document_no", numberWithLeadingZeros(docNumber, 10)));
		root.appendChild(createTextElement("fin_document_month_no", numberWithLeadingZeros(monthNumber, 10)));
		root.appendChild(createTextElement("fin_document_date", date.toString()));
	}

	public void appendPeriod(LocalDate fromDate, LocalDate toDate) {
		root.appendChild(createTextElement("health_insurance_fund_type_code", "NZOK"));
		root.appendChild(createTextElement("activity_type_code", "PIDP"));
		root.appendChild(createTextElement("date_from", fromDate.toString()));
		root.appendChild(createTextElement("date_to", toDate.toString()));
	}
	
	private String numberWithLeadingZeros(int number, int len) {
		String s = String.valueOf(number);
		return leading(s, len, "0");
	}

	private String leading(String s, int len, String leading) {
		while(s.length() < len) {
			s = leading + s;
		}
		return s;
	}
	
	public void createIssuer(String legalForm, String company, String address, boolean vat, String noVatChanging, int bulstat, String bulstatNoVat, int contractNumber, LocalDate contractDate, int rhiNhifNumber) {
		final Element issuer = doc.createElement("Invoice_Issuer");
		issuer.appendChild(createTextElement("issuer_type", 0));
		issuer.appendChild(createTextElement("legal_form", legalForm));
		issuer.appendChild(createTextElement("self_insured", "N"));
		issuer.appendChild(createTextElement("self_insured_declaration", "N"));
		issuer.appendChild(createTextElement("company_name", company));
		issuer.appendChild(createTextElement("address_by_contract", address));
		issuer.appendChild(createTextElement("address_by_activity", address));
		issuer.appendChild(createTextElement("registration_by_VAT", (vat ? 1 : 0)));
		issuer.appendChild(createTextElement("grounds_for_not_charging_VAT", noVatChanging));
		issuer.appendChild(createTextElement("issuer_bulstat", numberWithLeadingZeros(bulstat, 9)));
		issuer.appendChild(createTextElement("issuer_bulstat_no_vat", bulstatNoVat));
		issuer.appendChild(createTextElement("contract_no", numberWithLeadingZeros(contractNumber,6)));
		issuer.appendChild(createTextElement("contract_date", contractDate.toString()));
		issuer.appendChild(createTextElement("rhi_nhif_no", numberWithLeadingZeros(rhiNhifNumber, 10)));
		
		root.appendChild(issuer);
	}
	
	public void createRecipient(int rzokCode, String rzokName, String rzokAddress, int rzokBulstat) {
		final Element recipient = doc.createElement("Invoice_Recipient");
		recipient.appendChild(createTextElement("recipient_code", numberWithLeadingZeros(rzokCode, 2)));
		recipient.appendChild(createTextElement("recipient_name", rzokName));
		recipient.appendChild(createTextElement("recipient_address", rzokName));
		recipient.appendChild(createTextElement("recipient_bulstat", numberWithLeadingZeros(rzokBulstat, 9)));
		root.appendChild(recipient);
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
	
	public void createAggregatedAmmount(double totalAmount, double vatRate, LocalDate date) {
		final Element e = doc.createElement("Aggregated_amounts");
		e.appendChild(createTextElement("payment_type", "B"));
		e.appendChild(createTextElement("total_amount", totalAmount));
		e.appendChild(createTextElement("vat_rate", vatRate));
		e.appendChild(createTextElement("vat_value", totalAmount * vatRate));
		e.appendChild(createTextElement("payment_amount", totalAmount * vatRate));
		e.appendChild(createTextElement("original", "Y"));
		e.appendChild(createTextElement("tax_event_date", date.toString()));
		root.appendChild(e);
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
	
	public void writeToFile(File destFile) throws TransformerException, SAXException, IOException {
		XmlUtils.writeFile(destFile, doc);
		validate(destFile);
	}
	
	private void validate(File xml) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream("/ELECTRONIC_INVOICE.XSD")));
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(xml));
	}
	
	
}
