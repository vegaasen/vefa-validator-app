package no.difi.vefa.utils;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.ValidationType;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ..what..
 *
 * @author <a href="mailto:vegaasen@gmail.com">vegaasen</a>
 */
public class MessageUtils {

    private MessageUtils() {
    }

    public static Message translate(final Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        Message message = new Message();
        message.setValidationType(ValidationType.XSL);
        message.setMessageType(MessageType.Fatal);
        message.setTitle(e.getMessage());
        message.setDescription(exceptionAsString);
        return message;
    }

}
