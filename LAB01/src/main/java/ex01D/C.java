package ex01D;

import org.apache.log4j.Logger;

public class C {

	protected static Logger log = Logger.getLogger(C.class);

	/**
	 * This method show the difference between primitive type (int) and object
	 * reference (StringBuffer)
	 * 
	 * @param i
	 * @param s
	 */
	/*
	 * can't call non-static method from static environment : void method1(int
	 * i, StringBuffer s) we should add static
	 */
	static void method1(int i, StringBuffer s) {
		i++;
		s.append("d");
	}

	public static void main(String[] args) {
		int i = 0;
		StringBuffer s = new StringBuffer("abc");
		/*
		 * the method : methode1 doesn't exit but method1 does methode1(i, s);
		 */
		log.debug("before method1 : i=" + i + " ,s=" + s);
		method1(i, s);
		log.debug("after method1 : i=" + i + " ,s=" + s);
		/*
		 * A : " is missing before i System.out.println(i=" + i + ", s=" + s);
		 * // i=0, s=abcd
		 */
		System.out.println("i=" + i + ", s=" + s); // i=0, s=abcd
	}
}

/*
 * mvn exec:java -Dexec.mainClass="ex01D.C"
 */