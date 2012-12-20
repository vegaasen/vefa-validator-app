package no.difi.vefa.cli;

import no.difi.vefa.validation.Validate;

import java.io.File;
import java.util.Scanner;

/**
 * This class is used to validate a xml document from the command line.
 */
public class App 
{
	/**
	 * Performs automatic validation of input xml file
	 * 
	 * @param args Argument 1 should be an XML file
	 * @throws Exception 
	 */
    public static void main( String[] args ) throws Exception
    {
		String file = args[0];
		String xml = new Scanner(new File(file)).useDelimiter("\\Z").next();
		Validate validate = new Validate();
		validate.autodetectVersionAndIdentifier = true;
		validate.xml = xml;
		validate.main();
		System.out.println(validate.messagesAsXML());
    }
}
