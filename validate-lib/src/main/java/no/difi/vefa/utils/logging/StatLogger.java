package no.difi.vefa.utils.logging;

import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.utils.PropertiesUtils;
import no.difi.vefa.validation.Validate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * This class is used to log usage of the validator mainly for statistic use.
 */
public class StatLogger {

    private static final Logger LOG = LogManager.getLogger(Validate.class.getName());
    private static final String DELIM = ",", SEMI = ";", EMPTY = "";

    static {
        System.setProperty("no.difi.vefa.validation.logging.filepath", PropertiesUtils.INSTANCE.getDataDir() + "/LOG");
    }

    /**
     * Writes a log message to log the files.
     *
     * @param id       Configuration identificator as String
     * @param version  Version as String
     * @param valid    Is document valid as Boolean
     * @param messages List of messages
     */
    public static void logStats(String id, String version, Boolean valid, Messages messages) {
        LOG.info(id + SEMI + version + SEMI + valid + SEMI + getSchematronRules(messages));
    }

    /**
     * Extracts fatal SCHEMATRON errors
     *
     * @param messages List of messages
     * @return String of SCHEMATRON rules as comma separated list
     */
    private static String getSchematronRules(Messages messages) {
        final StringBuilder r = new StringBuilder();
        messages.getMessages().parallelStream().forEach(m -> {
            r.append(m.getMessageType() == MessageType.Fatal && !Objects.equals(m.getSchematronRuleId(), "") ? m.getSchematronRuleId() + DELIM : EMPTY);
        });
        String message = r.toString();
        if (!message.isEmpty()) {
            //todo: err, what?
            message = message.substring(0, message.length() - 1);
        }
        return message;
    }
}
