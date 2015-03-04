package no.difi.vefa.model.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void constructor_normalProcedure() {
        Message message = new Message();
        Hint hint = new Hint();
        hint.setDescription("Hint description");
        hint.setTitle("Hint title");
        message.setDescription("This is my description");
        message.setMessageType(MessageType.Fatal);
        message.setSchematronRuleId("BIRULE-0123-123");
        message.setTitle("This is my title");
        message.setValidationType(ValidationType.XSL);
        message.addHint(hint);
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
