package no.difi.vefa.model.xml.namespace;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class XMLNamespace {

    private String prefix;
    private String url;

    public XMLNamespace() {
    }

    public XMLNamespace(String prefix, String url) {
        this.prefix = prefix;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
