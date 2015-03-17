package no.difi.vefa.model.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This stuff is not in use. Why?
 */
@XmlRootElement(name = "hint")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Hint {

    private String title;
    private String description;

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

    @Override
    public String toString() {
        return "Hint{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
