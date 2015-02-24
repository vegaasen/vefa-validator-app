package no.difi.vefa.message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    private Message message;

    @Before
    public void setUp() throws Exception {
        Hint hint = new Hint();
        hint.setDescription("Hint description");
        hint.setTitle("Hint title");

        message = new Message();
        message.setDescription("This is my description");
        message.setMessageType(MessageType.Fatal);
        message.setSchematronRuleId("BIRULE-0123-123");
        message.setTitle("This is my title");
        message.setValidationType(ValidationType.XSL);
        message.addHint(hint);
    }

    @Test
    public void test() {
        assertEquals("This is my description", message.getDescription());
        assertEquals(MessageType.Fatal, message.getMessageType());
        assertEquals("BIRULE-0123-123", message.getSchematronRuleId());
        assertEquals("This is my title", message.getTitle());
        assertEquals(ValidationType.XSL, message.getValidationType());

        assertEquals(1, message.getHints().size());

        assertEquals("Hint description", message.getHints().iterator().next().getDescription());
        assertEquals("Hint title", message.getHints().iterator().next().getTitle());
    }

}
