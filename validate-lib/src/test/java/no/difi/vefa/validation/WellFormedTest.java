package no.difi.vefa.validation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.message.ValidationType;

import org.junit.Test;

public class WellFormedTest {
	
	@Test
	public void testMain() throws Exception {		
		List<Message> messages = new ArrayList<>();
		WellFormed wellFormed = new WellFormed();
		
		wellFormed.main("<data><test>Hello world</test></data>", messages);		
		assertEquals(0, messages.size());
		
		wellFormed.main("<data><test>Hello world</test></data>error", messages);		
		assertEquals(1, messages.size());
		assertEquals(MessageType.Fatal, messages.get(0).getMessageType());
		assertEquals(ValidationType.XMLWellFormed, messages.get(0).getValidationType());
	}

}
