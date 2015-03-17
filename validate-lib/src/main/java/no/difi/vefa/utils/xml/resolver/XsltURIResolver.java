package no.difi.vefa.utils.xml.resolver;

import no.difi.vefa.utils.PropertiesUtils;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public class XsltURIResolver implements URIResolver {

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            InputStream inputStream = new FileInputStream(PropertiesUtils.INSTANCE.getDataDir() + href);
            return new StreamSource(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
