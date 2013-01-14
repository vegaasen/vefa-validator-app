package no.difi.vefa.cache;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

/**
 * User: andy
 * Date: 1/11/13
 * Time: 4:07 PM
 */
public class SynchronisedTransformer {

    private Transformer transformer;

    public SynchronisedTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SynchronisedTransformer that = (SynchronisedTransformer) o;

        if (transformer != null ? !transformer.equals(that.transformer) : that.transformer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return transformer != null ? transformer.hashCode() : 0;
    }

    public synchronized void transform(Source streamSource, Result result) throws TransformerException {
        transformer.transform(streamSource, result);
    }
}
