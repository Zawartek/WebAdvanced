package ex01E;

import org.apache.log4j.Logger;

class B extends A {
	int x = 2;
	private static Logger log = Logger.getLogger(B.class);

	/**
	 * Overload of method m() from class A, change the message
	 */
	void m() {
		String message = "Je suis dans la méthode m d'une instance de B";
		System.out.println(message);
		log.info("message=" + message);
	}
}
