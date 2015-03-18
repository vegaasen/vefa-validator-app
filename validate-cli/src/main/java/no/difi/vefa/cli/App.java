package no.difi.vefa.cli;

import no.difi.vefa.utils.MessageUtils;
import no.difi.vefa.validation.Validate;

import java.io.File;
import java.util.Scanner;

/**
 * This class is used to validate a xml document from the command line.
 */
public class App {
    /**
     * Performs automatic validation of input xml file
     *
     * @param args Argument 1 should be an XML file. Optional argument 2 should be version and optional argument 3 should be id.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if ((args == null || args.length == 0)) {
            System.out.println(String.format(
                    "No XML file provided.%nUsage: java -jar %s <xml-file> (opt)<version> (opt)<id>",
                    App.class.getSimpleName()
            ));
            System.exit(0);
        }
        String file = args[0];
        Scanner scanner = new Scanner(new File(file));
        String xml = scanner.useDelimiter("\\Z").next();
        Validate validate = new Validate();
        if (args.length == 1) {
            validate.setAutodetectVersionAndIdentifier(true);
            validate.setSource(xml);
            validate.validate();
            System.out.println(MessageUtils.messagesToXml(validate.getMessages()));
        }
        if (args.length == 3) {
            String version = args[1];
            String id = args[2];

            validate.setSource(xml);
            validate.setVersion(version);
            validate.setId(id);
            validate.validate();
            System.out.println(MessageUtils.messagesToXml(validate.getMessages()));
        }
        scanner.close();
    }
}
