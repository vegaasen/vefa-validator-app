package no.difi.vefa.model.xml.namespace;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class SimpleNamespaceContext implements NamespaceContext {

    private final List<XMLNamespace> namespaces;

    public SimpleNamespaceContext(List<XMLNamespace> namespaces) {
        this.namespaces = namespaces;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Iterator getPrefixes(String namespaceURI) {
        return null;
    }

    @Override
    public String getPrefix(String namespaceURI) {
        return null;
    }

    @Override
    public String getNamespaceURI(String prefix) {
        for (XMLNamespace ns : namespaces) {
            if (ns.getPrefix().equals(prefix)) {
                return ns.getUrl();
            }
        }
        return null;
    }
}
