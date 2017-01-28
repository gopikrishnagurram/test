package com.wavelabs.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.hibernate.MappingNotFoundException;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.xml.MappingReader;
import org.hibernate.internal.util.xml.Origin;
import org.hibernate.internal.util.xml.OriginImpl;
import org.hibernate.internal.util.xml.XmlDocument;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * 
 * XmlDocumentBuilder provides methods to get xmlDocument reference.
 * 
 * @author gopikrishnag
 *
 */
public class XmlDocumentBuilder {

	private static EntityResolver entityResolver;
	private Configuration cfg = new Configuration();

	private XmlDocument add(InputSource inputSource, Origin origin) {
		entityResolver = cfg.getEntityResolver();
		XmlDocument metadataXml = MappingReader.INSTANCE.readMappingDocument(entityResolver, inputSource, origin);
		cfg.add(metadataXml);
		return metadataXml;
	}

	private XmlDocument add(InputSource inputSource, String originType, String originName) {
		return add(inputSource, new OriginImpl(originType, originName));
	}

	/**
	 * Pass the hbm.xml with fully qualified path. returns back XmlDocument
	 * 
	 * @param xmlFile
	 * @return xmlDocument reference
	 */

	public XmlDocument getXmlDocumentObject(final String xmlFile) {
		File xmlFileObject = new File(xmlFile);
		final String name = xmlFileObject.getAbsolutePath();
		final InputSource inputSource;
		try {
			inputSource = new InputSource(new FileInputStream(xmlFile));
		} catch (FileNotFoundException e) {
			throw new MappingNotFoundException("file", xmlFile.toString());
		}
		return add(inputSource, "file", name);
	}

}
