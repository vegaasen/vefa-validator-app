package no.difi.vefa.message;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {
	
	private Message message;
	private Hint hint;

	@Before
	public void setUp() throws Exception {
		hint = new Hint();
		hint.description = "Hint description";
		hint.title = "Hint title";
		
		message = new Message();
		message.description = "This is my description";
		message.messageType = MessageType.Fatal;
		message.schematronRuleId = "BIRULE-0123-123";
		message.title = "This is my title";
		message.validationType = ValidationType.XSL;
		message.hints.add(hint);		
	}

	@Test
	public void test() {
		assertEquals("This is my description", message.description);
		assertEquals(MessageType.Fatal, message.messageType);
		assertEquals("BIRULE-0123-123", message.schematronRuleId);
		assertEquals("This is my title", message.title);
		assertEquals(ValidationType.XSL, message.validationType);

		assertEquals(1, message.hints.size());
		
		assertEquals("Hint description", message.hints.get(0).description);
		assertEquals("Hint title", message.hints.get(0).title);				
	}

}
