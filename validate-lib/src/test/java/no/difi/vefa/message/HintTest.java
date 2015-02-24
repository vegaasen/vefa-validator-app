package no.difi.vefa.message;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HintTest {
	
	private Hint hint;

	@Before
	public void setUp() throws Exception {
		hint = new Hint();
		hint.setTitle("This is my title");
		hint.setDescription("This is my description");
	}

	@Test
	public void test() {
		assertEquals("This is my title", hint.getTitle());
		assertEquals("This is my description", hint.getDescription());
	}
}
