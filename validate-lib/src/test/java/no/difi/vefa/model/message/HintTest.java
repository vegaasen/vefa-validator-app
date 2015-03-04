package no.difi.vefa.model.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HintTest {

    @Test
    public void constructor_normalProcedure() {
        Hint hint = new Hint();
        hint.setTitle("This is my title");
        hint.setDescription("This is my description");
        assertEquals("This is my title", hint.getTitle());
        assertEquals("This is my description", hint.getDescription());
    }
}
