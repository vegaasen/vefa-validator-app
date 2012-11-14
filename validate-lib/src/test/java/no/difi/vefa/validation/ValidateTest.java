package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import no.difi.vefa.message.Hint;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;

import org.junit.Before;
import org.junit.Test;

public class ValidateTest {

	private Validate validate;
	private List<Message> messages;
	
	@Before
	public void setUp() throws Exception {
		validate = new Validate();
		messages = new ArrayList<Message>();
	}

	@Test
	public void testMessagesAsXML() throws Exception {
		Message message = new Message();
		message.validationType = ValidationType.Configuration;
		message.messageType = MessageType.Fatal;
		message.title = "My test title";
		message.description = "My test description";
		message.schematronRuleId = "ASD-1234-BBB";

		Hint hint = new Hint();
		hint.title = "My hint title";
		hint.description = "My hint description";
		
		message.hints.add(hint);
		
		this.messages.add(message);
		
		validate.version = "1.4";
		validate.schema = "testschema";
		validate.messages = this.messages;
		validate.suppressWarnings = false;
		
		String expected = "<messages><message schema=\"testschema\" validationType=\"Configuration\" version=\"1.4\">" +
				"<messageType>Fatal</messageType><title>My test title</title><description>My test description</description>" +
				"<schematronRuleId>ASD-1234-BBB</schematronRuleId><hints><hint><title>My hint title</title>" +
				"<description>My hint description</description></hint></hints></message></messages>";
		
		assertEquals(expected, validate.messagesAsXML());
	}

}
