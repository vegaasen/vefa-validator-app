package no.difi.vefa.utils.xml;

import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;
import no.difi.vefa.utils.xml.WellFormatted;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WellFormattedTest {

    @Test
    public void testMain() throws Exception {
        Messages messages = new Messages();

        WellFormatted.isValidXml("<data><test>Hello world</test></data>", messages);
        assertEquals(0, messages.getMessages().size());

        WellFormatted.isValidXml("<data><test>Hello world</test></data>error", messages);
        assertEquals(1, messages.getMessages().size());
        assertEquals(MessageType.Fatal, messages.getMessages().get(0).getMessageType());
        assertEquals(ValidationType.XMLWellFormed, messages.getMessages().get(0).getValidationType());
    }

}
