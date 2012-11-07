package no.difi.vefa.ws.logging;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import no.difi.vefa.message.Message;
import no.difi.vefa.message.MessageType;
import no.difi.vefa.properties.PropertiesFile;

/**
 * This class is used to log usage of the validator mainly for statistic use.
 */
public class StatLogger {
	private static Logger logger = Logger.getLogger("StatLogger");
	private static FileHandler fileHandler = null;
	
	/**
	 * Set up logger.
	 * 
	 * @param propertiesFile PropertiesFile
	 * @throws Exception
	 */		
	public StatLogger(PropertiesFile propertiesFile) throws Exception {
	      LogManager logManager = LogManager.getLogManager();	      	      
	      logManager.addLogger(logger);
  
	      String pattern = propertiesFile.dataDir + "/LOG/VEFAvalidator%g.log";
	      int limit = 1000000;
	      int numLogFiles = 50000;
  
	      fileHandler = new FileHandler(pattern, limit, numLogFiles, true);      		      	      
	      
	      fileHandler.setFormatter(new Formatter() {

			@Override
			public String format(LogRecord record) {
				return new java.util.Date() + ";" + record.getLevel() + ";" + record.getMessage() + "\r\n";
			}
	      });
	      
	      logger.addHandler(fileHandler);  
	      logger.setLevel(Level.INFO);
	      logger.setUseParentHandlers(false);	      
	}

	/**
	 * Writes a log message to log the files.
	 * 
	 * @param schema Schema identificator as String
	 * @param version Version as String
	 * @param valid Is document valid as Boolean
	 * @param  messages  List of messages
	 */	
	public void logStats(String schema, String version, Boolean valid, List<Message> messages) {
	      logger.log(Level.INFO, schema + ";" + version + ";" + valid + ";" + getSchematronRules(messages));	      
	      fileHandler.close();		
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
