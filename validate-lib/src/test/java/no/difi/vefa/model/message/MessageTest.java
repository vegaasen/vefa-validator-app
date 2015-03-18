package no.difi.vefa.model.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void constructor_normalProcedure() {
        final String expectedDescription = "Hint description";
        final String expectedRuleId = "BIRULE-0123-123";
        final String expectedTitle = "Hint title";

        Message message = new Message();
        Hint hint = new Hint();
        hint.setDescription(expectedDescription);
        hint.setTitle(expectedTitle);
        message.setDescription(expectedDescription);
        message.setMessageType(MessageType.Fatal);
        message.setSchematronRuleId(expectedRuleId);
        message.setTitle(expectedTitle);
        message.setValidationType(ValidationType.XSL);
        message.addHint(hint);

        assertEquals(expectedDescription, message.getDescription());
        assertEquals(MessageType.Fatal, message.getMessageType());
        assertEquals(expectedRuleId, message.getSchematronRuleId());
        assertEquals(expectedTitle, message.getTitle());
        assertEquals(ValidationType.XSL, message.getValidationType());
        assertEquals(1, message.getHints().size());
        assertEquals(expectedDescription, message.getHints().iterator().next().getDescription());
        assertEquals(expectedTitle, message.getHints().iterator().next().getTitle());
    }

}
