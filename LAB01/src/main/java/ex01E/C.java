package ex01E;

import org.apache.log4j.Logger;


class C extends B {
	
	int x = 3, a;
	private static Logger log = Logger.getLogger(C.class);

	void m() {
		String message = "Je suis dans la méthode m d'une instance de C";
		System.out.println(message);
		log.info("message="+message);
	}

	void test() {
		a = super.x;
		log.debug("super.x="+a);
		// Super.super impossible
		// a = super.super.x;
		a = ((B) this).x;
		log.debug("((B) this).x="+a);
		a = ((A) this).x;
		log.debug("((A) this).x="+a);
		super.m();
		// Super.super impossible
		// super.super.m();
		((B) this).m(); // (1)
	}

	public static void main(String[] args) {
		C c = new C();
		c.test();
	}
}

/*
 * mvn exec:java -Dexec.mainClass="ex01E.C"
 * 
 * La méthode m appelée est celle de la classe C.
 * 
 * De prime abord on peut penser que : ((B)this).m(); appellera la méthode m()
 * de la classe B. Ce n’est donc pas le résultat attendu. On constate donc que
 * le cast avec une autre classe ne permet pas d’atteindre ses méthodes mais
 * seulement ses attributs.
 * 
 * Cela correspond au "Multi-level inheritance" (héritage à niveaux multiples).
 */