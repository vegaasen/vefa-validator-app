package no.difi.vefa.ws.logging;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import no.difi.vefa.message.Message;

/**
 * This class is used to log usage of the validator mainly for statistic use.
 */
public class StatLogger {

	/**
	 * Writes a log message to log the files.
	 * 
	 * @param schema Schema identificator as String
	 * @param version Version as String
	 * @param valid Is document valid as Boolean
	 * @param  messages  List of messages
	 * @throws Exception
	 */		
	public static void info(String schema, String version, Boolean valid, List<Message> messages) throws Exception {
	      LogManager logManager = LogManager.getLogManager();
	      Logger logger = Logger.getLogger("StatLogger");
	      
	      logManager.addLogger(logger);
  
	      String pattern = "VEFAvalidator%g.log";
	      int limit = 1000000;
	      int numLogFiles = 50000;
  
	      FileHandler fileHandler = new FileHandler(pattern, limit, numLogFiles, true);      		      	      
	      
	      fileHandler.setFormatter(new Formatter() {

			@Override
			public String format(LogRecord record) {
				return new java.util.Date() + ";" + record.getLevel() + ";" + record.getMessage() + "\r\n";
			}
	      });
	      
	      logger.addHandler(fileHandler);  
	      logger.setLevel(Level.INFO);
	      logger.setUseParentHandlers(false);
	      
	      logger.log(Level.INFO, schema + ";" + version + ";" + valid);
  
	      fileHandler.close();
	}		
}
