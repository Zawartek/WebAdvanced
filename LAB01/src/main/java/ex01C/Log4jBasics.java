package ex01C;

import org.apache.log4j.*;

public class Log4jBasics {
	protected static Logger log = Logger.getLogger(Log4jBasics.class);

	/**
	 * Main method to test log with Log4j
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("DEBUG: Cool !");
		log.info("INFO: Cool !");
	}
}

/*
 * mvn exec:java -Dexec.mainClass="ex01C.Log4jBasics"
 * -Dlog4j.configuration=file:./src/main/resources/log4j.properties
 * 
 * le resultat dépend de la valeur écrite dans le fichier log4j.properties :
 * 
 * INFO : 18:41:41,875 INFO Log4jBasics:11 - INFO: Cool !
 * 
 * DEBUG : 18:42:08,465 DEBUG Log4jBasics:10 - DEBUG: Cool ! 18:42:08,465 INFO
 * Log4jBasics:11 - INFO: Cool !
 */