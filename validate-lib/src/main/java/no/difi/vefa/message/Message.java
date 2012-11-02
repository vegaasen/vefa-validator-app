package no.difi.vefa.message;

import java.util.HashSet;
import java.util.Set;

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
	public String title;
	
	/**
	 * Message description.
	 */
	public String description;
	
	/**
	 * Collection of message hints.
	 */
	public Set<Hint> hints = new HashSet<Hint>();
}
