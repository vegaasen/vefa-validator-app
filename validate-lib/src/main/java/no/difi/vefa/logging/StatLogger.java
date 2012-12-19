package no.difi.vefa.logging;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.validation.Validate;

/**
 * This class is used to log usage of the validator mainly for statistic use.
 */
public class StatLogger {
	static Logger logger = LogManager.getLogger(Validate.class.getName());    
	
	/**
	 * Writes a log message to log the files.
	 * 
	 * @param id Configuration identificator as String
	 * @param version Version as String
	 * @param valid Is document valid as Boolean
	 * @param  messages  List of messages
	 */	
	public void logStats(String id, String version, Boolean valid, List<Message> messages) {
		logger.info(id + ";" + version + ";" + valid + ";" + getSchematronRules(messages));
	}
	
	/**
	 * Extracts fatal SCHEMATRON errors
	 * 
	 * @param  messages  List of messages
	 * @return String of SCHEMATRON rules as comma separated list
	 */		
	private String getSchematronRules(List<Message> messages) {
		String r = "";
		
		for (Message message : messages) {
			if (message.messageType == MessageType.Fatal && message.schematronRuleId != "") {
				r += message.schematronRuleId + ",";
			}
		}
		
		if (r.length() > 0) {
			r = r.substring(0, r.length() - 1);
		}
		return r;
	}
}
