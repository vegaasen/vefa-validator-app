package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;
import no.difi.vefa.xml.Utils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class SchematronTransformationTest {
	
	private String basePath;
	private Utils utils;
    private SchematronTransformation schematronTransformation;

	@Before
	public void setUp() throws Exception {
		basePath = new java.io.File("src/test/resources/").getCanonicalPath();
		utils = new Utils();
		schematronTransformation = new SchematronTransformation();
	}

	@Test
	public void testMain() throws Exception {
		Scanner scanner = new Scanner(new File(basePath + "/Invoice.xml"));
        String xml = scanner.useDelimiter("\\Z").next();
        Document xmlDoc = utils.stringToXMLDOM(xml);

        List<Message> messages = new ArrayList<>();
		schematronTransformation.main(xmlDoc, basePath + "/UBL-T10-EUgen.xsl", messages);		
		assertEquals(1, messages.size());
		assertEquals("EUGEN-T10-R024", messages.get(0).getSchematronRuleId());
		
		messages = new ArrayList<>();
		schematronTransformation.main(null, basePath + "/UBL-T10-EUgen.xsl", messages);
		assertEquals(1, messages.size());
		assertEquals(MessageType.Fatal, messages.get(0).getMessageType());
		assertEquals(ValidationType.XSL, messages.get(0).getValidationType());
		
		scanner.close();
	}

}
