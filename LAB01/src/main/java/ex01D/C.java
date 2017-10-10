package ex01D;

public class C {
	/* void method1(int i, StringBuffer s) */
	static void method1(int i, StringBuffer s) {
		i++;
		s.append("d");
	}

	public static void main(String[] args) {
		int i = 0;
		StringBuffer s = new StringBuffer("abc");
		/* methode1(i, s); */
		method1(i, s);
		/* System.out.println(i=" + i + ", s=" + s); // i=0, s=abcd */
		System.out.println("i=" + i + ", s=" + s); // i=0, s=abcd
	}
}

/*
 * mvn exec:java -Dexec.mainClass="ex01D.C"
 */