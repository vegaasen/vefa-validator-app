package no.difi.vefa.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Message {

    private ValidationType validationType;
    private MessageType messageType;
    private String title = "";
    private String description = "";
    private String schematronRuleId = "";
    private List<Hint> hints = new ArrayList<>();

    @XmlAttribute(name = "validationType")
    public ValidationType getValidationType() {
        return validationType;
    }

    public void setValidationType(ValidationType validationType) {
        this.validationType = validationType;
    }

    @XmlElement(name = "messageType")
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "schematronRuleId")
    public String getSchematronRuleId() {
        return schematronRuleId;
    }

    public void setSchematronRuleId(String schematronRuleId) {
        this.schematronRuleId = schematronRuleId;
    }

    @XmlElement(name = "hints")
    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public void addHint(Hint hint) {
        getHints().add(hint);
    }

    public void addHints(List<Hint> hints) {
        getHints().addAll(hints);
    }

}
