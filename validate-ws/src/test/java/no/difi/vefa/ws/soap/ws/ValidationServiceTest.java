package no.difi.vefa.ws.soap.ws;

import no.difi.vefa.ws.model.exception.ValidationSoapServiceException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidationServiceTest {

    private static final String EXPECTED = "pong";

    private ValidationService validationService;

    @Before
    public void setUp() {
        validationService = new ValidationService();
    }

    @Test
    public void ping_withRequest() {
        String expected = "plong";
        assertEquals(expected, validationService.ping(expected));
    }

    @Test
    public void test_nilledRequest() {
        assertEquals(EXPECTED, validationService.ping(null));
    }

    @Test
    public void test_emptyRequest() {
        assertEquals(EXPECTED, validationService.ping(""));
    }

    @Test
    public void validateInvoice_normalProcedure_autodetect() throws ValidationSoapServiceException {
        validationService.validateInvoice("", null, null, false);
    }

}