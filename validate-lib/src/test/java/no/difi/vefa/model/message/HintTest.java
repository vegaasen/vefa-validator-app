package no.difi.vefa.model.message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HintTest {

    @Test
    public void constructor_normalProcedure() {
        String expectedDescription = "This is my description";
        String expectedTitle = "This is my title";
        Hint hint = new Hint();
        hint.setTitle(expectedTitle);
        hint.setDescription(expectedDescription);
        assertEquals(expectedTitle, hint.getTitle());
        assertEquals(expectedDescription, hint.getDescription());
    }
}
