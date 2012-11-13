package no.difi.vefa.message;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to describe a message.
 */
public class Message {
	/**
	 * Validation type.
	 */
	public ValidationType validationType;
	
	/**
	 * Message type.
	 */
	public MessageType messageType;

	/**
	 * Message title.
	 */
	public String title = "";
	
	/**
	 * Message description.
	 */
	public String description = "";

	/**
	 * Schematron rule id.
	 */
	public String schematronRuleId = "";

	/**
	 * Collection of message hints.
	 */
	public List<Hint> hints = new ArrayList<Hint>();
}
