package ex01E;

import org.apache.log4j.Logger;

class A {
	int x = 1;
	private static Logger log = Logger.getLogger(A.class);

	/**
	 * This method create a string and print it
	 */
	void m() {
		String message = "Je suis dans la méthode m d'une instance de C";
		System.out.println(message);
		log.info("message=" + message);
	}
}
