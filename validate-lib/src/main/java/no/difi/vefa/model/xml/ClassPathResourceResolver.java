package no.difi.vefa.model.xml;

import no.difi.vefa.utils.PropertiesUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class ClassPathResourceResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(PropertiesUtils.INSTANCE.getDataDir() + systemId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new Input(publicId, systemId, stream);
    }
}
