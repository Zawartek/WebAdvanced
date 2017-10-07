package ex01E;

class C extends B
{
  int x=3, a;

  void m()
  {
    System.out.println("Je suis dans la méthode m d'une instance de C");
  }

  void test()
  {
    a = super.x;
    System.out.println(a);
    // Super.super impossible
    //a = super.super.x;
    a = ((B)this).x;
    System.out.println(a);
    a = ((A)this).x;
    System.out.println(a);
    super.m();
    // Super.super impossible
    //super.super.m();
    ((B)this).m(); // (1)
    A aclass = new A();
    aclass.m();
    aclass = new B();
    aclass.m();
  }

  public static void main(String[] args)
  {
    C c = new C();
    c.test();
  }
}

/*
mvn exec:java -Dexec.mainClass="ex01E.C"

La méthode m appelée est celle de la classe C. 

De prime abord on peut penser que : ((B)this).m(); appellera la méthode m() de la classe B. Ce n’est donc pas le résultat attendu. On constate donc que le cast avec une autre classe ne permet pas d’atteindre ses méthodes mais seulement ses attributs.

Cela correspond au "Multi-level inheritance" (héritage à niveaux multiples).
*/