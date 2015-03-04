package no.difi.vefa.validation;

import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WellFormedTest {

    @Test
    public void testMain() throws Exception {
        Messages messages = new Messages();
        WellFormed wellFormed = new WellFormed();

        wellFormed.main("<data><test>Hello world</test></data>", messages);
        assertEquals(0, messages.getMessages().size());

        wellFormed.main("<data><test>Hello world</test></data>error", messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XMLWellFormed, messages.getMessages().get(0).getValidationType());
    }

}
