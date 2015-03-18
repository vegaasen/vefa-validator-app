package no.difi.vefa.utils;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.ValidationType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class MessagesUtilsTest {

    @Test
    public void translate_nilledException_fail() {
        assertNull(MessageUtils.translate(null));
    }

    @Test
    public void translate_normalProcedure() {
        String errorMessage = "blabla";
        Message message = MessageUtils.translate(new RuntimeException(errorMessage));
        assertNotNull(message);
        assertEquals(errorMessage, message.getTitle());
        assertEquals(ValidationType.UNKNOWN, message.getValidationType());
    }

    @Test
    public void translate_normalProcedure_withMessage() {
        String errorMessage = "blabla";
        ValidationType xsl = ValidationType.XSL;
        Message message = MessageUtils.translate(new RuntimeException(errorMessage), xsl);
        assertNotNull(message);
        assertEquals(errorMessage, message.getTitle());
        assertEquals(xsl, message.getValidationType());
    }

}
