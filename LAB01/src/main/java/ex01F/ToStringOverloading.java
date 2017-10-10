package ex01F;

public class ToStringOverloading {
	public static void main(String[] args) {
		Circle c1 = new Circle(0, 0, 5);
		Circle c2 = new Circle(0, 0, 3);
		System.out.println("C1 => " + c1);
	}
}

/*
 * C1 => Circle with center (0,0) and radius 5 (Perimter is 31,42)
 * 
 * La méthode toString() par défaut est appelé lorsque l'on fait un
 * System.out.println sur un objet d'une classe en écrivant une méthode
 * toString() dans la classe Circle.java on surcharge celle par défaut c'est
 * pour cela qu'elle s'exécute sans être appelée
 * 
 * Si l'on renome la méthode toString() il n'y a plus surcharge de la méthode
 * par défaut de ce fait la classe de l'objet et son adresse mémoire son
 * affichées à la place :
 * 
 * C1 => ex01F.Circle@2bb6e6dc
 */