package no.difi.vefa.utils.logging;

import no.difi.vefa.model.message.Message;
import no.difi.vefa.model.message.MessageType;
import no.difi.vefa.model.message.Messages;
import no.difi.vefa.validation.Validate;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * This class is used to log usage of the validator mainly for statistic use.
 */
public class StatLogger {

    private static final String DELIM = ",";
    private static final Logger LOG = LogManager.getLogger(Validate.class.getName());
    public static final String SEMI = ";";

    /**
     * Writes a log message to log the files.
     *
     * @param id       Configuration identificator as String
     * @param version  Version as String
     * @param valid    Is document valid as Boolean
     * @param messages List of messages
     */
    public void logStats(String id, String version, Boolean valid, Messages messages) {
        LOG.info(id + SEMI + version + SEMI + valid + SEMI + getSchematronRules(messages));
    }

    /**
     * Extracts fatal SCHEMATRON errors
     *
     * @param messages List of messages
     * @return String of SCHEMATRON rules as comma separated list
     */
    private String getSchematronRules(Messages messages) {
        String r = "";
        for (Message message : messages.getMessages()) {
            if (message.getMessageType() == MessageType.Fatal && !Objects.equals(message.getSchematronRuleId(), "")) {
                r += message.getSchematronRuleId() + DELIM;
            }
        }
        if (!r.isEmpty()) {
            //todo: err, what?
            r = r.substring(0, r.length() - 1);
        }
        return r;
    }
}
