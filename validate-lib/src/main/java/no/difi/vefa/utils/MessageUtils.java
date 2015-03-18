package no.difi.vefa.utils;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.model.message.ValidationType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
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
        return translate(e, ValidationType.UNKNOWN);
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

    /**
     * Returns the validation message collection as XML.
     *
     * @return String Messages as XML
     * @throws Exception
     */
    public static String messagesToXml(final Messages messages) {
        try {
            Marshaller jaxbMarshaller = JAXBContext.newInstance(Messages.class).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(messages, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to marshal messages {%s}", messages.toString()), e);
        }
    }

}
