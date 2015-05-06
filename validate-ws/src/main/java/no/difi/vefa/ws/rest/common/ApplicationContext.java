package no.difi.vefa.ws.rest.common;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Simple applicationContext holder. This mainly holds various states of the application which last the full
 * scope of the application itself.
 *
 * @author <a href="vegaasen@gmail.com">vegardaasen</a>
 */
public enum ApplicationContext {

    INSTANCE;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private byte[] killWord;

    public byte[] generateAndStoreKillWord() {
        if (killWord == null || killWord.length == 0) {
            killWord = generateApplicationKillWord();
        }
        return killWord;
    }

    public String getKillWord() {
        if (killWord == null || killWord.length == 0) {
            throw new IllegalArgumentException("KillWord not defined, even though it was expected");
        }
        return new String(killWord);
    }

    private byte[] generateApplicationKillWord() {
        return new BigInteger(130, SECURE_RANDOM).toString(32).getBytes();
    }

}
