/*
 * Created on 18-May-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.wora.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class XmlUtils {

	private static Logger log = Logger.getLogger(XmlUtils.class);

	public static final SimpleDateFormat fmtDate = new SimpleDateFormat("dd.mm.yyyy");
	public static final SimpleDateFormat fmtTimeStamp = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");
	public static final SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");

	private static DOMImplementationRegistry domImpRegistry = null;
	private static DOMImplementationLS domImpLS = null;
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * XML validator
	 */
	private static Validator validator;

	/**
	 * @return DOM Implementation Load Save
	 * @throws Exception
	 */
	private static DOMImplementationLS getDOMImplementationLS() throws Exception {
		if (domImpLS == null) {
			domImpLS = (DOMImplementationLS) getDOMImplementationRegistry().getDOMImplementation("LS");
		}
		return domImpLS;
	}

	/**
	 * @return DOM Implementation Registry
	 * @throws Exception
	 */
	private static DOMImplementationRegistry getDOMImplementationRegistry() throws Exception {
		if (domImpRegistry == null) {
			domImpRegistry = DOMImplementationRegistry.newInstance();
		}
		return domImpRegistry;
	}

	/**
	 * This method converts xml string to DOM Document
	 *
	 * @param xmlString
	 *            XML string
	 * @return DOM Document
	 */
	public static Document loadXmlFromString(String xmlString) {
		try {
			LSParser builder = getDOMImplementationLS().createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
			LSInput input = getDOMImplementationLS().createLSInput();
			input.setStringData(xmlString);
			return builder.parse(input);
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	public static Document loadXmlFromFile(String fileName) {

		try {

			LSParser builder = getDOMImplementationLS().createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
			LSInput input = getDOMImplementationLS().createLSInput();
			FileInputStream fis = new FileInputStream(fileName);

			input.setByteStream(fis);
			return builder.parse(input);

		} catch (Exception ex) {
			log.error(ex, ex);
			return null;
		}
	}

	public static Document loadXmlFromFile(File xmlFile) {

		try {

			LSParser builder = getDOMImplementationLS().createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
			LSInput input = getDOMImplementationLS().createLSInput();

			InputStream inputStream = new FileInputStream(xmlFile);

			input.setByteStream(inputStream);
			return builder.parse(input);

		} catch (Exception ex) {
			log.error(ex, ex);
			return null;
		}
	}

	/**
	 * This method convert DOM Document to string
	 *
	 * @param xmlDoc
	 *            DOM Document
	 * @return Xml String
	 */
	public static String xmlToString(Document xmlDoc) {
		return xmlToString(xmlDoc, "UTF-8");
	}

	/**
	 * This method convert DOM Document to string
	 *
	 * @param xmlDoc
	 *            DOM Document
	 * @return Xml String
	 */
	public static String xmlToString(Document xmlDoc, String encoding) {

		log.debug("xmlToString ...");
		try {

			LSSerializer serializer = getDOMImplementationLS().createLSSerializer();

			serializer.getDomConfig().setParameter("format-pretty-print", true);

			LSOutput lsOutput = getDOMImplementationLS().createLSOutput();
			lsOutput.setEncoding(encoding);
			Writer stringWriter = new StringWriter();
			lsOutput.setCharacterStream(stringWriter);
			serializer.write(xmlDoc, lsOutput);

			return stringWriter.toString();
		} catch (Exception e) {
			log.error(e, e);
		}

		return null;
	}

	/**
	 * Xml dokumani disk'e kaydeder.
	 *
	 * @param xmlDoc
	 *            : Xml dokuman
	 * @param fileName
	 *            : dosya ismi
	 * @throws Exception
	 */
	public static boolean xmlToFile(Document xmlDoc, String baseDir, String fileName) throws Exception {
		return xmlToFile(xmlDoc, baseDir, fileName, "UTF-8");
	}

	/**
	 * Xml dokumani disk'e kaydeder.
	 *
	 * @param xmlDoc
	 *            : Xml dokuman
	 * @param fileName
	 *            : dosya ismi
	 * @throws Exception
	 */
	public static boolean xmlToFile(Document xmlDoc, String baseDir, String fileName, String encoding) throws Exception {

		log.debug("Xml file saving");
		log.debug("Basedir : " + baseDir);
		log.debug("FileName : " + fileName + ".xml");
		log.debug("Encoding : " + encoding);
		LSSerializer serializer = getDOMImplementationLS().createLSSerializer();

		serializer.getDomConfig().setParameter("format-pretty-print", true);

		LSOutput lsOutput = getDOMImplementationLS().createLSOutput();
		lsOutput.setEncoding(encoding);

		FileOutputStream fos = new FileOutputStream(baseDir + File.separator + fileName + ".xml");
		lsOutput.setByteStream(fos);
		return serializer.write(xmlDoc, lsOutput);

	}

	public static void isValidXML(Document xmlDoc, String xsdFileName) throws Exception {
		log.trace("Validating xmlDoc document..");
		// validate the DOM tree
		getXsdValidator(xsdFileName).validate(new DOMSource(xmlDoc));
		log.trace("xmlDoc document is valid");
	}

	/**
	 * @param xsdFileName
	 *            XSD file name
	 * @return XSD validator
	 * @throws SAXException
	 */
	private static Validator getXsdValidator(String xsdFileName) throws SAXException {

		if (validator == null) {
			log.debug("Creating xsd validator. XSD File name : " + xsdFileName);
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance
			Source schemaFile = new StreamSource(new File(xsdFileName));
			Schema schema = factory.newSchema(schemaFile);

			validator = schema.newValidator();
		}

		// create a Validator instance, which can be used to validate an
		// instance document
		return validator;
	}

	public static Element addTextNode(Document xmlDoc, Element ndParent, String nodeName, String nodeValue) {

		Element ndNode = xmlDoc.createElement(nodeName);
		Text ndTextNode = xmlDoc.createTextNode(nodeName);
		ndTextNode.setData(nodeValue);
		ndNode.appendChild(ndTextNode);
		ndParent.appendChild(ndNode);
		return ndNode;

	}

	public static Element findElement(Node nd, String element) {

		Element res = null;
		try {
			NodeList ndList = XPathAPI.selectNodeList(nd, element);
			if (ndList.getLength() > 0) {
				res = (Element) ndList.item(0);
			}
		} catch (Exception ex) {
			log.error(ex, ex);
		}
		return res;
	}

	/**
	 * @param map
	 * @param string
	 * @return
	 */
	public static String getAtributeValue(NamedNodeMap map, String string) {
		if (map.getNamedItem(string) != null) {
			return map.getNamedItem(string).getNodeValue();
		} else {
			return null;
		}
	}

	public static Element addEmptyNode(Document xmlDoc, Element ndParent, String nodeName) {
		Element ndNode = xmlDoc.createElement(nodeName);
		ndParent.appendChild(ndNode);
		return ndNode;
	}

	public static void addNode(Document xmlDoc, Element ndParent, String nodeName, String nodeValue) {
		Element ndNode = xmlDoc.createElement(nodeName);
		Text ndTextNode = xmlDoc.createTextNode(nodeName);
		ndTextNode.setData(nodeValue);
		ndNode.appendChild(ndTextNode);
		ndParent.appendChild(ndNode);
	}

	public static CDATASection extractCDATASection(Node nd) {
		CDATASection cdata = null;
		try {
			NodeList ndlst = nd.getChildNodes();
			for (int ii = 0; ii < ndlst.getLength(); ii++) {
				if (ndlst.item(ii).getNodeType() == Node.CDATA_SECTION_NODE) {
					cdata = (CDATASection) ndlst.item(ii);
					return cdata;
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return cdata;
	}

	/**
	 * @param nd
	 * @param element
	 * @return
	 */
	public static Element findNode(Node nd, String element) {

		Element res = null;
		try {
			NodeList ndList = XPathAPI.selectNodeList(nd, element);
			if (ndList.getLength() > 0) {
				res = (Element) ndList.item(0);
			}
		} catch (Exception ex) {
			log.error(ex, ex);
		}
		return res;
	}

	public static String getValueFromXMLDoc(Document xmlDoc, String columnName) throws Exception {
		Node firstChild = xmlDoc.getFirstChild();
		for (int i = 0; i < firstChild.getChildNodes().getLength(); i++) {
			Node item = firstChild.getChildNodes().item(i);
			if (item.hasAttributes()) {
				if (item.getAttributes().getNamedItem("name").getNodeValue().equals(columnName)) {
					return item.getAttributes().getNamedItem("value").getNodeValue().trim();
				}
			}
		}
		throw new Exception("Column '" + columnName + "' could not found in xml file: " + XmlUtils.xmlToString(xmlDoc));
	}

}