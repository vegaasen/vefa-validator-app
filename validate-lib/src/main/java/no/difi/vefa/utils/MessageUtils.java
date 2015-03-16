package no.difi.vefa.utils;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.ValidationType;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Translate utilities for Messages. This can be used to translate from e.g an exception to a Message.
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class MessageUtils {

    private MessageUtils() {
    }

    public static Message translate(final Exception e) {
        return translate(e, ValidationType.XSL);
    }

    public static Message translate(final Exception e, final ValidationType validationType) {
        if (e != null) {
            final StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            final String exceptionAsString = sw.toString();
            final Message message = new Message();
            message.setValidationType(validationType);
            message.setMessageType(MessageType.Fatal);
            message.setTitle(e.getMessage());
            message.setDescription(exceptionAsString);
            return message;
        }
        return null;
    }

}
