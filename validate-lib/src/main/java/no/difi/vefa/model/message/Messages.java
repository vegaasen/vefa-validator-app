package no.difi.vefa.model.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * _what_
 *
 * @author <a href="mailto:vegard.aasen1@kongsberg.com">vegaraa</a>
 */
@XmlRootElement(name = "messages")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Messages {

    private List<Message> messages = new ArrayList<>();

    @XmlElement(name = "message")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages=" + messages +
                '}';
    }
}
