package com.bssys.smev3.crypto.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.voskhod.crypto.XMLTransformHelper;
import ru.voskhod.smev.client.api.services.signature.Signer;
import ru.voskhod.smev.client.api.services.signature.configuration.SignerConfiguration;
import ru.voskhod.smev.client.api.signature.impl.SignerFactory;
import ru.voskhod.smev.client.api.types.exception.SMEVException;
import ru.voskhod.smev.client.api.types.exception.SMEVRuntimeException;

/**
 * Hello world!
 *
 */
public class App {

	private Signer signer;

	public App() throws SMEVRuntimeException {
		
		//validateSmev();
		signSMEV();

	}

	private SignerConfiguration getSignerConfiguration() {

		final PropertiesService propertiesService = new PropertiesService();
		SignerConfiguration signerConfiguration = new SignerConfiguration() {
			@Override
			public String getProviderName() {
				return propertiesService.get("signer.provider");
			}

			@Override
			public String getCertificateAlias() {
				return propertiesService.get("signer.certificate.alias");
			}

			@Override
			public String getPrivateKeyAlias() {
				return propertiesService.get("signer.private.key.alias");
			}

			@Override
			public String getPrivateKeyPassword() {
				return propertiesService.get("signer.private.key.password");
			}

			@Override
			public File getSMEVFileCertificateStore() {
				// TODO Auto-generated method stub
				return propertiesService.getFile("signer.smev.certificate.store");
			}

			
		};

		return signerConfiguration;
	}

	private void signSMEV() throws SMEVRuntimeException {

		SignerConfiguration config = getSignerConfiguration();
		signer = SignerFactory.getSigner(config, config.getCertificateAlias(), config.getPrivateKeyAlias(), config.getPrivateKeyPassword());

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setCoalescing(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);

		Element documentElement;
		try {
			DocumentBuilder newDocumentBuilder = documentBuilderFactory.newDocumentBuilder();
			FileReader characterStream = new FileReader("d:/Integration/BSS/SMEV3CryptoClient/client/src/main/resources/ToSmev.xml");
			documentElement = newDocumentBuilder.parse(new InputSource(characterStream)).getDocumentElement();

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			try {
				XPathExpression contentXPath = xpath.compile("//*[local-name()='SenderProvidedResponseData']");
				Element content2validate = (Element) contentXPath.evaluate(documentElement, XPathConstants.NODE);

				Element signXMLDSigDetached = signer.signXMLDSigDetached(content2validate, null);
				String elementToString = XMLTransformHelper.elementToString(signXMLDSigDetached);
				System.out.println(elementToString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void validateSmev() throws SMEVRuntimeException {
		
		SignerConfiguration config = getSignerConfiguration();
		signer = SignerFactory.getSigner(config, config.getCertificateAlias(), config.getPrivateKeyAlias(), config.getPrivateKeyPassword());

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setCoalescing(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);

		Element documentElement;
		try {
			DocumentBuilder newDocumentBuilder = documentBuilderFactory.newDocumentBuilder();
			FileReader characterStream = new FileReader("d:/Integration/BSS/SMEV3CryptoClient/client/src/main/resources/smev.xml");
			documentElement = newDocumentBuilder.parse(new InputSource(characterStream)).getDocumentElement();

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			try {
				XPathExpression contentXPath = xpath.compile("//*[local-name()='RequestMessage']");
				Element content2validate = (Element) contentXPath.evaluate(documentElement, XPathConstants.NODE);

				XPathExpression signatureXPath = xpath.compile("//*[local-name()='SMEVSignature']/*[local-name()='Signature']");
				Element signature = (Element) signatureXPath.evaluate(documentElement, XPathConstants.NODE);

				signer.validateSMEVSignature(signature, content2validate);

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SMEVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new App();
		} catch (SMEVRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
