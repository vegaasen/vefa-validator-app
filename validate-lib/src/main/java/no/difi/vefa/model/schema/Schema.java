package no.difi.vefa.model.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
@XmlRootElement(name = "schema")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class Schema {

    private String id;
    private String version;
    private boolean detected;

    private Schema(String id, String version, boolean detected) {
        this.id = id;
        this.version = version;
        this.detected = detected;
    }

    public static Schema create(String id, String version, boolean detected) {
        return new Schema(id, version, detected);
    }

    @XmlAttribute(name = "validationType")
    public String getId() {
        return id;
    }

    @XmlAttribute(name = "validationType")
    public String getVersion() {
        return version;
    }

    @XmlElement(name = "validationType")
    public boolean isDetected() {
        return detected;
    }

    @Override
    public String toString() {
        return "SchemaVersion{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
