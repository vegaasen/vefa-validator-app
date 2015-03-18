package no.difi.vefa.ws.soap.ws;

import no.difi.vefa.common.DifiConstants;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.validation.Validate;
import no.difi.vefa.ws.model.exception.ValidationSoapServiceException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidationService.class, Validate.class})
public class ValidationServiceTest {

    private static final String EXPECTED = "pong";

    private ValidationService validationService;

    @BeforeClass
    public static void initialize() {
        System.setProperty(
                DifiConstants.Properties.PROPERTY_DATA_DIR,
                ClassLoader.getSystemResource("validator.properties").getPath()
        );
    }

    @Before
    public void setUp() {
        validationService = spy(new ValidationService());
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

    @Test(expected = ValidationSoapServiceException.class)
    public void validateInvoice_nilledAttributes_fail() throws Exception {
        Validate validate = mock(Validate.class);
        whenNew(Validate.class).withNoArguments().thenReturn(validate);
        doNothing().when(validate).setAutodetectVersionAndIdentifier(true);
        doNothing().when(validate).setSuppressWarnings(anyBoolean());
        doNothing().when(validate).setSource("");
        doThrow(new RuntimeException()).when(validate).validate();
        validationService.validateInvoice("", null, null, false);
    }

    @Test
    public void validateInvoice_normalProcedure_autodetect() throws Exception {
        Validate validate = mock(Validate.class);
        whenNew(Validate.class).withNoArguments().thenReturn(validate);
        doNothing().when(validate).setAutodetectVersionAndIdentifier(true);
        doNothing().when(validate).setSuppressWarnings(anyBoolean());
        doNothing().when(validate).setSource("");
        doNothing().when(validate).validate();
        Messages messages = mock(Messages.class);
        when(validate.getMessages()).thenReturn(messages);
        Messages result = validationService.validateInvoice("", null, null, false);
        assertNotNull(result);
        assertEquals(messages, result);
        verify(validate).getMessages();
        verify(validate).setAutodetectVersionAndIdentifier(true);
    }

    @Test
    public void validateInvoice_normalProcedure_manual() throws Exception {
        Validate validate = mock(Validate.class);
        whenNew(Validate.class).withNoArguments().thenReturn(validate);
        doNothing().when(validate).setId(EXPECTED);
        doNothing().when(validate).setSuppressWarnings(anyBoolean());
        doNothing().when(validate).setVersion(EXPECTED);
        doNothing().when(validate).setSource(EXPECTED);
        doNothing().when(validate).validate();
        Messages messages = mock(Messages.class);
        when(validate.getMessages()).thenReturn(messages);
        Messages result = validationService.validateInvoice(EXPECTED, EXPECTED, EXPECTED, false);
        assertNotNull(result);
        assertEquals(messages, result);
        verify(validate).getMessages();
        verify(validate).setId(EXPECTED);
    }

}